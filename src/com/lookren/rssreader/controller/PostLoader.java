package com.lookren.rssreader.controller;

import com.lookren.rssreader.data.DatabaseHelper;
import com.lookren.rssreader.model.Post;
import com.lookren.rssreader.model.Subscription;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PostLoader extends LoaderTask<List<Post>> {

    private long mSubscriptionId;

    public PostLoader(Context context, long subscriptionId) {
        super(context);
        mSubscriptionId = subscriptionId;
    }

    @Override
    public List<Post> loadInBackground() {
        Subscription subscription = DatabaseHelper.getInstance(getContext()).getSubscripiton(mSubscriptionId);
        if (subscription == null) {
            return null;
        }
        List<Post> rssItems = null;
        try {
            RssParser parser = new RssParser();
            rssItems = parser.parse(getInputStream(subscription.getUrl()));
        } catch (XmlPullParserException e) {
            Log.w(e.getMessage(), e);
        } catch (IOException e) {
            Log.w(e.getMessage(), e);
        }
        if (rssItems != null && rssItems.size() > 0) {
            DatabaseHelper.getInstance(getContext())
                    .deletePosts(Post.WHERE_SUBSCRIPTION_ID_CLAUSE, new String[]{String.valueOf(mSubscriptionId)});
            for (Post each : rssItems) {
                DatabaseHelper.getInstance(getContext()).savePost(each, mSubscriptionId);
            }
        }
        return DatabaseHelper.getInstance(getContext())
                .getPostList(Post.WHERE_SUBSCRIPTION_ID_CLAUSE, new String[]{String.valueOf(mSubscriptionId)},
                        Post.ORDER_BY_ID);
    }

    public InputStream getInputStream(String link) {
        try {
            URL url = new URL(link);
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            Log.w(PostLoader.class.getSimpleName(), "Exception while retrieving the input stream", e);
            return null;
        }
    }

    public class RssParser {

        // We don't use namespaces
        private final String ns = null;

        public List<Post> parse(InputStream inputStream) throws XmlPullParserException, IOException {
            if (inputStream == null) {
                return null;
            }
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inputStream, null);
                parser.nextTag();
                return readFeed(parser);
            } finally {
                inputStream.close();
            }
        }

        private List<Post> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, null, "rss");
            String title = null;
            String link = null;
            String description = null;
            List<Post> items = new ArrayList<Post>();
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("title")) {
                    title = readTitle(parser);
                } else if (name.equals("link")) {
                    link = readLink(parser);
                } else if (name.equals("description")) {
                    description = readDescripiton(parser);
                }
                if (title != null && link != null && description != null) {
                    Post item = new Post(title, mSubscriptionId, description, link);
                    items.add(item);
                    title = null;
                    link = null;
                }
            }
            return items;
        }

        private String readLink(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "link");
            String link = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "link");
            return link;
        }

        private String readTitle(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "title");
            String title = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "title");
            return title;
        }

        private String readDescripiton(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "description");
            String title = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "description");
            return title;
        }

        private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
            String result = "";
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            return result;
        }
    }
}
