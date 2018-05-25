package com.web114.weatherapp;

/**
 * Created by user on 5/16/2018.
 */

public class WeatherObject {
    private String dayOfWeek;
    private int weatherIcon;
    private String weatherResult;
    private String weatherResultSmall;
    private String weatherdescrpition;
    private String temp_max;
    private String date;
    public WeatherObject(String dayOfWeek, int weatherIcon, String weatherResult, String weatherResultSmall,String weatherDescription,String temp_max,String date) {
        this.dayOfWeek = dayOfWeek;
        this.weatherIcon = weatherIcon;
        this.weatherResult = weatherResult;
        this.weatherResultSmall = weatherResultSmall;
        this.weatherdescrpition = weatherDescription;
        this.temp_max = temp_max;
        this.date = date;
    }
    public String getDayOfWeek() {
        return dayOfWeek;
    }
    public int getWeatherIcon() {
        return weatherIcon;
    }
    public String getWeatherResult() {
        return weatherResult;
    }
    public String getWeatherResultSmall() {
        return weatherResultSmall;
    }
    public String getWeatherdescrpition() {
        return weatherdescrpition;
    }
    public String gettemp_max() {
        return temp_max;
    }
    public String getdate() {
        return date;
    }
}
