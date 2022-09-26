package com.example.newui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TripHistory extends AppCompatActivity {
private ArrayList<Trip> TripList;
private RecyclerView recyclerView;

    BottomNavigationView bottomNavigationMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);

        bottomNavigationMenu = findViewById(R.id.bottomNavigationMenu);
        bottomNavigationMenu.setSelectedItemId(R.id.menuhistory);
        recyclerView = findViewById(R.id.tripRecycler);

        TripList = new ArrayList<>();
        
        setUserInfo();
        setAdapter();


        bottomNavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menumode:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menuhistory:
                        return true;
                }

                return false;
            }
        });
    }

    private void setAdapter() {
        TripAdapter adapter = new TripAdapter(TripList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setUserInfo() {
        TripList.add(new Trip("Trip 1"));
        TripList.add(new Trip("Trip 2"));
        TripList.add(new Trip("Trip 3"));
        TripList.add(new Trip("Trip 4"));
        TripList.add(new Trip("Trip 5"));
    }
}