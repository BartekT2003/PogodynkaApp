package com.example.pogodynkaapp.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryEntry {
    private String cityName;
    private String country;
    private double temperature;
    private String description;
    private long timestamp;
    private String key;

    public HistoryEntry() {
    }

    public HistoryEntry(String cityName, String country, double temperature, 
                       String description, long timestamp) {
        this.cityName = cityName;
        this.country = country;
        this.temperature = temperature;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public String getWeatherDescription() { return description; }
    public void setWeatherDescription(String description) { this.description = description; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getFullCityName() {
        return cityName + ", " + country;
    }

    public String getFormattedDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public String getFormattedTemperature() {
        return String.format(Locale.getDefault(), "%.0f°C", temperature);
    }
} 