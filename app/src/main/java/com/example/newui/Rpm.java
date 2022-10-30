package com.example.newui;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "rpm_value")
public class Rpm {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private Integer mId;

    public Integer getId() {
        return this.mId;
    }

    @ColumnInfo(name = "int_rpm")
    private Float mRpm;

    public Rpm(@NonNull int id, @NonNull float rpm) {
        this.mId = id;
        this.mRpm = rpm;
    }

    public Float getRpm() {
        return this.mRpm;
    }

}
