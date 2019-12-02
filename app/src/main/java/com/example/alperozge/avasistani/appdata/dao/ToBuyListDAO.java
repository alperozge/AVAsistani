package com.example.alperozge.avasistani.appdata.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.alperozge.avasistani.appdata.dbentity.AvailableItem;
import com.example.alperozge.avasistani.appdata.dbentity.Category;
import com.example.alperozge.avasistani.appdata.SearchItem;
import com.example.alperozge.avasistani.appdata.dbentity.ToBuyItem;
import com.example.alperozge.avasistani.appdata.dbentity.ToBuyList;
import com.example.alperozge.avasistani.appdata.repository.AvailableItemsReceiver;
import com.example.alperozge.avasistani.appdata.repository.ToBuyItemsDAO;

import java.util.List;

@Dao
public abstract class ToBuyListDAO implements ToBuyItemsDAO , AvailableItemsReceiver {

    /*
    * Methods against ItemToBuy (items_to_buy) table
    *
    * ToBuyItemsDAO implementation methods
    * */
    @Override
    @Insert
    public abstract void insertToBuyItem(ToBuyItem item);

    @Override
    @Delete
    public abstract void deleteToBuyItem(ToBuyItem item);

    @Override
    @Update(entity = ToBuyItem.class)
    public abstract void updateToBuyItem(ToBuyItem item);

    @Override
    @Query(value = "Delete FROM items_to_buy")
    public abstract void deleteAllToBuyItems();

    @Override
    @Query(value = "Select * FROM items_to_buy order by _id")
    public abstract List<ToBuyItem> getAllToBuyItems();



//    @Query(value = "Select * From items_to_buy where item_to_buy IN (Select _id from avail_items where avail_item Like :pattern)")
//    public abstract List<ToBuyItem> searchToBuyItemForItemName(String pattern);

    @Override
    @Query(value = "Select * From items_to_buy where list_no = :listNo")
    public abstract LiveData<List<ToBuyItem>> getToBuyItemsForList(int listNo);


    /*
    * Methods against AvailableItems (avail_items) table
    * */
    @Insert
    public abstract void insertAvailableItem(AvailableItem availableItem);

    @Query(value = "Select * From avail_items order by _id")
    public abstract List<AvailableItem> getAllAvailableItems();

    @Query(value = "Select * From avail_items where avail_item  Like :pattern")
    public abstract List<AvailableItem> searchAvailableItemsForItemName(String pattern);


//    @Override
//    public List<AvailableItem> searchAvailableItemsStartsWith(String pattern) {
//        return searchAvailableItemsForItemName(pattern.concat("%"));
//    }



    @Query(value = "select categories.category,avail_items.avail_item from categories inner join avail_items on categories._id = avail_items.cat_no where avail_item = :name collate nocase")
    public abstract List<SearchItem> searchAvailableItemsForAvailableItemName(String name);

    public List<SearchItem> searchAvailableItemsStartsWith(String pattern) {
        return searchAvailableItemsForAvailableItemName(pattern.concat("%"));
    }

    @Transaction
    public void insertSearchItem(SearchItem searchItem) {
        List<Integer> categoryIDList = getCategoryIDsOfName(searchItem.category);
        if(categoryIDList.size() == 0) {
            Category category = new Category();
            category.category = searchItem.category;
            insertCategory(category);
            categoryIDList = getCategoryIDsOfName(category.category);
        }
        AvailableItem availableItem = new AvailableItem();
        availableItem.categoryNo = categoryIDList.get(0);
        availableItem.availableItem = searchItem.availableItem;
        insertAvailableItem(availableItem);
    }
    /*
    * Methods against Category table
    * */
    @Insert
    public abstract void insertCategory(Category category);

    @Delete
    public abstract void deleteCategory(Category category);

    @Query(value = "Select _id from categories where category = :category")
    public abstract List<Integer> getCategoryIDsOfName(String category);




    /*
    * Methods against to_buy_lists table
    * */

    @Override
    @Query(value = "select * from to_buy_lists")
    public abstract LiveData<List<ToBuyList>> getAllToBuyLists();

    @Override
    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract long insertNewToBuyListTable(ToBuyList list);
//
//    @Override
//    @Transaction
//    public int insertNewToBuyListTable(ToBuyList list) throws DatabaseConflictException {
//        int value = insertDAONewToBuyListTable(list);
//        if(value != 1) {
//            throw new DatabaseConflictException("There is already a row exist with the ToBuyList.name or ToBuyList.id");
//        } else {
//            return value;
//        }
//    }


    @Override
    @Update(onConflict = OnConflictStrategy.ABORT)
    public abstract int updateToBuyList(ToBuyList tbl);

    @Override
    @Delete
    public abstract void dropToBuyListTable(ToBuyList list);

    @Query(value = "delete from to_buy_lists where to_buy_lists._id In (:IDList)")
    public abstract void deleteAllToBuyListsWithID(List<Integer> IDList);


}
