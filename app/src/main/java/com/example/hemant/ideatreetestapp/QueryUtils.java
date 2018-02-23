package com.example.hemant.ideatreetestapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QueryUtils {

    /* LOG TAG for tags */
    public static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {

    }

    public static List<Track> fetchTrackData(String requestUrl) {

        /**
         * Sleep the main thread to show the loading spinner
         */
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL Object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to URL and receive JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error making HTTP request", e);
        }

        // Extract JSON response and create List of track object
        List<Track> tracks = extractFeaturesFromJson(jsonResponse);

        // Return the list of tracks
        return tracks;
    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }


    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the track data JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Convert the InputStream into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Return List<Track> object by parsing out the information about first track from the trackDataJSON string.
     */
    private static List<Track> extractFeaturesFromJson(String trackDataJSON) {

        // If JSON string is empty return null
        if (TextUtils.isEmpty(trackDataJSON)) {
            return null;
        }

        // Create list of tracks where we can add track data
        List<Track> tracks = new ArrayList<>();

        // Parse the JSON response
        try {
            // Convert JSON_RESPONSE String into JSONObject
            JSONObject JSONResponse = new JSONObject(trackDataJSON);

            // Get JSONArray for key results
            JSONArray trackArray = JSONResponse.getJSONArray("results");

            // Loop through the resultCount
            for (int i = 0; i < trackArray.length(); i++) {

                // Get track JSONObject at position i
                JSONObject currentTrack = trackArray.getJSONObject(i);

                // Get track name
                String trackName = currentTrack.getString("trackName");

                // Get trackTime
                long trackTimeMillis = currentTrack.getLong("trackTimeMillis");

                // Get Track Image using artworkUrl60
                String trackImage = currentTrack.getString("artworkUrl100");

                // Create track object
                Track track = new Track(trackName, trackTimeMillis, trackImage);

                // Add track to the list of tracks
                tracks.add(track);

            }
        } catch (JSONException e) {
            // Log message to show if the exception occurred while parsing JSON response
            Log.e("QueryUtils", "Problem parsing the track data JSON results", e);
        }

        // Return list of tracks
        return tracks;
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView mImageView;

        public DownloadImageTask(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];

            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error decoding Image", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageView.setImageBitmap(bitmap);
        }
    }

    public static String getTrackTime(Long timeInMilliseconds) {
        Date date = new Date(timeInMilliseconds);
        String formattedTime = formatTime(date);
        return formattedTime;
    }

    /**
     * Return the formatted date string (i.e. "4:30") from a Date object.
     */
    private static String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        return timeFormat.format(dateObject);
    }
}
