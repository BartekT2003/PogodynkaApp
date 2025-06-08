package com.example.pogodynkaapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.pogodynkaapp.models.Weather;
import com.example.pogodynkaapp.services.WeatherService;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int FAVORITES_REQUEST_CODE = 1002;
    private static final int HISTORY_REQUEST_CODE = 1003;
    
    private DrawerLayout drawerLayout;
    private TextInputEditText searchEditText;
    private TextView cityNameTextView, temperatureTextView, weatherDescriptionTextView, weatherIconTextView;
    private TextView feelsLikeTextView, humidityTextView, windSpeedTextView;
    private Button addToFavoritesButton;
    private ProgressBar progressBar;
    private androidx.constraintlayout.widget.ConstraintLayout mainContainer;
    
    private WeatherService weatherService;
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    
    private Weather currentWeather;
    private DecimalFormat temperatureFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_main);

            initializeComponents();
            setupToolbarAndDrawer();
            setupClickListeners();
            
            temperatureFormat = new DecimalFormat("#°C");
        } catch (Exception e) {
            Toast.makeText(this, "Błąd inicjalizacji: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void initializeComponents() {
        drawerLayout = findViewById(R.id.drawerLayout);
        searchEditText = findViewById(R.id.searchEditText);
        cityNameTextView = findViewById(R.id.cityNameTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        weatherDescriptionTextView = findViewById(R.id.weatherDescriptionTextView);
        weatherIconTextView = findViewById(R.id.weatherIconTextView);
        feelsLikeTextView = findViewById(R.id.feelsLikeTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        windSpeedTextView = findViewById(R.id.windSpeedTextView);
        addToFavoritesButton = findViewById(R.id.addToFavoritesButton);
        progressBar = findViewById(R.id.progressBar);
        mainContainer = findViewById(R.id.mainContainer);
        
        weatherService = new WeatherService();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance("https://pogodynkaapp-f3a47-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    }

    private void setupToolbarAndDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupClickListeners() {
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> searchWeather());
        
        Button currentLocationButton = findViewById(R.id.currentLocationButton);
        currentLocationButton.setOnClickListener(v -> getCurrentLocationWeather());
        
        addToFavoritesButton.setOnClickListener(v -> addToFavorites());
    }

    private void searchWeather() {
        String cityName = searchEditText.getText().toString().trim();
        if (cityName.isEmpty()) {
            Toast.makeText(this, "Wprowadź nazwę miasta", Toast.LENGTH_SHORT).show();
            return;
        }
        
        showProgress(true);
        weatherService.getCurrentWeather(cityName, new WeatherService.WeatherCallback() {
            @Override
            public void onSuccess(Weather weather) {
                runOnUiThread(() -> {
                    showProgress(false);
                    displayWeather(weather);
                    saveToHistory(weather);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showProgress(false);
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void getCurrentLocationWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 
                LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        showProgress(true);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        getWeatherByLocation(location.getLatitude(), location.getLongitude());
                    } else {
                        showProgress(false);
                        Toast.makeText(this, "Nie udało się pobrać lokalizacji", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    showProgress(false);
                    Toast.makeText(this, "Błąd lokalizacji: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void getWeatherByLocation(double latitude, double longitude) {
        weatherService.getCurrentWeatherByLocation(latitude, longitude, new WeatherService.WeatherCallback() {
            @Override
            public void onSuccess(Weather weather) {
                runOnUiThread(() -> {
                    showProgress(false);
                    displayWeather(weather);
                    saveToHistory(weather);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showProgress(false);
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void displayWeather(Weather weather) {
        currentWeather = weather;
        
        cityNameTextView.setText(weather.getCityName() + ", " + weather.getCountry());
        temperatureTextView.setText(temperatureFormat.format(weather.getTemperature()));
        weatherDescriptionTextView.setText(weather.getDescription());
        feelsLikeTextView.setText("Odczuwalna: " + temperatureFormat.format(weather.getFeelsLike()));
        humidityTextView.setText("Wilgotność: " + weather.getHumidity() + "%");
        windSpeedTextView.setText("Wiatr: " + String.format("%.1f", weather.getWindSpeed()) + " m/s");
        
        setWeatherIconAndBackground(weather.getDescription());
        addToFavoritesButton.setVisibility(View.VISIBLE);
    }

    private void setWeatherIconAndBackground(String weatherDescription) {
        String lowerDescription = weatherDescription.toLowerCase();
        String icon;
        String backgroundGradient;
        
        if (lowerDescription.contains("słońce") || lowerDescription.contains("sun") || 
            lowerDescription.contains("clear") || lowerDescription.contains("bezchmurnie")) {
            icon = "☀️";
            backgroundGradient = "sunny";
        } else if (lowerDescription.contains("deszcz") || lowerDescription.contains("rain") || 
                   lowerDescription.contains("przelotne")) {
            icon = "🌧️";
            backgroundGradient = "rainy";
        } else if (lowerDescription.contains("chmur") || lowerDescription.contains("cloud") || 
                   lowerDescription.contains("pochmurno")) {
            icon = "☁️";
            backgroundGradient = "cloudy";
        } else if (lowerDescription.contains("śnieg") || lowerDescription.contains("snow")) {
            icon = "❄️";
            backgroundGradient = "cloudy";
        } else if (lowerDescription.contains("burza") || lowerDescription.contains("thunder") || 
                   lowerDescription.contains("storm")) {
            icon = "⛈️";
            backgroundGradient = "rainy";
        } else if (lowerDescription.contains("mgła") || lowerDescription.contains("fog") || 
                   lowerDescription.contains("mist")) {
            icon = "🌫️";
            backgroundGradient = "cloudy";
        } else {
            icon = "🌤️";
            backgroundGradient = "default";
        }
        
        weatherIconTextView.setText(icon);
    }

    private void addToFavorites() {
        if (currentWeather == null) {
            Toast.makeText(this, "Najpierw wyszukaj pogodę", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Musisz być zalogowany", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String userId = mAuth.getCurrentUser().getUid();
        Map<String, Object> favoriteData = new HashMap<>();
        favoriteData.put("cityName", currentWeather.getCityName());
        favoriteData.put("country", currentWeather.getCountry());
        favoriteData.put("timestamp", System.currentTimeMillis());
        
        databaseRef.child("favorites").child(userId).push().setValue(favoriteData)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "⭐ Dodano do ulubionych!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Błąd dodawania do ulubionych", Toast.LENGTH_SHORT).show());
    }

    private void saveToHistory(Weather weather) {
        if (mAuth.getCurrentUser() == null) return;
        
        String userId = mAuth.getCurrentUser().getUid();
        Map<String, Object> historyData = new HashMap<>();
        historyData.put("cityName", weather.getCityName());
        historyData.put("country", weather.getCountry());
        historyData.put("temperature", weather.getTemperature());
        historyData.put("description", weather.getDescription());
        historyData.put("timestamp", System.currentTimeMillis());
        
        databaseRef.child("history").child(userId).push().setValue(historyData);
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationWeather();
            } else {
                Toast.makeText(this, "Uprawnienie do lokalizacji jest wymagane", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.nav_favorites) {
            openFavoritesActivity();
        } else if (id == R.id.nav_history) {
            openHistoryActivity();
        } else if (id == R.id.nav_logout) {
            logout();
        }
        
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void openFavoritesActivity() {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivityForResult(intent, FAVORITES_REQUEST_CODE);
    }

    private void openHistoryActivity() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivityForResult(intent, HISTORY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK && data != null) {
            String selectedCity = data.getStringExtra("selected_city");
            if (selectedCity != null) {
                searchEditText.setText(selectedCity);
                searchWeather();
            }
        }
    }
} 