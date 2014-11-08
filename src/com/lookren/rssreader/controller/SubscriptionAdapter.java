package com.lookren.rssreader.controller;

import com.lookren.rssreader.R;
import com.lookren.rssreader.model.Subscription;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {

    private Context mContext;
    private Callback mCallback;

    private List<Subscription> mItems;
    private long mSelectedId = -1;
    private int mLastPosition = -1;

    public interface Callback {
        void setSelectedSubscription(long id);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mText;
        LinearLayout mContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            mContainer = (LinearLayout) itemView.findViewById(R.id.item_layout_container);
            mText = (TextView) itemView.findViewById(R.id.item_layout_text);
        }
    }

    public SubscriptionAdapter(List<Subscription> items, Context context) {
        mItems = items;
        mContext = context;
        if (context instanceof Callback) {
            mCallback = (Callback) context;
        }
    }

    @Override
    public SubscriptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedId = position;
                notifyDataSetChanged();
                if (mCallback != null) {
                    mCallback.setSelectedSubscription(mItems.get(position).getId());
                }
            }
        });
        holder.mText.setText(mItems.get(position).getName());
        if (mSelectedId == position) {
            holder.mText.setTypeface(null, Typeface.BOLD);
        } else {
            holder.mText.setTypeface(null, Typeface.NORMAL);
        }
        if (position > mLastPosition) {
            setAnimation(holder.mContainer, position);
            mLastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    private void setAnimation(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
        viewToAnimate.startAnimation(animation);
    }
}