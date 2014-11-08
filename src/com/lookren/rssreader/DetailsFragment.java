package com.lookren.rssreader;

import com.lookren.rssreader.controller.PostDetailsLoader;
import com.lookren.rssreader.model.Post;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

    private TextView mHeader;
    private TextView mDescription;

    protected ProgressBar mProgressBar;
    protected TextView mTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.details_fragment, null);
        mHeader = (TextView) root.findViewById(R.id.header);
        mDescription = (TextView) root.findViewById(R.id.description);
        mProgressBar = (ProgressBar) root.findViewById(R.id.progress);
        mTextView = (TextView) root.findViewById(R.id.empty_text);

        mHeader.setVisibility(View.GONE);
        mDescription.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.GONE);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startLoading();
    }

    private void startLoading() {
        final LoaderManager lm = getLoaderManager();
        lm.initLoader(1, null, new PostDetailsLoaderCallback());
    }

    public void restartLoading() {
        final LoaderManager lm = getLoaderManager();
        lm.restartLoader(1, null, new PostDetailsLoaderCallback());
    }

    private class PostDetailsLoaderCallback implements LoaderManager.LoaderCallbacks<Post> {

        @Override
        public Loader<Post> onCreateLoader(int id, Bundle args) {
            return new PostDetailsLoader(getActivity(), ((RssActivity)getActivity()).getSelectedPost());
        }

        @Override
        public void onLoadFinished(Loader<Post> loader, Post data) {
            if (data == null) {
                onLoaderReset(loader);
                return;
            }

            mTextView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);

            mHeader.setVisibility(View.VISIBLE);
            mDescription.setVisibility(View.VISIBLE);
            mHeader.setText(data.getName());
            mDescription.setText(data.getDescription());
        }

        @Override
        public void onLoaderReset(Loader<Post> loader) {
            mTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
