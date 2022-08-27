package com.example.newui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationMenu = findViewById(R.id.bottomNavigationMenu);
        bottomNavigationMenu.setSelectedItemId(R.id.menumode);

        bottomNavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menumode:
                        return true;
                    case R.id.menuhistory:
                        startActivity(new Intent(getApplicationContext(),TripHistory.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }
}