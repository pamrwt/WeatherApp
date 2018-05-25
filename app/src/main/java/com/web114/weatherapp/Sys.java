package com.web114.weatherapp;


public class Sys {

    private String type;

    private String id;

    private String message;

    private String country;

    private long sunrise;

    private long sunset;

    public Sys(String type, String id, String message, String country, long sunrise, long sunset) {
        this.type = type;
        this.id = id;
        this.message = message;
        this.country = country;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getCountry() {
        return country;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }
}
