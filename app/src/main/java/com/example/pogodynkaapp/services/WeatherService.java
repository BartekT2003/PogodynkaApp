package com.example.pogodynkaapp.services;

import com.example.pogodynkaapp.api.WeatherApi;
import com.example.pogodynkaapp.api.WeatherResponse;
import com.example.pogodynkaapp.models.Weather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherService {
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "24256169e77a01b086d81b77e59956f7";
    
    private WeatherApi weatherApi;

    public WeatherService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        weatherApi = retrofit.create(WeatherApi.class);
    }

    public interface WeatherCallback {
        void onSuccess(Weather weather);
        void onError(String error);
    }

    public void getCurrentWeather(String cityName, WeatherCallback callback) {
        Call<WeatherResponse> call = weatherApi.getCurrentWeather(cityName, API_KEY, "metric");
        
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        WeatherResponse weatherResponse = response.body();
                        Weather weather = convertToWeather(weatherResponse);
                        callback.onSuccess(weather);
                    } catch (Exception e) {
                        callback.onError("Błąd parsowania danych: " + e.getMessage());
                    }
                } else {
                    String errorMsg = "Błąd API - Kod: " + response.code();
                    if (response.code() == 401) {
                        errorMsg = "Nieprawidłowy klucz API";
                    } else if (response.code() == 404) {
                        errorMsg = "Nie znaleziono miasta: " + cityName;
                    }
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                callback.onError("Błąd połączenia: " + t.getMessage());
            }
        });
    }

    public void getCurrentWeatherByLocation(double latitude, double longitude, WeatherCallback callback) {
        Call<WeatherResponse> call = weatherApi.getCurrentWeatherByCoordinates(latitude, longitude, API_KEY, "metric");
        
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    Weather weather = convertToWeather(weatherResponse);
                    callback.onSuccess(weather);
                } else {
                    callback.onError("Nie udało się pobrać pogody dla lokalizacji");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                callback.onError("Błąd połączenia: " + t.getMessage());
            }
        });
    }

    private Weather convertToWeather(WeatherResponse response) {
        try {
            String description = response.weather != null && !response.weather.isEmpty() 
                    ? response.weather.get(0).description : "Brak opisu";
            String icon = response.weather != null && !response.weather.isEmpty() 
                    ? response.weather.get(0).icon : "";
            
            return new Weather(
                    response.name != null ? response.name : "Nieznane miasto",
                    response.sys != null && response.sys.country != null ? response.sys.country : "??",
                    response.main != null ? response.main.temp : 0.0,
                    description,
                    icon,
                    response.main != null ? response.main.humidity : 0,
                    response.wind != null ? response.wind.speed : 0.0,
                    response.main != null ? response.main.feels_like : 0.0
            );
        } catch (Exception e) {
            throw new RuntimeException("Błąd parsowania danych pogodowych: " + e.getMessage());
        }
    }
} 