package com.example.newui;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class FuelRepository {

    private FuelDao mFuelDao;
    private LiveData<List<Fuel>>mAllFuels;

    FuelRepository(Application application) {
        FuelRoomDatabase db = FuelRoomDatabase.getDatabase(application);
        mFuelDao = db.fuelDao();
        mAllFuels = mFuelDao.getAllFuel();
    }

    LiveData<List<Fuel>> getmAllFuels(){
        return mAllFuels;
    }

    void insert (Fuel fuel) {
        FuelRoomDatabase.databaseWriteExecutor.execute(() -> {
            mFuelDao.insert(fuel);
        });
    }
}
