package com.example.pogodynkaapp.models;

public class Weather {
    private String cityName;
    private String country;
    private double temperature;
    private String description;
    private String weatherIcon;
    private int humidity;
    private double windSpeed;
    private double feelsLike;
    private long timestamp;

    public Weather() {
    }

    public Weather(String cityName, String country, double temperature, String description, 
                   String weatherIcon, int humidity, double windSpeed, double feelsLike) {
        this.cityName = cityName;
        this.country = country;
        this.temperature = temperature;
        this.description = description;
        this.weatherIcon = weatherIcon;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.feelsLike = feelsLike;
        this.timestamp = System.currentTimeMillis();
    }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getWeatherIcon() { return weatherIcon; }
    public void setWeatherIcon(String weatherIcon) { this.weatherIcon = weatherIcon; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

    public double getFeelsLike() { return feelsLike; }
    public void setFeelsLike(double feelsLike) { this.feelsLike = feelsLike; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getFullCityName() {
        return cityName + ", " + country;
    }
} 