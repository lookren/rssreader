package com.lookren.rssreader.model;

import com.lookren.rssreader.data.Columns;

import android.content.ContentValues;

public class Subscription implements RssContent, Columns.SubscriptionColumns {
    private long mId = -1;
    private final String mName;
    private final String mUrl;

    public Subscription(long id, String name, String url) {
        this(name, url);
        mId = id;
    }

    public Subscription(String name, String url) {
        mName = name;
        mUrl = url;
    }

    @Override
    public ContentValues toContentValues() {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(DISPLAY_NAME, mName);
        contentValues.put(URL, mUrl);
        return contentValues;
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
    }

    public long getId() {
        return mId;
    }
}
