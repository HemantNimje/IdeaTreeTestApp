package com.example.hemant.ideatreetestapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Track>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    private static final String TRACK_DATA_REQUEST_URL = "https://itunes.apple.com/search?term=Michael+jackson";

    private static final int TRACK_LOADER_ID = 0;

    private TrackAdapter mAdapter;

    private TextView mEmptyStateTextView;

    private ProgressBar mProgressBar;

    public static final String TRACK_NAME = "name";

    public static final String TRACK_TIME = "time";

    public static final String TRACK_IMAGE = "image";

    public static final String TRACK_PREVIEW_URL = "preview_url";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find reference to listView
        ListView trackListView = findViewById(R.id.list);

        // Find the EmptyTextView and set it to the listview
        mEmptyStateTextView = findViewById(R.id.empty_view);
        trackListView.setEmptyView(mEmptyStateTextView);

        // Show the progress bar
        mProgressBar = findViewById(R.id.loading_spinner);

        // Create a new adapter that takes empty list of track as input
        mAdapter = new TrackAdapter(this, new ArrayList<Track>());

        // Set the adapter to the listView
        trackListView.setAdapter(mAdapter);

        // Check if the device is connected to the internet
        if (isConnectedToNetwork()) {
            // Interact with the loaders using loader manager
            getLoaderManager().initLoader(TRACK_LOADER_ID, null, this);
        } else {
            /* Hide the progress bar */
            mProgressBar.setVisibility(View.GONE);

            /* Notify no internet connection */
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        trackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Track currentTrack = mAdapter.getItem(position);

                Intent detailIntent = new Intent(MainActivity.this,DetailActivity.class);

                Bundle track = new Bundle();
                track.putString(TRACK_NAME, currentTrack.getName());
                track.putLong(TRACK_TIME, currentTrack.getTimeInMilliseconds());
                track.putString(TRACK_IMAGE,currentTrack.getUrl());
                detailIntent.putExtras(track);

                startActivity(detailIntent);
            }
        });
    }

    /* Check if the device is connected to the network or not */
    public boolean isConnectedToNetwork() {
        ConnectivityManager cm = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    /* Override the Loader methods */
    @Override
    public Loader<List<Track>> onCreateLoader(int id, Bundle args) {
        /* Create uri for the data request url */
        Uri uri = Uri.parse(TRACK_DATA_REQUEST_URL);
        Uri.Builder builder = uri.buildUpon();

        return new TrackLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Track>> loader, List<Track> data) {
         /* Hide the loading indicator */
        mProgressBar.setVisibility(View.GONE);

        // Set empty state text to display "No track found."
        mEmptyStateTextView.setText(R.string.no_track);

        // Clear the adapter of previous track data
        mAdapter.clear();

        // update listview
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Track>> loader) {
        // On loader reset clear the adapter
        mAdapter.clear();
    }
}
