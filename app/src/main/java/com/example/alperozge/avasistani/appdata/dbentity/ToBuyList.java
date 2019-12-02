package com.example.alperozge.avasistani.appdata.dbentity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import androidx.databinding.library.baseAdapters.BR;

@Entity(tableName = "to_buy_lists")
public class ToBuyList extends BaseObservable implements Parcelable {

    public ToBuyList(){}

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private  int listId;


    @ColumnInfo(name = "list_name" , collate = ColumnInfo.NOCASE)
    @NonNull
    private String listName;



   @ColumnInfo(name = "to_buy_items_count" , defaultValue = "0")
    public int toBuyItemsCount;


   @Bindable
    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
        notifyPropertyChanged(BR.listId);
    }

    @Bindable
    @NonNull
    public String getListName() {
        return listName;
    }

    public void setListName(@NonNull String listName) {
        this.listName = listName;
        notifyPropertyChanged(BR.listName);
    }

    @Bindable
    @Nullable
    public Integer getToBuyItemsCount() {
        return toBuyItemsCount;
    }

    public void setToBuyItemsCount(@Nullable Integer toBuyItemsCount) {
        this.toBuyItemsCount = toBuyItemsCount;
        notifyPropertyChanged(BR.toBuyItemsCount);
    }


    /**
     * Parcelable Implementation
     * */
    public ToBuyList(Parcel in) {
        listId = in.readInt();
        listName = in.readString();
        toBuyItemsCount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(listId);
        parcel.writeString(listName);
        try {
            parcel.writeInt(toBuyItemsCount);
        } catch (Exception e) {
            parcel.writeInt(0);
        }
    }

    public static Parcelable.Creator<ToBuyList> CREATOR = new Parcelable.Creator<ToBuyList>() {
        public ToBuyList createFromParcel(Parcel in) {
            return new ToBuyList(in);
        }

        @Override
        public ToBuyList[] newArray(int i) {
            return new ToBuyList[i];
        }
    };
}
