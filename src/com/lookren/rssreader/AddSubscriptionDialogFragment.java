package com.lookren.rssreader;

import com.lookren.rssreader.data.DatabaseHelper;
import com.lookren.rssreader.model.Subscription;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AddSubscriptionDialogFragment extends DialogFragment implements View.OnClickListener {

    private TextView mHeader;
    private TextView mUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.add_subscription_dialog, null);
        mHeader = (TextView) root.findViewById(R.id.header);
        mUrl = (TextView) root.findViewById(R.id.link);

        root.findViewById(R.id.save).setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                if (getActivity() == null) {
                    return null;
                }
                String name = mHeader.getText().toString();
                String url = mUrl.getText().toString();
                if (name.length() > 0 && url.length() > 0) {
                    Subscription subscription = new Subscription(name, url);
                    DatabaseHelper.getInstance(getActivity()).saveSubscription(subscription);
                    return true;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    dismiss();
                    ((RssActivity)getActivity()).showSubscriptionFragment();
                }
            }
        }.execute();
    }
}
