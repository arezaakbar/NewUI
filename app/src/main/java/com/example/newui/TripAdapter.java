package com.example.newui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {
    private ArrayList<Trip> tripList;

    public TripAdapter(ArrayList<Trip> tripList){
        this.tripList = tripList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName;

        public MyViewHolder(final View view){
            super(view);
            txtName = view.findViewById(R.id.textTrip);
        }
    }

    @NonNull
    @Override
    public TripAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripAdapter.MyViewHolder holder, int position) {
        String trip = tripList.get(position).getTrip();
        holder.txtName.setText(trip);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
}
