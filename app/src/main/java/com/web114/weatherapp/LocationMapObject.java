package com.web114.weatherapp;


import java.util.List;

public class LocationMapObject {

    private Coord coord;

    private List<WeatherResults> weather;

    private String base;

    private Main main;

    private long visibility;

    private Wind wind;

    private Rain rain;

    private Clouds clouds;

    private String dt;

    private Sys sys;

    private String id;

    private String name;

    private String cod;

    public LocationMapObject(Coord coord, List<WeatherResults> weather, String base, Main main, long visibility, Wind wind, Rain rain, Clouds clouds, String dt, Sys sys, String id, String name, String cod) {
        this.coord = coord;
        this.weather = weather;
        this.base = base;
        this.main = main;
        this.visibility = visibility;
        this.wind = wind;
        this.rain = rain;
        this.clouds = clouds;
        this.dt = dt;
        this.sys = sys;
        this.id = id;
        this.name = name;
        this.cod = cod;
    }

    public Coord getCoord() {
        return coord;
    }

    public List<WeatherResults> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public long getVisibility() {
        return visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public Rain getRain() {
        return rain;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public String getDt() {
        return dt;
    }

    public Sys getSys() {
        return sys;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCod() {
        return cod;
    }
}
