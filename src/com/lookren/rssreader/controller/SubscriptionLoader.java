package com.lookren.rssreader.controller;

import com.lookren.rssreader.RssActivity;
import com.lookren.rssreader.data.DatabaseHelper;
import com.lookren.rssreader.model.Subscription;

import android.content.Context;

import java.util.List;

public class SubscriptionLoader extends LoaderTask<List<Subscription>> {

    public SubscriptionLoader(Context context) {
        super(context);
    }

    @Override
    public List<Subscription> loadInBackground() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(getContext());
        List<Subscription> result = dbHelper.getSubscripitonList();
        if (getContext() instanceof RssActivity && ((RssActivity)getContext()).getSelectedSubscription() <= 0
                && result.size() > 0) {
            ((RssActivity)getContext()).setSelectedSubscription(result.get(0).getId());
        }
        return result;
    }
}
