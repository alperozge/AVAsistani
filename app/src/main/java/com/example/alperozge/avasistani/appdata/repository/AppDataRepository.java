package com.example.alperozge.avasistani.appdata.repository;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import androidx.arch.core.util.Function;
import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.alperozge.avasistani.activities.ActivityReferenceProvider;
import com.example.alperozge.avasistani.appdata.dbentity.ToBuyItem;
import com.example.alperozge.avasistani.appdata.dbentity.ToBuyList;
import com.example.alperozge.avasistani.dbmanager.ShoppingDB;
import com.example.alperozge.avasistani.exceptions.DatabaseConflictException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class AppDataRepository {



    private static AppDataRepository INSTANCE;
    private static Context mAppContext;
    private AvailableItemsReceiver mAvailableItemsReceiver;
    private ToBuyItemsDAO mToBuyItemsDAO;

    MediatorLiveData<List<ToBuyList>> mToBuyLists;
    MediatorLiveData<List<ToBuyItem>> mToBuyItemsOfList;

    public static  AppDataRepository getInstance() {
        if(INSTANCE == null) {
            synchronized (AppDataRepository.class) {
                INSTANCE = new AppDataRepository();
                INSTANCE.initializeDataAccess(mAppContext);
            }
        }
        return INSTANCE;
    }

    public static void injectContext(Context context) {
        if(null == mAppContext) {
            mAppContext = context.getApplicationContext();
        }
    }

    private AppDataRepository() {

    }

    //TODO: THIS METHOD is for trial phase only and should be removed when dependency injection is implemented
    private void initializeDataAccess(Context context) {
        ShoppingDB db = ShoppingDB.getInstance(context);
        mAvailableItemsReceiver = db.dao();
        mToBuyItemsDAO = db.dao();
    }


    /*Public Methods:*/

    public LiveData<List<ToBuyList>> getToBuyLists() {
        if(mToBuyLists == null) {
            mToBuyLists = new MediatorLiveData<>();
            mToBuyLists.setValue(null);
            mToBuyLists.addSource(mToBuyItemsDAO.getAllToBuyLists(),mToBuyLists::postValue);
        }
        return mToBuyLists;
    }

    public void insertNewToBuyList(ToBuyList list) throws DatabaseConflictException {
        final Wrapper<Long> intWrapper = new Wrapper<>();
        Consumer<Long> cons = intWrapper::setValue;
//        new ToBuyItemsDAOOperator<ToBuyList,Long>(toBuyList->mToBuyItemsDAO.insertNewToBuyListTable(toBuyList),cons).execute(list);
//
//
//
//        if(intWrapper.getValue() != null && intWrapper.getValue() == -1) {
//            throw new DatabaseConflictException("There is already a row in database with conflicting input ToBuyList");
//        }

    }

    public void deleteAllToBuyListsWithID(List<Integer> list) {
        new ToBuyItemsDAOConsumer<List<Integer>>(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) {
                mToBuyItemsDAO.deleteAllToBuyListsWithID(integers);
            }
        }).execute(list);
    }

    public <T extends Activity> void insertNewToBuyList(ToBuyList list, ActivityReferenceProvider<T> objectRef, Method m, Object... args) {
        new ToBuyItemsDAOOperator<ToBuyList,Long,T>((ToBuyList l) -> {
            return mToBuyItemsDAO.insertNewToBuyListTable(l);
        },
                objectRef,m ,args).execute(list);
    }



    public <T extends Activity>  void updateToBuyList(ToBuyList list, ActivityReferenceProvider<T> objectRef, Method onUpdateFail, Object... methodArgs) {
        new ToBuyItemsDAOOperator<>((ToBuyList l) -> {
            return mToBuyItemsDAO.updateToBuyList(l);
        },
                objectRef,onUpdateFail,methodArgs).execute(list);
//        new ToBuyItemsDAOConsumer<ToBuyList>(tbl->mToBuyItemsDAO.updateToBuyList(tbl)).execute(updatedToBuyList);
    }

    public  LiveData<List<ToBuyItem>> getToBuyItemsForList(ToBuyList list) {
        if(null == mToBuyItemsOfList) {
            mToBuyItemsOfList = new MediatorLiveData<>();
            mToBuyItemsOfList.addSource(mToBuyItemsDAO.getToBuyItemsForList(list.getListId()),mToBuyItemsOfList::postValue);
        }
        return mToBuyItemsOfList;
    }


    private static class ToBuyItemsDAOConsumer<T> extends AsyncTask<T,Void,Void> {

        Consumer<T> mConsumer;

        ToBuyItemsDAOConsumer(Consumer<T> consumer) {
            mConsumer = consumer;
        }

        @Override
        protected Void doInBackground(T... ts) {
            if (null != ts && ts.length > 0) {
                mConsumer.accept(ts[0]);
            }
            return null;
        }
    }

    private static class ToBuyItemsDAOOperator<T,U,V extends Activity> extends AsyncTask<T,Void,U> {
        Function<T,U> mFunction;
        ActivityReferenceProvider<V> mRefProvider;
        Method mMethod;

        Object[] mArgs;
        boolean mExcepted = false;

        public ToBuyItemsDAOOperator(Function<T, U> function, ActivityReferenceProvider<V> objectRef , Method onUpdateFail ,Object...methodsArgs) {
            mFunction = function;
            mRefProvider = objectRef;
            mMethod = onUpdateFail;
            mArgs = methodsArgs;
        }

        @Override
        protected U doInBackground(T... ts) {
            U u = null;


            if (null != ts && ts.length > 0) {
                //TODO: Remove following try-catch and wrapped code


                try {
                    u = mFunction.apply(ts[0]);
                } catch (SQLiteConstraintException e) {
                    mExcepted = true;
                }
            }


//            Log.d("AppDataRepository", "doInBackground: u = " + u);
            return u;
        }

        @Override
        protected void onPostExecute(U u) {
//            if(null != u && Long.valueOf(-1).equals(u) && null != mRefProvider.getReference().get()) {
              if(null == u && null !=mRefProvider.getReference().get() && mExcepted) {
                try {
                    mMethod.invoke(mRefProvider.getReference().get(),mArgs);
                } catch (IllegalAccessException e) {

                } catch (InvocationTargetException e) {

                }
            }
        }
    }

    private static class  ToBuyItemsDAOReceiver<U,V> extends AsyncTask<U,Void,LiveData<List<V>>> {

        interface SyncCallback<T> {
            void callBack(T t);
        }
        private Function<U,LiveData<List<V>>> mFunction;
        private SyncCallback<LiveData<List<V>>> mCallback;


        public ToBuyItemsDAOReceiver (Function<U,LiveData<List<V>>> func , SyncCallback<LiveData<List<V>>> callback ) {
            mFunction = func;
            mCallback = callback;
        }

        @Override
        protected LiveData<List<V>> doInBackground(U... us) {
            return mFunction.apply(us.length > 0 ?  us[0] : null);
        }
        @Override
        protected void onPostExecute(LiveData<List<V>> vs) {
            mCallback.callBack(vs);
        }


    }

    private static class Wrapper<T> {
        T t;

        public Wrapper(T t) {
            this.t = t;
        }

        public Wrapper() {
        }

        public T getValue() {
            return t;
        }

        public void setValue(T t) {
            this.t = t;
        }
    }
}
