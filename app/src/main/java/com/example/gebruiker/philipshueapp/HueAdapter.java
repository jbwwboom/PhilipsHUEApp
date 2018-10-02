package com.example.gebruiker.philipshueapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class HueAdapter extends RecyclerView.Adapter<HueAdapter.HueViewHolder> {

    private ArrayList<Light> dataset;

    public HueAdapter(ArrayList<Light> dataset) {
        this.dataset = dataset;
    }

    public class HueViewHolder extends RecyclerView.ViewHolder {

        public TextView model, name;

        public HueViewHolder(View itemView) {
            super(itemView);
            model = itemView.findViewById(R.id.model);
            name = itemView.findViewById(R.id.name);
        }
    }

    @NonNull
    @Override
    public HueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.listview_row, parent, false);
        return new HueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HueViewHolder holder, int position) {
        final Light light = dataset.get(position);
        holder.model.setText(light.getModelid());
        holder.name.setText(light.getName());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
