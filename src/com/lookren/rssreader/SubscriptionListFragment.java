package com.lookren.rssreader;

import com.lookren.rssreader.controller.SubscriptionAdapter;
import com.lookren.rssreader.controller.SubscriptionLoader;
import com.lookren.rssreader.model.Subscription;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class SubscriptionListFragment extends AbstractListFragment {
    private SubscriptionAdapter mAdapter;

    public void startLoading() {
        final LoaderManager lm = getLoaderManager();
        lm.initLoader(1, null, new SubscriptionLoaderCallback());
    }

    public void restartLoading() {
        final LoaderManager lm = getLoaderManager();
        lm.restartLoader(1, null, new SubscriptionLoaderCallback());
    }

    private class SubscriptionLoaderCallback implements LoaderManager.LoaderCallbacks<List<Subscription>> {

        @Override
        public Loader<List<Subscription>> onCreateLoader(int id, Bundle args) {
            return new SubscriptionLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<List<Subscription>> loader, List<Subscription> data) {
            mAdapter = new SubscriptionAdapter(data, getActivity(), ((RssActivity)getActivity()).getSelectedSubscription());
            mRecyclerView.setAdapter(mAdapter);
            mProgressBar.setVisibility(View.GONE);
            if (mAdapter.getItemCount() > 0) {
                mTextView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mTextView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Subscription>> loader) {
            mTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }
}
