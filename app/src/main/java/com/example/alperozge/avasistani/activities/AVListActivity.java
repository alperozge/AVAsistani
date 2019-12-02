package com.example.alperozge.avasistani.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.example.alperozge.avasistani.R;
import com.example.alperozge.avasistani.databinding.ActivityAVListBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public abstract class AVListActivity<T extends ViewDataBinding> extends AppCompatActivity
                                                                implements EditableItemsActivity , SimpleAlertDisplayerActivity{
    private boolean isActionModeStarted = false;
    private ActivityAVListBinding mAVListBinding;
    protected T mBinding;
    ActionMode mActionMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAVListBinding = DataBindingUtil.setContentView(this, R.layout.activity_a_v_list);
        FrameLayout fl = findViewById(R.id.content_panel);
//        getLayoutInflater().inflate(getContentLayoutID(),fl,false);
        View contentView =  LayoutInflater.from(this).inflate(getContentLayoutID(),null,false);
        fl.addView(contentView);
//        fl.addView(LayoutInflater.from(this).inflate(getContentLayoutID(),fl,false));
        mBinding = DataBindingUtil.bind(contentView);
//        mAVListBinding.contentPanel.addView(getLayoutInflater().inflate(getContentLayoutID(),(ViewGroup) mAVListBinding.getRoot(),false));


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = mAVListBinding.fab;
        fab.setOnClickListener(getOnFABClick());

    }

    @Nullable
    @Override
    public ActionMode startSupportActionMode(@NonNull ActionMode.Callback callback) {
        mActionMode = super.startSupportActionMode(callback);
        return mActionMode;
    }

    private void nullifyActionMode() {
        mActionMode = null;
    }
    /**
     * Return the layout ID for activity content layout; ie {@code R.layout.<layout_name>}
     * */
    protected abstract int getContentLayoutID();

    protected abstract View.OnClickListener getOnFABClick();

    protected abstract void onDestroyActionMode();

    protected abstract void onActionEdit();

    protected abstract void onActionDelete();

    protected abstract void onActionSelectAll();




    protected final boolean isActionMode() {
        return null != mActionMode;
    }




    protected static class ActionModeCallback implements ActionMode.Callback {
        AVListActivity mActivity;
        ActionModeCallback(AVListActivity activity) {
            mActivity = activity;
        }
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            if(mActivity.isActionMode()) {
                return false;
            }
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.menu_main_context, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit:
                    mActivity.onActionEdit();
                    actionMode.finish();
                    return true;

                case R.id.action_delete:
                    mActivity.onActionDelete();
                    actionMode.finish();
                    return true;

                case R.id.action_select_all:
                    mActivity.onActionSelectAll();
//                    actionMode.finish();
                    return true;

                default:
                    return false;

            }

        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActivity.onDestroyActionMode();
            mActivity.nullifyActionMode();

        }
    }

    /**
     * Convenience method showing SimpleAlertFragment
     * */
    protected final void showAlertAndClose(String title , String formatMessage , CharSequence ... higlightedInputs) {
        SimpleAlertFragment fr = SimpleAlertFragment.newInstance(title,formatMessage,higlightedInputs);
        fr.show(getSupportFragmentManager(),null);
    }

    /**
     * Display a non-cancelable information dialog window with a message tu user.
     * It can perform an action after pressing OK button via passing in a {@code OnAlertDialogOKClick} instance
     * */
//    protected final void showAlertAndClose(String message , OnAlertDialogOKClick onClickOK) {
//        SimpleAlertFragment.newInstance(getString(R.string.))
////        AlertDialog.Builder builder = new AlertDialog.Builder(this);
////        builder.setCancelable(false)
////                .setMessage(message)
////                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        if (null != onClickOK) {
////                            onClickOK.onClick();
////                        }
////                    }
////                })
////                .show();
//    }


}
