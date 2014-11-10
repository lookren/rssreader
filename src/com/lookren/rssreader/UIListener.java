package com.lookren.rssreader;

public interface UIListener {
    void setSelectedSubscription(long id);
    void setSelectedPost(long id);
    long getSelectedSubscription();
    long getSelectedPost();
}
