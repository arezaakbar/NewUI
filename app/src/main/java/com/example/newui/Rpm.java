package com.example.newui;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rpm_value")
public class Rpm {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private Integer mId;
    public Rpm(@NonNull int id){this.mId = id;}
    public Integer getId(){return this.mId;}

    @ColumnInfo(name = "int_fuel")
    private Float mFuel;
    public Rpm(@NonNull float fuel){this.mFuel=fuel;}
    public Float getFuel(){return this.mFuel;}

}
