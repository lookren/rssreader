package com.lookren.rssreader;

import com.lookren.rssreader.controller.PostAdapter;
import com.lookren.rssreader.controller.PostLoader;
import com.lookren.rssreader.model.Post;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class PostListFragment extends AbstractListFragment {

    private PostAdapter mAdapter;

    public void startLoading() {
        final LoaderManager lm = getLoaderManager();
        lm.initLoader(1, null, new PostLoaderCallback());

        if (mEmptyTextView != null) {
            mEmptyTextView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    public void restartLoading() {
        final LoaderManager lm = getLoaderManager();
        lm.restartLoader(1, null, new PostLoaderCallback());

        if (mEmptyTextView != null) {
            mEmptyTextView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    private class PostLoaderCallback implements LoaderManager.LoaderCallbacks<List<Post>> {

        @Override
        public Loader<List<Post>> onCreateLoader(int id, Bundle args) {
            return new PostLoader(getActivity(), mUIListener != null ? mUIListener.getSelectedSubscription() : -1);
        }

        @Override
        public void onLoadFinished(Loader<List<Post>> loader, List<Post> data) {
            mAdapter = new PostAdapter(data, getActivity());
            mRecyclerView.setAdapter(mAdapter);
            mProgressBar.setVisibility(View.GONE);
            if (mAdapter.getItemCount() > 0) {
                mEmptyTextView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mEmptyTextView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Post>> loader) {
            mEmptyTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }
}
