package com.example.alperozge.avasistani.appdata.vievmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.alperozge.avasistani.activities.AVListActivity;
import com.example.alperozge.avasistani.activities.ActivityReferenceProvider;
import com.example.alperozge.avasistani.appdata.dbentity.ToBuyItem;
import com.example.alperozge.avasistani.appdata.dbentity.ToBuyList;
import com.example.alperozge.avasistani.appdata.repository.AppDataRepository;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AppViewModel extends AndroidViewModel {




    private MutableLiveData<Boolean> listsEmpty;
    private SavedStateHandle mState;
    private MediatorLiveData<List<ToBuyList>> mToBuyLists;

    private MediatorLiveData<List<ToBuyItem>> mToBuyItems;

    private AppDataRepository mRepository;
    private WeakReference<? extends AVListActivity> mActivityRef;

// /   private LiveData<List<ToBuyList>> toBuyLists;

    public AppViewModel(Application application , SavedStateHandle state) {
        super(application);
        mState = state;
        AppDataRepository.injectContext(application);
        mRepository = AppDataRepository.getInstance();

    }


    public LiveData<List<ToBuyList>> getToBuyLists() {
        if (null == mToBuyLists) {
            mToBuyLists = new MediatorLiveData<>();
            mToBuyLists.addSource(mRepository.getToBuyLists(),toBuyLists -> mToBuyLists.setValue(toBuyLists));
        }
        return mToBuyLists;
    }

    public LiveData<List<ToBuyItem>> getToBuyItems(ToBuyList list) {
        if(null == mToBuyItems) {
            mToBuyItems = new MediatorLiveData<>();
            mToBuyItems.addSource(mRepository.getToBuyItemsForList(list),
                                    toBuyItems -> mToBuyItems.setValue(toBuyItems));
        }
        return mToBuyItems;
    }
//    public void insertToBuyListItem(ToBuyList list) throws DatabaseConflictException {
//        mRepository.insertNewToBuyList(list);
//    }

    public <T extends AVListActivity> void  insertToBuyListItem(ToBuyList list, Method onInsertFail  ,Object...args) {

        mRepository.insertNewToBuyList(list , new ActivityReferenceProvider<T>() {
            @Override
            public WeakReference<T> getReference() {
                return (WeakReference<T>) mActivityRef;
            }}, onInsertFail  ,args);
    }

    public <T extends AVListActivity> void updateToBuyListItem(ToBuyList list, Method onInsertFail  ,Object...args) {
        mRepository.updateToBuyList(list,new ActivityReferenceProvider<T>() {
            @Override
            public WeakReference<T> getReference() {
                return (WeakReference<T>) mActivityRef;
            }}, onInsertFail  ,args);
    }

    public void deleteAllToBuyListsWithID(ArrayList<Integer> list) {
        mRepository.deleteAllToBuyListsWithID(list);
    }

    public void setActivityReference (AVListActivity activity) {
        mActivityRef = new WeakReference<>(activity);
    }


}
