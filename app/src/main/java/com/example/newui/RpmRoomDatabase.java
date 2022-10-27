package com.example.newui;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Rpm.class}, version = 1, exportSchema = false)
public abstract class RpmRoomDatabase extends RoomDatabase {

    public abstract RpmDao rpmDao();

    private static volatile RpmRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static RpmRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (RpmRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RpmRoomDatabase.class, "rpm_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
