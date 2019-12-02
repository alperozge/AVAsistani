package com.example.alperozge.avasistani.dbmanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.alperozge.avasistani.R;
import com.example.alperozge.avasistani.appdata.dbentity.AvailableItem;
import com.example.alperozge.avasistani.appdata.dbentity.Category;
import com.example.alperozge.avasistani.appdata.dbentity.ToBuyItem;
import com.example.alperozge.avasistani.appdata.dbentity.ToBuyList;
import com.example.alperozge.avasistani.appdata.dao.ToBuyListDAO;

@Database(entities = {AvailableItem.class, Category.class, ToBuyItem.class , ToBuyList.class}, version = 1 )
public abstract class ShoppingDB extends RoomDatabase {

    public static final Migration MIGRATION1_2 = getMigration1To2();

    private static volatile ShoppingDB INSTANCE;
    public abstract ToBuyListDAO dao();

    public static  ShoppingDB getInstance(final Context context) {
        if(INSTANCE == null) {
            synchronized (ShoppingDB.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ShoppingDB.class, context.getApplicationContext().getString(R.string.db_name))
//                        .addMigrations(MIGRATION1_2)
                        .build();


//             INSTANCE.getQueryExecutor().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        Cursor cursor = INSTANCE.query("select * from avail_items",null);
//                        Log.d("ShoppingDB", "run: query returns size for 'avail+items': " + cursor.getCount());
//                    }
//                });
            }
        }
        return INSTANCE;
    }

    private static Migration getMigration1To2() {
        return new Migration(1,2) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase database) {

            }
        };
    }

}
