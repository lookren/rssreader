package com.lookren.rssreader.controller;

import com.lookren.rssreader.data.DatabaseHelper;
import com.lookren.rssreader.model.Post;

import android.content.Context;
import android.content.Loader;

import java.util.List;

public class PostDetailsLoader extends LoaderTask<Post> {

    private final long mPostId;

    public PostDetailsLoader(Context context, long selectedPost) {
        super(context);
        mPostId = selectedPost;
    }

    @Override
    public Post loadInBackground() {
        return DatabaseHelper.getInstance(getContext()).getPost(Post.WHERE_ID_CLAUSE,
                new String[]{String.valueOf(mPostId)}, Post.ORDER_BY_ID);
    }
}
