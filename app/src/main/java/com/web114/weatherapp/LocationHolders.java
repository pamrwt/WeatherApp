package com.web114.weatherapp;

/**
 * Created by user on 5/16/2018.
 */

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;
public class LocationHolders extends RecyclerView.ViewHolder{
    private static final String TAG = LocationHolders.class.getSimpleName();
    public TextView locationCity;
    public TextView weatherInformation;
    public ImageView deleteText;
    public RadioButton selectableRadioButton;
    public LocationHolders(final View itemView, final List<LocationObject> locationObject) {
        super(itemView);
        locationCity = (TextView) itemView.findViewById(R.id.city_location);
        weatherInformation = (TextView)itemView.findViewById(R.id.temp_info);
        selectableRadioButton = (RadioButton)itemView.findViewById(R.id.radio_button);
        deleteText = (ImageView)itemView.findViewById(R.id.delete_row);

    }
}
