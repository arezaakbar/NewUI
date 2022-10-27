package com.example.newui;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class RpmRepository {

    private RpmDao mRpmDao;
    private LiveData<List<Rpm>>mAllRpms;

    RpmRepository(Application application) {
        RpmRoomDatabase db = RpmRoomDatabase.getDatabase(application);
        mRpmDao = db.rpmDao();
        mAllRpms = mRpmDao.getAllRpm();
    }

    LiveData<List<Rpm>> getmAllRpms(){
        return mAllRpms;
    }

    void insert (Rpm rpm) {
        RpmRoomDatabase.databaseWriteExecutor.execute(() -> {
            mRpmDao.insert(rpm);
        });
    }
}
