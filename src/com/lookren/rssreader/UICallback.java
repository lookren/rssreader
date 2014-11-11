package com.lookren.rssreader;

public interface UICallback {
    void setSelectedSubscription(long id);
    void setSelectedPost(long id);
    long getSelectedSubscription();
    long getSelectedPost();
}
