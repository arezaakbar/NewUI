package com.example.newui;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FuelDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Fuel fuel);

    @Query("DELETE FROM fuel_value")
    void deleteAll();

    @Query("SELECT * FROM fuel_value ORDER BY id ASC")
    LiveData<List<Fuel>> getAllFuel();
}
