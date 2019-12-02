package com.example.alperozge.avasistani.appdata.repository;

import com.example.alperozge.avasistani.appdata.dbentity.AvailableItem;
import com.example.alperozge.avasistani.appdata.SearchItem;

import java.util.List;

/**
 * Interface for querying available items. This interface is created such that the available items
 * migth be retrieved from remote server as well as local database.
 * */
public interface AvailableItemsReceiver {
    List<AvailableItem> getAllAvailableItems();


    /**
     * Searches AvailableItem source (local database / probable remote server) for the
     * {@code pattern} parameter. Returns the {@code AvailableItem} list whose members
     * contain available item names that starts with the input string
     * */
    List<SearchItem> searchAvailableItemsStartsWith(String pattern);


    void insertSearchItem(SearchItem searchItem);

}
