package com.example.pogodynkaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pogodynkaapp.adapters.HistoryAdapter;
import com.example.pogodynkaapp.models.HistoryEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.widget.LinearLayout;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.OnHistoryClickListener {
    
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<HistoryEntry> historyEntries;
    private ProgressBar progressBar;
    private LinearLayout emptyTextView;
    private MaterialButton clearHistoryButton;
    
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initializeComponents();
        setupToolbar();
        setupRecyclerView();
        setupClickListeners();
        loadHistory();
    }

    private void initializeComponents() {
        recyclerView = findViewById(R.id.historyRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        emptyTextView = findViewById(R.id.emptyTextView);
        clearHistoryButton = findViewById(R.id.clearHistoryButton);
        
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance("https://pogodynkaapp-f3a47-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        
        historyEntries = new ArrayList<>();
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
        adapter = new HistoryAdapter(historyEntries, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        clearHistoryButton.setOnClickListener(v -> showClearHistoryDialog());
    }

    private void loadHistory() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Musisz być zalogowany", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        showProgress(true);
        String userId = mAuth.getCurrentUser().getUid();
        
        Query historyQuery = databaseRef.child("history").child(userId)
                .orderByChild("timestamp");
        
        historyQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                historyEntries.clear();
                
                Toast.makeText(HistoryActivity.this, 
                        "Pobrano " + dataSnapshot.getChildrenCount() + " wpisów historii", 
                        Toast.LENGTH_SHORT).show();
                
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HistoryEntry entry = snapshot.getValue(HistoryEntry.class);
                    if (entry != null) {
                        entry.setKey(snapshot.getKey());
                        historyEntries.add(entry);
                    }
                }
                
                Collections.reverse(historyEntries);
                
                adapter.updateData(new ArrayList<>(historyEntries));
                showProgress(false);
                
                if (historyEntries.isEmpty()) {
                    emptyTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    clearHistoryButton.setEnabled(false);
                } else {
                    emptyTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    clearHistoryButton.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showProgress(false);
                Toast.makeText(HistoryActivity.this, 
                        "Błąd ładowania historii: " + databaseError.getMessage(), 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onHistoryClick(HistoryEntry entry) {
        Intent intent = new Intent();
        intent.putExtra("selected_city", entry.getCityName());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showClearHistoryDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Wyczyść historię")
                .setMessage("Czy na pewno chcesz usunąć całą historię wyszukiwań?")
                .setPositiveButton("Wyczyść", (dialog, which) -> clearHistory())
                .setNegativeButton("Anuluj", null)
                .show();
    }

    private void clearHistory() {
        if (mAuth.getCurrentUser() == null) return;
        
        showProgress(true);
        String userId = mAuth.getCurrentUser().getUid();
        
        databaseRef.child("history").child(userId)
                .removeValue()
                .addOnSuccessListener(aVoid -> {
                    showProgress(false);
                    Toast.makeText(this, "Historia wyczyszczona", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    showProgress(false);
                    Toast.makeText(this, "Błąd czyszczenia historii", Toast.LENGTH_SHORT).show();
                });
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 