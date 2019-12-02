package com.example.alperozge.avasistani;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import 	androidx.test.core.app.ApplicationProvider;
import androidx.room.Room;
import androidx.test.runner.AndroidJUnit4;

import com.example.alperozge.avasistani.appdata.dbentity.AvailableItem;
import com.example.alperozge.avasistani.appdata.dbentity.Category;
import com.example.alperozge.avasistani.appdata.dbentity.ToBuyItem;
import com.example.alperozge.avasistani.appdata.dbentity.ToBuyList;
import com.example.alperozge.avasistani.dbmanager.ShoppingDB;
import com.example.alperozge.avasistani.appdata.dao.ToBuyListDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;

@RunWith(AndroidJUnit4.class)
public class DatabaseAccessTest {
    private ToBuyListDAO dao;
    private ShoppingDB db;

    @Before
    public void createDB() {
        Context context = ApplicationProvider.getApplicationContext();

        db = Room.inMemoryDatabaseBuilder(context, ShoppingDB.class).build();
        dao = db.dao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {


        Category category = new Category();
        category.category = "Market";
        dao.insertCategory(category);





        AvailableItem avItem = new AvailableItem();
        avItem.categoryNo = 1;
        avItem.availableItem = "Ekmek";
        dao.insertAvailableItem(avItem);

        avItem = new AvailableItem();
        avItem.categoryNo = 1;
        avItem.availableItem = "Sıvı Yağ";
        Log.d("DatabaseAccessTest", "writeUserAndReadInList: availableItem id: " + avItem.id);
//        avItem.id = 2;
//        dao.insertAvailableItem(avItem);

        ToBuyList toBuyList = new ToBuyList();
        toBuyList.setListName("Market");
        dao.insertNewToBuyListTable(toBuyList);

        toBuyList = new ToBuyList();
        toBuyList.setListName("Baska bir liste");
        dao.insertNewToBuyListTable(toBuyList);

        ToBuyItem item = new ToBuyItem();
        item.item = 2;
        item.listNo = 1;
        item.qtyNote = "3 adet";
        dao.insertToBuyItem(item);
        List<AvailableItem> byName = dao.searchAvailableItemsForItemName("%Sıvı%");
        item.id = 1;
//        List<ToBuyItem> byName = dao.selectAllToBuyItems();
       ;
    }

    @Test
    public void getToBuyListAndItems() throws Exception {
        ToBuyItem item = new ToBuyItem();
        item.id = 1;
        item.listNo = 1;
        item.item = 3;
        item.qtyNote = "3 adet";


        LiveData<List<ToBuyItem>> toBuyItems = dao.getToBuyItemsForList(1);
        assert item.equals(toBuyItems.getValue().get(0));
    }
}
