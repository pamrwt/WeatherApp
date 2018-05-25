package com.web114.weatherapp;

/**
 * Created by user on 5/18/2018.
 */

public class Weather {
    private String description;

    private String pressure;

    private String humidity;

    private String temp_min;

    private String temp_max;
    private String icon;

    public Weather(String description, String pressure, String humidity, String temp_min, String temp_max, String icon) {
        this.description = description;
        this.pressure = pressure;
        this.humidity = humidity;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumudity() {
        return humidity;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public String getTemp_max() {
        return temp_max;
    }
    public String getIcon() {
        return icon;
    }
}
