package com.example.hemant.ideatreetestapp;

public class Track {

    // Track Name
    private String mName;

    // Track Time
    private long mTimeInMilliseconds;

    // Track Image URL
    private String mUrl;

    /**
     * Create Track object
     *
     * @param name               is Name for the track
     * @param timeInMilliseconds is timeInMilliseconds
     * @param url                is url for the track image
     */
    public Track(String name, long timeInMilliseconds, String url) {
        mName = name;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
    }

    // Get name of the track
    public String getName() {
        return mName;
    }

    // Set name of the track
    public void setName(String name) {
        mName = name;
    }

    // Get timeInMilliseconds of track
    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    // Set timeInMilliseconds for track
    public void setTimeInMilliseconds(long timeInMilliseconds) {
        mTimeInMilliseconds = timeInMilliseconds;
    }

    // Get the track image URL
    public String getUrl() {
        return mUrl;
    }

    // Set the track image URL
    public void setUrl(String url) {
        mUrl = url;
    }
}
