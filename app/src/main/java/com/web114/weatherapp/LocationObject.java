package com.web114.weatherapp;

/**
 * Created by user on 5/16/2018.
 */

import com.google.gson.annotations.SerializedName;
public class LocationObject{
private int id;
@SerializedName("name")
private String locationCity;
@SerializedName("country")
private String weatherInformation;

public LocationObject(int id, String locationCity, String weatherInformation)

    {
        this.id = id;
        this.locationCity = locationCity;
       this.weatherInformation = weatherInformation;}

    public String getLocationCity() {
        return locationCity;
    }
    public String getWeatherInformation() {
        return weatherInformation;
    }
    public int getId() {
        return id;
    }
}
