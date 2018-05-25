package com.web114.weatherapp;

public class Wind {

    private String speed;

    private int deg;

    public Wind(String speed, int deg) {
        this.speed = speed;
        this.deg = deg;
    }

    public String getSpeed() {
        return speed;
    }

    public int getDeg() {
        return deg;
    }
}
