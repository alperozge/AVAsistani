package com.example.alperozge.avasistani.appdata.repository;

import androidx.lifecycle.LiveData;

import com.example.alperozge.avasistani.appdata.dbentity.ToBuyItem;
import com.example.alperozge.avasistani.appdata.dbentity.ToBuyList;

import java.util.List;

/**
 * This interface defines methods related to ToBuyItems (Alisveris Listesi)
 * such as getting 'ToBuyItem's , searching available items , adding an available
 * item to ToBuyItems table, deleting from ToBuyItem's table etc.
 * */
public interface ToBuyItemsDAO {


    void insertToBuyItem(ToBuyItem item);

    void updateToBuyItem(ToBuyItem item);

    void deleteToBuyItem(ToBuyItem item);

    void deleteAllToBuyItems();

    List<ToBuyItem> getAllToBuyItems();

    // retrieve 'ToBuyItem's based on list_no
    LiveData<List<ToBuyItem>> getToBuyItemsForList(int listNo);

    void dropToBuyListTable(ToBuyList toBuyList);
//    List<ToBuyItem> searchToBuyItemForItemName(String pattern);

    long insertNewToBuyListTable(ToBuyList list);

    LiveData<List<ToBuyList>> getAllToBuyLists();

    void deleteAllToBuyListsWithID(List<Integer> integers);


    int updateToBuyList(ToBuyList tbl);
}
