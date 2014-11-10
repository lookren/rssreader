package com.lookren.rssreader;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class AbstractListFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected ProgressBar mProgressBar;
    protected TextView mEmptyTextView;
    protected UICallback mUIListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.list_fragment, null);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) root.findViewById(R.id.progress);
        mEmptyTextView = (TextView) root.findViewById(R.id.empty_text);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mEmptyTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
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

    protected abstract void startLoading();
}
