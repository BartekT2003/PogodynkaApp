package com.example.pogodynkaapp.api;

import java.util.List;

public class WeatherResponse {
    public String name;
    public Sys sys;
    public Main main;
    public List<Weather> weather;
    public Wind wind;

    public static class Sys {
        public String country;
    }

    public static class Main {
        public double temp;
        public double feels_like;
        public int humidity;
    }

    public static class Weather {
        public String main;
        public String description;
        public String icon;
    }

    public static class Wind {
        public double speed;
    }
} 