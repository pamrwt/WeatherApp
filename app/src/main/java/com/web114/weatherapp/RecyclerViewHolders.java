package com.web114.weatherapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 5/16/2018.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder{
    private static final String TAG = RecyclerViewHolders.class.getSimpleName();
    public TextView dayOfWeek;
    public ImageView weatherIcon;
    public TextView weatherResult;
    public TextView weatherResultSmall,temp_max,description;
    public RecyclerViewHolders(final View itemView) {
        super(itemView);
        dayOfWeek = (TextView)itemView.findViewById(R.id.day_of_week);
        weatherIcon = (ImageView)itemView.findViewById(R.id.weather_icon);
        weatherResult = (TextView) itemView.findViewById(R.id.weather_result);
        weatherResultSmall = (TextView)itemView.findViewById(R.id.weather_result_small);
        temp_max = (TextView)itemView.findViewById(R.id.temp_max);
       description = (TextView)itemView.findViewById(R.id.weather_description);
    }
}
