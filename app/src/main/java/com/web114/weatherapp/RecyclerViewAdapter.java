package com.web114.weatherapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by user on 5/16/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
    private List<WeatherObject> dailyWeather;
    protected Context context;
    public RecyclerViewAdapter(Context context, List<WeatherObject> dailyWeather) {
        this.dailyWeather = dailyWeather;
        this.context = context;
    }
    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolders viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_daily_list, parent, false);
        viewHolder = new RecyclerViewHolders(layoutView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.dayOfWeek.setText(dailyWeather.get(position).getDayOfWeek());
       // holder.weatherIcon.setImageResource(dailyWeather.get(position).getWeatherIcon());
        double mTemp = Double.parseDouble(dailyWeather.get(position).getWeatherResult());
        holder.weatherResult.setText(String.valueOf(Math.round(mTemp)) + "°C");
        holder.weatherResultSmall.setText(dailyWeather.get(position).getWeatherResultSmall());
        holder.temp_max.setText(dailyWeather.get(position).getWeatherdescrpition());
        switch (dailyWeather.get(position).getWeatherdescrpition()){
            case "Haze":
                holder.weatherIcon.setImageResource(R.drawable.sun);
                break;
            case"clear sky":
                    holder.weatherIcon.setImageResource(R.drawable.sunny);
                break;
            case"few clouds":
                holder.weatherIcon.setImageResource(R.drawable.sun);
                break;
            case "broken clouds":
                holder.weatherIcon.setImageResource(R.drawable.sun);
                break;
            case"light rain":
                holder.weatherIcon.setImageResource(R.drawable.rain);
                break;
            case"scattered clouds":
                holder.weatherIcon.setImageResource(R.drawable.sun);
                break;
            default:
                holder.weatherIcon.setImageResource(R.drawable.sunny);
                break;
        }
        holder.description.setText(dailyWeather.get(position).getdate());
        holder.weatherResultSmall.setVisibility(View.GONE);
    }
    @Override
    public int getItemCount() {
        return 5;
    }
}