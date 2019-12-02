package com.example.alperozge.avasistani.activities;

import android.content.DialogInterface;
import android.content.Intent;

import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Typeface;
import android.os.Bundle;


import android.os.Parcelable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;


import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import 	androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProviders;

import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;

import androidx.recyclerview.selection.StorageStrategy;

import androidx.recyclerview.widget.RecyclerView;

import com.example.alperozge.avasistani.R;
import com.example.alperozge.avasistani.appdata.dbentity.ToBuyList;
import com.example.alperozge.avasistani.appdata.vievmodel.AppViewModel;
import com.example.alperozge.avasistani.appdata.vievmodel.OnAvailableListsClicked;



import com.example.alperozge.avasistani.databinding.ContentAlllistsBinding;
import com.example.alperozge.avasistani.selection.CustomStableIdProvider;
import com.example.alperozge.avasistani.selection.ListsItemDetailsLookup;
import com.example.alperozge.avasistani.ui.MargineItemDecoration;
import com.example.alperozge.avasistani.viewadapters.ToBuyListsAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AllListsActivity extends AVListActivity<ContentAlllistsBinding> {

    private final static String TO_BUY_LIST_ID = "TO_BUY_LIST_ID";
    private AppViewModel mViewModel;

//    private ContentAlllistsBinding mBinding;

    private RecyclerView mRecyclerView;
    private ToBuyListsAdapter mAdapter;

    private SelectionTracker<Long> mTracker;


    private OnAvailableListsClicked onClick = new OnAvailableListsClicked() {
        @Override
        public void callback(ToBuyList list) {
            Intent newActivityIntent = new Intent(AllListsActivity.this,ToBuyListActivity.class);
            newActivityIntent.putExtra(TO_BUY_LIST_ID, list.getListId());
//            AllListsActivity.this.startActivity(newActivityIntent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this, new SavedStateViewModelFactory(getApplication(),this,savedInstanceState)).get(AppViewModel.class);



//        mBinding = DataBindingUtil.setContentView(this, R.layout.content_alllists);



        mRecyclerView = findViewById(R.id.listsRecyclerView);
        mAdapter = new ToBuyListsAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new MargineItemDecoration(8));
        mAdapter.setOnAvailableListClicked(onClick);
        mTracker = new SelectionTracker.Builder<>("to_buy_lists_selection", //Simply pass an unique id here, we do not need to keep the reference so no need for a constant or a resource
                                                                    mRecyclerView,
                                                                    new CustomStableIdProvider(mRecyclerView),
                                                                    new ListsItemDetailsLookup(mRecyclerView),
                                                                     StorageStrategy.createLongStorage())
                                                                .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                                                                .withOperationMonitor(ActivityUtil.getOperationMonitorForActivity(this,new ActionModeCallback(this)))
                                                                .build();
        mTracker.onRestoreInstanceState(savedInstanceState);
        mAdapter.setSelectionTracker(mTracker);

        initiateBindings();


        mTracker.addObserver(mSelectionObserver);



//        SpannableString spannedString = new SpannableString(" Spanned");
//        ForegroundColorSpan fcs = new ForegroundColorSpan(255);
//        spannedString.setSpan(fcs,0,spannedString.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        SpannableStringBuilder sb = new SpannableStringBuilder("There is RED text");
//        sb.insert(sb.length(),spannedString);
//
//        Log.d("TAG", "useAppContext: " + (sb.getSpans(0,sb.length(),ForegroundColorSpan.class)[0]));
//        Log.d("TAG", "onCreate: " + sb.getSpanStart(fcs));
//        Log.d("TAG", "onCreate: " + sb);

    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mTracker.onSaveInstanceState(outState);

//        Parcelable recylerViewState = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).las.onSaveInstanceState();
//        outState.putParcelable("recViewState",recylerViewState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_add:
                createAddListFragment(null);
                break;
            case R.id.action_settings:
                // TODO: code for hadling settings - and delete from the menu if no settings will be supplied
                break;

        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.content_alllists;
    }

    @Override
    protected View.OnClickListener getOnFABClick() {
        return view -> createAddListFragment(null);
    }

    @Override
    protected void onDestroyActionMode() {
        mTracker.clearSelection();
    }

    @Override
    protected void onActionEdit() {
        int size = mTracker.getSelection().size();
        if(size != 1) {throw new RuntimeException("This selection should not have been possible");}
        Iterator<Long> iterator = mTracker.getSelection().iterator();
        while (iterator.hasNext()) {
            Long id = iterator.next();
            for(ToBuyList ls : mViewModel.getToBuyLists().getValue()) {
                if((long)ls.getListId() == id) {
                    createEditListNameFragment(ls);
                }
            }
        }
    }

    @Override
    protected void onActionDelete() {


        ArrayList<Integer> list = new ArrayList<>(mTracker.getSelection().size());

        Iterator<Long> iterator = mTracker.getSelection().iterator();
        while (iterator.hasNext()) {
            Long longValue = iterator.next();
//            Log.d("AllListsActivity" ,"onActionDelete: adding item to list: " + longValue.intValue());
            int value = longValue.intValue();
            list.add(value);
        }

        mTracker.clearSelection(); // finish active selections first as per documentation says that data of recyclerview shouldn't be altered while there are active selections.
        //now delete items
        mViewModel.deleteAllToBuyListsWithID(list);
    }

    @Override
    protected void onActionSelectAll() {
        List<ToBuyList> lists = mViewModel.getToBuyLists().getValue();
        List<Long> IDs = new ArrayList<>();
        for(ToBuyList l : lists) {
            IDs.add((long)(l.getListId()));
        }
        mTracker.setItemsSelected(IDs,true);
    }

    /**
     * EditableItemsActivity implementation
     * */
    @Override
    public void onAddReturnPositive(String fragmentFeedback , Parcelable tag) {
        ToBuyList list = new ToBuyList();
        list.setListName(fragmentFeedback);
        try {
            Method mt = AVListActivity.class.getDeclaredMethod("showAlertAndClose",String.class,String.class, CharSequence[].class);
            mt.setAccessible(true);
//            showAlertAndClose();
            SpannableString spannableString = new SpannableString(list.getListName());
            spannableString.setSpan(new StyleSpan(Typeface.BOLD),0,list.getListName().length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,R.color.colorAccent)),0,list.getListName().length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new UnderlineSpan(),0,list.getListName().length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new AbsoluteSizeSpan(24,true),0,list.getListName().length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new StyleSpan(R.style.TextAppearance_AppCompat_Caption),0, list.listName.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            SpannableString[] spannableStrings = new SpannableString[] {spannableString};
            mViewModel.insertToBuyListItem(list,mt,null,getString(R.string.already_present_tobuylist),spannableStrings);
//            mViewModel.insertToBuyListItem(list,mt,getString(R.string.already_present_tobuylist));
        } catch (SQLiteConstraintException  e ) {
            showCrashAlertAndClose("SQL hatasi\n" + e.getMessage());
        } catch (NoSuchMethodException e) {
            showCrashAlertAndClose("Metot hatasi\n" + e.getMessage());
        }
    }

    @Override
    public void onEditReturnPositive(String fragmentFeedback , Parcelable tag) {
        ToBuyList list = (ToBuyList)tag;
        list.setListName(fragmentFeedback);
        try {
            Method mt = AVListActivity.class.getDeclaredMethod("showAlertAndClose",String.class,String.class, CharSequence[].class);
            mt.setAccessible(true);
//            showAlertAndClose();
            SpannableString spannableString = new SpannableString(list.getListName());
            spannableString.setSpan(new StyleSpan(Typeface.BOLD),0,list.getListName().length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,R.color.colorAccent)),0,list.getListName().length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new UnderlineSpan(),0,list.getListName().length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new AbsoluteSizeSpan(24,true),0,list.getListName().length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new StyleSpan(R.style.TextAppearance_AppCompat_Caption),0, list.listName.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            SpannableString[] spannableStrings = new SpannableString[] {spannableString};

            mViewModel.updateToBuyListItem(list,mt,null,getString(R.string.already_present_tobuylist),spannableStrings);
//            mViewModel.insertToBuyListItem(list,mt,getString(R.string.already_present_tobuylist));
        } catch (SQLiteConstraintException  e ) {
            showCrashAlertAndClose("SQL hatasi\n" + e.getMessage());
        } catch (NoSuchMethodException e) {
            showCrashAlertAndClose("Metot hatasi\n" + e.getMessage());
        }
    }

    @Override
    public void onReturnNegative() {

    }

    /**
     * SimpleAlertDisplayerActivity implementation
     * */
    @Override
    public void onAlertReturnPositive(CharSequence[] inputs) {
        ToBuyList list = new ToBuyList();
        list.setListName(inputs[0].toString());
        createEditListNameFragment(list);
    }

    @Override
    public void onAlertReturnNegative() {

    }

    /*
    * Private Methods
    * */

    private void initiateBindings() {
        mViewModel.setActivityReference(this);
        mViewModel.getToBuyLists().observe(this, toBuyList -> {
            mBinding.setIsEmpty(null == toBuyList ||toBuyList.size() < 1);
            mAdapter.setToBuyLists(toBuyList);
        });

    }

    private void showCrashAlertAndClose(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setMessage(message)
                .setPositiveButton(getString(R.string.alert_dialog_OK_button_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .show();
    }



    private void createAddListFragment(String listName) {
        AddListDialogFragment fragment = AddListDialogFragment.newInstance(listName,AddListDialogFragment.OperationMode.ADD);
        fragment.show(getSupportFragmentManager() , getString(R.string.add_list_fragment_tag));
    }

    private void createEditListNameFragment (ToBuyList oldListObject) {
        AddListDialogFragment fragment = AddListDialogFragment.newInstance(oldListObject.getListName(), AddListDialogFragment.OperationMode.EDIT , oldListObject);
        fragment.show(getSupportFragmentManager(),getString(R.string.edit_listname_fragment_tag));
    }

    private final SelectionTracker.SelectionObserver mSelectionObserver = new SelectionTracker.SelectionObserver() {
        @Override
        public void onSelectionChanged() {
            int selectionSize = mTracker.getSelection().size();
            switch (selectionSize) {
                case 0:
                    mActionMode.finish();
                    break;
                case 1:
                    if(null != mActionMode) {
                        MenuItem item = mActionMode.getMenu().findItem(R.id.action_edit);
                        item.setEnabled(true);
                        item.setVisible(true);
                    }
                    break;
                default:
                    if(null != mActionMode) {
                        MenuItem item = mActionMode.getMenu().findItem(R.id.action_edit);
                        item.setEnabled(false);
                        item.setVisible(false);
                    }
                    break;
            }
        }
    };


}
