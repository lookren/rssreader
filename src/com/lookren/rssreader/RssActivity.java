package com.lookren.rssreader;

import com.lookren.rssreader.controller.PostAdapter;
import com.lookren.rssreader.controller.SubscriptionAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class RssActivity extends Activity implements SubscriptionAdapter.Callback, PostAdapter.Callback,
        View.OnClickListener {

    private static final String PAGE_NUM_PARAM = "param_page_num";
    private static final String SELECTED_SUBSCRIPTION_PARAM = "param_selected_subscription";
    private static final String SELECTED_POST_PARAM = "param_selected_post";
    private static final int SUBSCRIPTIONS_PAGE_NUM = 0;
    private static final int POSTS_PAGE_NUM = 1;
    private static final int DETAILS_PAGE_NUM = 2;

    private ViewGroup mContainerLeft;
    private ViewGroup mContainerRight;
    private ViewGroup mContainerLists;
    private ViewGroup mContainerDetails;

    private long mSelectedSubscriptionId = -1;
    private long mSelectedPostId = -1;

    private SubscriptionListFragment mSubscriptionListFragment;
    private PostListFragment mPostListFragment;
    private DetailsFragment mDetailsFragment;

    private View mAddSubscriptionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mAddSubscriptionButton = findViewById(R.id.fab);
        mAddSubscriptionButton.setOnClickListener(this);
        mContainerLeft = (ViewGroup) findViewById(R.id.container1);
        mContainerRight = (ViewGroup) findViewById(R.id.container2);
        mContainerLists = (ViewGroup) findViewById(R.id.lists);
        mContainerDetails = (ViewGroup) findViewById(R.id.container_details);

        int pageNum = SUBSCRIPTIONS_PAGE_NUM;
        if (savedInstanceState != null) {
            pageNum = savedInstanceState.getInt(PAGE_NUM_PARAM, SUBSCRIPTIONS_PAGE_NUM);
            mSelectedSubscriptionId = savedInstanceState.getLong(SELECTED_SUBSCRIPTION_PARAM, mSelectedSubscriptionId);
            mSelectedPostId = savedInstanceState.getLong(SELECTED_POST_PARAM, mSelectedPostId);
        }
        switch (pageNum) {
            case SUBSCRIPTIONS_PAGE_NUM:
                showSubscriptionFragment();
                break;
            case POSTS_PAGE_NUM:
                showPostFragment();
                break;
            case DETAILS_PAGE_NUM:
                showDetailsFragment();
                break;
            default:
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SELECTED_SUBSCRIPTION_PARAM, mSelectedSubscriptionId);
        outState.putLong(SELECTED_POST_PARAM, mSelectedPostId);
        outState.putInt(PAGE_NUM_PARAM,
                (mContainerLists.getVisibility() == View.GONE ? DETAILS_PAGE_NUM :
                mContainerRight.getVisibility() == View.VISIBLE ? POSTS_PAGE_NUM : SUBSCRIPTIONS_PAGE_NUM));
    }

    @Override
    public void onBackPressed() {
        if (mDetailsFragment != null) {
            showSubscriptionFragment();
            return;
        }
        super.onBackPressed();
    }

    void showSubscriptionFragment() {
        mContainerDetails.setVisibility(View.GONE);
        mContainerLists.setVisibility(View.VISIBLE);
        final FragmentManager fm = getFragmentManager();
        mSubscriptionListFragment = (SubscriptionListFragment) fm
                .findFragmentByTag(SubscriptionListFragment.class.getSimpleName());
        if (mSubscriptionListFragment != null) {
            mSubscriptionListFragment.restartLoading();
            return;
        }
        mSubscriptionListFragment = new SubscriptionListFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(mContainerLeft.getId(), mSubscriptionListFragment, SubscriptionListFragment.class.getSimpleName()).commit();
        mContainerRight.setVisibility(View.GONE);
    }

    void showPostFragment() {
        mContainerLists.setVisibility(View.VISIBLE);
        mContainerRight.setVisibility(View.VISIBLE);
        mContainerDetails.setVisibility(View.GONE);
        final FragmentManager fm = getFragmentManager();
        if (mPostListFragment == null) {
            mPostListFragment = (PostListFragment) fm.findFragmentByTag(PostListFragment.class.getSimpleName());
        }
        if (mPostListFragment != null && !mPostListFragment.isDetached()) {
            mPostListFragment.restartLoading();
            return;
        }
        mPostListFragment = new PostListFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(mContainerRight.getId(), mPostListFragment, PostListFragment.class.getSimpleName()).commit();
    }

    private void showDetailsFragment() {
        mContainerLists.setVisibility(View.GONE);
        mContainerDetails.setVisibility(View.VISIBLE);
        final FragmentManager fm = getFragmentManager();
        if (mDetailsFragment == null) {
            mDetailsFragment = (DetailsFragment) fm.findFragmentByTag(DetailsFragment.class.getSimpleName());
        }
        if (mDetailsFragment != null) {
            mDetailsFragment.restartLoading();
            return;
        }
        mDetailsFragment = new DetailsFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(mContainerDetails.getId(), mDetailsFragment, DetailsFragment.class.getSimpleName()).commit();
    }

    private void showAddSubscriptionFragment() {
        final FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentByTag(AddSubscriptionDialogFragment.class.getSimpleName());
        if (fragment != null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(fragment);
        }
        AddSubscriptionDialogFragment dialog = new AddSubscriptionDialogFragment();
        dialog.show(fm, AddSubscriptionDialogFragment.class.getSimpleName());
    }

    @Override
    public void setSelectedSubscription(long id) {
        mSelectedSubscriptionId = id;
        showPostFragment();
    }

    @Override
    public void setSelectedPost(long id) {
        mSelectedPostId = id;
        showDetailsFragment();
    }

    @Override
    public long getSelectedSubscription() {
        return mSelectedSubscriptionId;
    }
    public long getSelectedPost() {
        return mSelectedPostId;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            showAddSubscriptionFragment();
        }
    }
}
