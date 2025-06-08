package com.example.pogodynkaapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pogodynkaapp.R;
import com.example.pogodynkaapp.models.HistoryEntry;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    
    private List<HistoryEntry> historyEntries;
    private OnHistoryClickListener listener;

    public interface OnHistoryClickListener {
        void onHistoryClick(HistoryEntry entry);
    }

    public HistoryAdapter(List<HistoryEntry> historyEntries, OnHistoryClickListener listener) {
        this.historyEntries = historyEntries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryEntry entry = historyEntries.get(position);
        
        holder.cityNameTextView.setText(entry.getFullCityName());
        holder.searchDateTextView.setText(entry.getFormattedDateTime());
        holder.temperatureTextView.setText(entry.getFormattedTemperature());
        holder.weatherDescriptionTextView.setText(entry.getWeatherDescription());
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHistoryClick(entry);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyEntries.size();
    }

    public void updateData(List<HistoryEntry> newEntries) {
        this.historyEntries.clear();
        if (newEntries != null) {
            this.historyEntries.addAll(newEntries);
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityNameTextView;
        TextView searchDateTextView;
        TextView temperatureTextView;
        TextView weatherDescriptionTextView;

        ViewHolder(View itemView) {
            super(itemView);
            cityNameTextView = itemView.findViewById(R.id.cityNameTextView);
            searchDateTextView = itemView.findViewById(R.id.searchDateTextView);
            temperatureTextView = itemView.findViewById(R.id.temperatureTextView);
            weatherDescriptionTextView = itemView.findViewById(R.id.weatherDescriptionTextView);
        }
    }
} 