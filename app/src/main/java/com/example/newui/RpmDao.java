package com.example.newui;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RpmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Rpm rpm);

    @Query("DELETE FROM rpm_value")
    void deleteAll();

    @Query("SELECT * FROM rpm_value ORDER BY id ASC")
    LiveData<List<Rpm>> getAllRpm();
}
