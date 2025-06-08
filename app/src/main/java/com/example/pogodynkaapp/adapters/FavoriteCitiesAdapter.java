package com.example.pogodynkaapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pogodynkaapp.R;
import com.example.pogodynkaapp.models.FavoriteCity;

import java.util.List;

public class FavoriteCitiesAdapter extends RecyclerView.Adapter<FavoriteCitiesAdapter.ViewHolder> {
    
    private List<FavoriteCity> favoriteCities;
    private OnFavoriteCityClickListener listener;

    public interface OnFavoriteCityClickListener {
        void onCityClick(FavoriteCity city);
        void onDeleteClick(FavoriteCity city);
    }

    public FavoriteCitiesAdapter(List<FavoriteCity> favoriteCities, OnFavoriteCityClickListener listener) {
        this.favoriteCities = favoriteCities;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteCity city = favoriteCities.get(position);
        
        holder.cityNameTextView.setText(city.getFullCityName());
        holder.addedDateTextView.setText(city.getFormattedDate());
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCityClick(city);
            }
        });
        
        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(city);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteCities.size();
    }

    public void updateData(List<FavoriteCity> newCities) {
        this.favoriteCities.clear();
        if (newCities != null) {
            this.favoriteCities.addAll(newCities);
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityNameTextView;
        TextView addedDateTextView;
        MaterialButton deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            cityNameTextView = itemView.findViewById(R.id.cityNameTextView);
            addedDateTextView = itemView.findViewById(R.id.addedDateTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
} 