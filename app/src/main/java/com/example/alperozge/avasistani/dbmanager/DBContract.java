package com.example.alperozge.avasistani.dbmanager;

public class DBContract {
    private DBContract() {}

    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_AVAIL_ITEMS = "avail_items";
    public static final String TABLE_TO_BUY = "items_to_buy";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATG = "category";
    public static final String COLUMN_CATG_NO = "cat_no"; // foreign key inside 'avail_items' table to 'avail_categories' table
    public static final String COLUMN_AVAIL_ITEM = "avail_item";
    public static final String COLUMN_ITEM_TO_BUY = "item_to_buy"; // foreign key inside 'table_to_buy' to 'avail_items'
    public static final String COLUMN_QTY_NOTE = "qty_note";
}
