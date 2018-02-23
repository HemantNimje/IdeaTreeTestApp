package com.example.hemant.ideatreetestapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class TrackLoader extends AsyncTaskLoader<List<Track>> {

    /* Query URL */
    private String mUrl;

    public TrackLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    /* To start loader force load it */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Track> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform network request and extract list of users
        List<Track> result = QueryUtils.fetchTrackData(mUrl);
        return result;
    }
}
