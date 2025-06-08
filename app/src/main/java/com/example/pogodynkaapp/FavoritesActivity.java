package com.example.pogodynkaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pogodynkaapp.adapters.FavoriteCitiesAdapter;
import com.example.pogodynkaapp.models.FavoriteCity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.widget.LinearLayout;

public class FavoritesActivity extends AppCompatActivity implements FavoriteCitiesAdapter.OnFavoriteCityClickListener {
    
    private RecyclerView recyclerView;
    private FavoriteCitiesAdapter adapter;
    private List<FavoriteCity> favoriteCities;
    private ProgressBar progressBar;
    private LinearLayout emptyTextView;
    
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        initializeComponents();
        setupToolbar();
        setupRecyclerView();
        loadFavorites();
    }

    private void initializeComponents() {
        recyclerView = findViewById(R.id.favoritesRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        emptyTextView = findViewById(R.id.emptyTextView);
        
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance("https://pogodynkaapp-f3a47-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        
        favoriteCities = new ArrayList<>();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupRecyclerView() {
        adapter = new FavoriteCitiesAdapter(favoriteCities, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadFavorites() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Musisz być zalogowany", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        showProgress(true);
        String userId = mAuth.getCurrentUser().getUid();
        
        databaseRef.child("favorites").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        favoriteCities.clear();
                        
                        Toast.makeText(FavoritesActivity.this, 
                                "Pobrano " + dataSnapshot.getChildrenCount() + " ulubionych", 
                                Toast.LENGTH_SHORT).show();
                        
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            try {
                                FavoriteCity city = snapshot.getValue(FavoriteCity.class);
                                if (city != null) {
                                    city.setKey(snapshot.getKey());
                                    favoriteCities.add(city);
                                } else {
                                    city = parseManually(snapshot);
                                    if (city != null) {
                                        city.setKey(snapshot.getKey());
                                        favoriteCities.add(city);
                                    }
                                }
                            } catch (Exception e) {
                                FavoriteCity city = parseManually(snapshot);
                                if (city != null) {
                                    city.setKey(snapshot.getKey());
                                    favoriteCities.add(city);
                                }
                            }
                        }
                        
                        adapter.updateData(new ArrayList<>(favoriteCities));
                        showProgress(false);
                        
                        if (favoriteCities.isEmpty()) {
                            emptyTextView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            emptyTextView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        showProgress(false);
                        Toast.makeText(FavoritesActivity.this, 
                                "Błąd ładowania ulubionych: " + databaseError.getMessage(), 
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCityClick(FavoriteCity city) {
        Intent intent = new Intent();
        intent.putExtra("selected_city", city.getCityName());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDeleteClick(FavoriteCity city) {
        new AlertDialog.Builder(this)
                .setTitle("Usuń z ulubionych")
                .setMessage("Czy na pewno chcesz usunąć " + city.getFullCityName() + " z ulubionych?")
                .setPositiveButton("Usuń", (dialog, which) -> deleteFavorite(city))
                .setNegativeButton("Anuluj", null)
                .show();
    }

    private void deleteFavorite(FavoriteCity city) {
        if (mAuth.getCurrentUser() == null || city.getKey() == null) return;
        
        String userId = mAuth.getCurrentUser().getUid();
        databaseRef.child("favorites").child(userId).child(city.getKey())
                .removeValue()
                .addOnSuccessListener(aVoid -> 
                    Toast.makeText(this, "Usunięto z ulubionych", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> 
                    Toast.makeText(this, "Błąd usuwania", Toast.LENGTH_SHORT).show());
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    
    private FavoriteCity parseManually(DataSnapshot snapshot) {
        try {
            Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
            if (data == null) return null;
            
            String cityName = (String) data.get("cityName");
            String country = (String) data.get("country");
            Long timestamp = (Long) data.get("timestamp");
            
            if (timestamp == null) {
                Object timestampObj = data.get("timestamp");
                if (timestampObj instanceof Integer) {
                    timestamp = ((Integer) timestampObj).longValue();
                } else if (timestampObj instanceof Double) {
                    timestamp = ((Double) timestampObj).longValue();
                } else {
                    timestamp = System.currentTimeMillis();
                }
            }
            
            if (cityName != null) {
                return new FavoriteCity(cityName, country != null ? country : "", timestamp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
} 