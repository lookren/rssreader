package com.lookren.rssreader.controller;

import android.content.AsyncTaskLoader;
import android.content.Context;

public abstract class LoaderTask<RESULT> extends AsyncTaskLoader<RESULT> {

    private RESULT mResult;

    public LoaderTask(Context context) {
        super(context);
    }

    // Loader
    @Override
    public void deliverResult(RESULT result) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            return;
        }

        mResult = result;

        if (isStarted()) {
            super.deliverResult(result);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mResult != null) {
            deliverResult(mResult);
        }
        if (takeContentChanged() || mResult == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        mResult = null;
    }

    // AsyncTaskLoader<RESULT>
    @Override
    public void onCanceled(RESULT result) {
        mResult = null;
    }
}
