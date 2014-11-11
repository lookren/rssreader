package com.lookren.rssreader;

import com.lookren.rssreader.controller.PostDetailsLoader;
import com.lookren.rssreader.model.Post;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

    private TextView mHeader;
    private TextView mDescription;

    private ProgressBar mProgressBar;
    private TextView mEmptyText;
    private WebView mWebView;

    private UICallback mUIListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.details_fragment, null);
        mHeader = (TextView) root.findViewById(R.id.header);
        mDescription = (TextView) root.findViewById(R.id.description);
        mProgressBar = (ProgressBar) root.findViewById(R.id.progress);
        mEmptyText = (TextView) root.findViewById(R.id.empty_text);
        mWebView = (WebView) root.findViewById(R.id.web_view);

        mHeader.setVisibility(View.GONE);
        mDescription.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyText.setVisibility(View.GONE);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startLoading();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof UICallback) {
            mUIListener = (UICallback) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mUIListener = null;
    }

    private void startLoading() {
        mWebView.setVisibility(View.GONE);
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
            return new PostDetailsLoader(getActivity(), mUIListener != null ? mUIListener.getSelectedPost() : -1);
        }

        @Override
        public void onLoadFinished(Loader<Post> loader, Post data) {
            if (data == null) {
                onLoaderReset(loader);
                return;
            }

            mEmptyText.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);

            mHeader.setVisibility(View.VISIBLE);
            mDescription.setVisibility(View.VISIBLE);
            mHeader.setText(data.getName());
            mDescription.setText(Html.fromHtml(data.getDescription()));
            mDescription.setMovementMethod(LinkMovementMethod.getInstance());
            mWebView.setVisibility(View.GONE);
            mWebView.loadUrl(data.getLink());
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    mProgressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    mWebView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onLoaderReset(Loader<Post> loader) {
            mEmptyText.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mWebView.setVisibility(View.GONE);
        }
    }
}
