package com.example.hemant.ideatreetestapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class TrackAdapter extends ArrayAdapter<Track> {
    public TrackAdapter(@NonNull Context context, ArrayList<Track> tracks) {
        super(context, 0, tracks);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view can be reused, else create a new view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent,
                    false);
        }

        // Get the object located in that position in the list
        Track currentTrack = getItem(position);

        // Find the textview for the track name
        TextView trackName = listItemView.findViewById(R.id.track_name);

        // Set the name of the currentTrack
        trackName.setText(currentTrack.getName());

        // Set the image of the currentTrack
        ImageView trackImage = listItemView.findViewById(R.id.track_image);

        if (currentTrack != null){
            new QueryUtils.DownloadImageTask(trackImage).execute(currentTrack.getUrl());
        }

        return listItemView;
    }
}
