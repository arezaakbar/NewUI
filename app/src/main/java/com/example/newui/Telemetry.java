package com.example.newui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Telemetry extends AppCompatActivity {
//private ArrayList<Trip> tripList;
//private RecyclerView recyclerView;

    BottomNavigationView bottomNavigationMenu;
    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);

        bottomNavigationMenu = findViewById(R.id.bottomNavigationMenu);
        bottomNavigationMenu.setSelectedItemId(R.id.menuhistory);

        final TextView displayFuel = findViewById(R.id.textFuelValue);
        final TextView displayLvl = findViewById(R.id.textAdcVal);
        final TextView displayAfr = findViewById(R.id.textAfrValue);
//        final TextView displayVolt = findViewById(R.id.textVoltVal);
        final TextView displayRpm = findViewById(R.id.textRpmValue);
        final TextView displayFreq = findViewById(R.id.textFreqVal);
        // recyclerView = findViewById(R.id.tripRecycler);

//        tripList = new ArrayList<>();
//
//        setUserInfo();
//        setAdapter();

        float lvl = getIntent().getFloatExtra(MainActivity.EXTRA_DATA_LVL,0f);
        float fuel = getIntent().getFloatExtra(MainActivity.EXTRA_DATA_FUEL, 0f);
        float afr = getIntent().getFloatExtra(MainActivity.EXTRA_DATA_AFR, 0f);
//        float volt = getIntent().getFloatExtra(MainActivity.EXTRA_DATA_VOLT, 0f);
        float rpm = getIntent().getFloatExtra(MainActivity.EXTRA_DATA_RPM,0f);
        float freq = getIntent().getFloatExtra(MainActivity.EXTRA_DATA_FREQ, 0f);

        displayLvl.setText(Float.toString(lvl));
        displayFuel.setText(Float.toString(fuel));
        displayAfr.setText(Float.toString(afr));
//        displayVolt.setText(Float.toString(volt));
        displayRpm.setText(Float.toString(rpm));
        displayFreq.setText(Float.toString(freq));

//        handler = new Handler(Looper.getMainLooper()){
//
//            @Override
//            public void handleMessage(Message msg) {
//
//                String statusFuel = msg.obj.toString().replace("/n","");
//                displayFuel.setText(statusFuel);
//
//                String statusAdc = msg.obj.toString().replace("/n","");
//                displayLvl.setText(statusAdc);
//
//                String statusAfr = msg.obj.toString().replace("/n","");
//                displayAfr.setText(statusAfr);
//
////                String statusVolt = msg.obj.toString().replace("/n","");
////                displayVolt.setText(statusVolt);
//
//                String statusRpm = msg.obj.toString().replace("/n","");
//                displayRpm.setText(statusRpm);
//
//                String statusFreq = msg.obj.toString().replace("/n","");
//                displayFreq.setText(statusFreq);
//                }
//            };

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

//    private void setAdapter() {
//        TripAdapter adapter = new TripAdapter(tripList);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);
//    }
//
//    private void setUserInfo() {
//        tripList.add(new Trip("Trip 1"));
//        tripList.add(new Trip("Trip 2"));
//        tripList.add(new Trip("Trip 3"));
//        tripList.add(new Trip("Trip 4"));
//        tripList.add(new Trip("Trip 5"));
//    }
}

