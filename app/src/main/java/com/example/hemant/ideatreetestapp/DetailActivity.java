package com.example.hemant.ideatreetestapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import static com.example.hemant.ideatreetestapp.MainActivity.TRACK_IMAGE;
import static com.example.hemant.ideatreetestapp.MainActivity.TRACK_NAME;
import static com.example.hemant.ideatreetestapp.MainActivity.TRACK_PREVIEW_URL;
import static com.example.hemant.ideatreetestapp.MainActivity.TRACK_TIME;

public class DetailActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Access the bundle passed to this activity
        Intent parent = getIntent();
        Bundle parentBundle= parent.getExtras();

        // Get the values from the bundle
        String name = parentBundle.getString(TRACK_NAME);
        Long time = parentBundle.getLong(TRACK_TIME);
        String url = parentBundle.getString(TRACK_IMAGE);
        String previewUrl = parentBundle.getString(TRACK_PREVIEW_URL);

        // Find the layout components
        TextView trackName = findViewById(R.id.detail_track_name);
        TextView trackTime = findViewById(R.id.detail_track_time);
        ImageView trackImage = findViewById(R.id.detail_track_image);

        trackName.setText(name);
        trackTime.setText(QueryUtils.getTrackTime(time));
        new QueryUtils.DownloadImageTask(trackImage).execute(url);
    }
}
