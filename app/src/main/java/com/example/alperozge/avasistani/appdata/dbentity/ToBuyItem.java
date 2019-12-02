package com.example.alperozge.avasistani.appdata.dbentity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.core.os.ParcelCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import org.jetbrains.annotations.NotNull;

import java.util.Objects;


@Entity(tableName = "items_to_buy" , foreignKeys = @ForeignKey(entity = ToBuyList.class,
        parentColumns = "_id",
        childColumns = "list_no" , onDelete = ForeignKey.CASCADE))
public class ToBuyItem extends BaseObservable implements Parcelable {

    public ToBuyItem() {}

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;

    @ColumnInfo(name = "list_no" , defaultValue = "1")
    @Nullable
    private Integer listNo;


    @NotNull
    @ColumnInfo(name = "item_to_buy")
    private int item;


    @ColumnInfo(name = "qty_note")
    @Nullable
    private String qtyNote;

    @Ignore
    public boolean checked = false;


    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    @Nullable
    public Integer getListNo() {
        return listNo;
    }

    public void setListNo(@Nullable Integer listNo) {
        if (this.listNo != listNo) {
            this.listNo = listNo;
            notifyPropertyChanged(BR.listNo);
        }
    }

    @Bindable
    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        if (this.item != item) {
            this.item = item;
            notifyPropertyChanged(BR.item);
        }
    }

    @Bindable
    public String getQtyNote() {
        return qtyNote;
    }

    public void setQtyNote(@Nullable String qtyNote) {
        if (Objects.equals(this.qtyNote,qtyNote)) {
            this.qtyNote = qtyNote;
            notifyPropertyChanged(BR.qtyNote);
        }
    }

    @Bindable
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;
            notifyPropertyChanged(BR.checked);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToBuyItem toBuyItem = (ToBuyItem) o;
        return id == toBuyItem.id &&
                item == toBuyItem.item &&
                Objects.equals(qtyNote, toBuyItem.qtyNote);
    }

    @Override
    public int hashCode() {
        return id * id + item * item * 41 + qtyNote.hashCode();
    }


    /**
     * Parcelable Implementation
     * */
    public ToBuyItem(Parcel in) {
        id = in.readInt();
        item = in.readInt();
        qtyNote = in.readString();
        checked = ParcelCompat.readBoolean(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(item);
        parcel.writeString(qtyNote);
        ParcelCompat.writeBoolean(parcel , checked);
    }

    public static Parcelable.Creator<ToBuyItem> CREATOR = new Parcelable.Creator<ToBuyItem>() {
        public ToBuyItem createFromParcel(Parcel in) {
            return new ToBuyItem(in);
        }

        @Override
        public ToBuyItem[] newArray(int i) {
            return new ToBuyItem[i];
        }
    };
}
