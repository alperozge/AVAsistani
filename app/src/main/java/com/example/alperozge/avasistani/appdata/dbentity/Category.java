package com.example.alperozge.avasistani.appdata.dbentity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public int id;


    @ColumnInfo(name = "category")
    @NotNull
    public String category;
}
