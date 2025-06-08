package com.example.pogodynkaapp.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FavoriteCity {
    private String cityName;
    private String country;
    private long timestamp;
    private String key;

    public FavoriteCity() {
    }

    public FavoriteCity(String cityName, String country, long timestamp) {
        this.cityName = cityName;
        this.country = country;
        this.timestamp = timestamp;
    }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getFullCityName() {
        return cityName + ", " + country;
    }

    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return "Dodano: " + sdf.format(new Date(timestamp));
    }
} 