package com.example.newui;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Fuel.class}, version = 1, exportSchema = false)
public abstract class FuelRoomDatabase extends RoomDatabase {

    public abstract FuelDao fuelDao();

    private static volatile FuelRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static FuelRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (FuelRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FuelRoomDatabase.class, "fuel_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    @Dao
    public static interface RpmDao {

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        void insert(Fuel fuel);

        @Query("DELETE FROM fuel_value")
        void deleteAll();

        @Query("SELECT * FROM fuel_value ORDER BY id ASC")
        LiveData<List<Fuel>> getAllFuel();
    }
}
