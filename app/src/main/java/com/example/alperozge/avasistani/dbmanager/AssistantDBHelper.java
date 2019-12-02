package com.example.alperozge.avasistani.dbmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.alperozge.avasistani.R;

import java.util.Locale;

/*
* Table 'Categories'
*  _______ __________________
* |  _id  |  category (text) |
*  --------------------------
*
*
*  Table 'avail_items'
*   ______________________________________________________
*  |  _id  | avail_item(text) |  cat_no (FK Categries._id)|
*   ------------------------------------------------------
*
*
*  Table 'items_to_buy'
*   __________________________________________________
*  |  item_to_buy (FK avail_items._id)  |  qty_note   |
*   --------------------------------------------------
*
* */

public class AssistantDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
//    public static final String DATABASE_NAME = "AVAssistantDB.db";

    public static final String CREATE_IF_NOT_EX = "CREATE TABLE IF NOT EXISTS ";
    public static final String DROP_IF_EX = "DROP TABLE IF EXISTS ";

    public static final String CREATE_TABLE_CATEGORIES =
            CREATE_IF_NOT_EX + DBContract.TABLE_CATEGORIES + " (" +
            DBContract.COLUMN_ID + " INTEGER PRIMARY KEY," +
            DBContract.COLUMN_CATG + " TEXT UNIQUE)";

    public static final String DROP_TABLE_CATEGORIES =
            DROP_IF_EX + DBContract.TABLE_CATEGORIES;

    public static final String CREATE_TABLE_AVAIL_ITEMS =
            CREATE_IF_NOT_EX + DBContract.TABLE_AVAIL_ITEMS + " (" +
            DBContract.COLUMN_ID + " INTEGER PRIMARY KEY," +
            DBContract.COLUMN_AVAIL_ITEM + " TEXT, " +
            DBContract.COLUMN_CATG_NO + " INTEGER, " +
            getFKeyString(DBContract.COLUMN_CATG_NO,DBContract.TABLE_CATEGORIES,DBContract.COLUMN_ID) +
            ")";

    public static final String DROP_TABLE_AVAIL_ITEMS =
            DROP_IF_EX + DBContract.TABLE_AVAIL_ITEMS;

    public static final String CREATE_TABLE_ITEMS_TO_BUY =
            CREATE_IF_NOT_EX + DBContract.TABLE_TO_BUY + " (" +
            DBContract.COLUMN_ITEM_TO_BUY + " INTEGER," +
            DBContract.COLUMN_QTY_NOTE + " TEXT, " +
            getFKeyString(DBContract.COLUMN_ITEM_TO_BUY,DBContract.TABLE_AVAIL_ITEMS,DBContract.COLUMN_ID);

    public static final String DROP_TABLE_TO_BUY =
            DROP_IF_EX + DBContract.TABLE_TO_BUY;

    public AssistantDBHelper(Context context) {
        super(context, context.getString(R.string.db_name), null, DATABASE_VERSION);
    }



    /*
    * Method to create foreign key constraint string.
    * Ex:
  CREATE TABLE track(
     trackid     INTEGER,
     trackname   TEXT,
     trackartist INTEGER,

     FOREIGN KEY(trackartist) REFERENCES artist(artistid)
                 child key/column     parent table(parent key/column)
   );
    * */
    public static String getFKeyString(String childKey , String parentTable , String parentKey) {
        StringBuilder sb = new StringBuilder();
        sb.append("FOREIGN KEY (");
        sb.append(childKey);
        sb.append(") REFERENCES ");
        sb.append(parentTable);
        sb.append("(");
        sb.append(parentKey);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        db.execSQL(CREATE_TABLE_AVAIL_ITEMS);
//        db.execSQL(CREATE_TABLE_CATEGORIES);
//        db.execSQL(CREATE_TABLE_ITEMS_TO_BUY);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
        db.setLocale(new Locale("tr","TR"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
