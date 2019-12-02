package com.example.alperozge.avasistani.appdata.dbentity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "avail_items" , foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "_id",
        childColumns = "cat_no",onDelete = ForeignKey.SET_DEFAULT))
public class AvailableItem {
    @PrimaryKey
    @ColumnInfo(name = "_id")
    public int id;

    @ColumnInfo(name = "cat_no",defaultValue = "1")
    public int categoryNo;

    @NotNull
    @ColumnInfo(name = "avail_item", collate = ColumnInfo.NOCASE)
    public String availableItem;
}
