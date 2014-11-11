package com.lookren.rssreader.model;

import com.lookren.rssreader.data.Columns;

import android.content.ContentValues;

public class Post implements RssContent, Columns.PostColumns {
    private long mId = -1;
    private final String mName;
    private final long mSubscriptionId;
    private final String mDescription;
    private final String mLink;


    public Post(long id, String name, long subscriptionId, String description, String link) {
        this(name, subscriptionId, description, link);
        mId = id;
    }

    public Post(String name, long subscriptionId, String description, String link) {
        mName = name;
        mSubscriptionId = subscriptionId;
        mDescription = description;
        mLink = link;
    }

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getLink() {
        return mLink;
    }

    public String getDescription() {
        return mDescription;
    }

    @Override
    public ContentValues toContentValues() {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(DISPLAY_NAME, mName);
        contentValues.put(SUBSCRIPTION, mSubscriptionId);
        contentValues.put(DESCRIPTION, mDescription);
        contentValues.put(LINK, mLink);
        return contentValues;
    }
}
