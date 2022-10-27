package com.example.newui;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fuel_value")
public class Fuel {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private Integer mId;
    public Fuel(@NonNull int id){this.mId = id;}
    public Integer getId(){return this.mId;}

    @ColumnInfo(name = "int_fuel")
    private Float mFuel;
    public Fuel(@NonNull float fuel){this.mFuel=fuel;}
    public Float getFuel(){return this.mFuel;}

}
