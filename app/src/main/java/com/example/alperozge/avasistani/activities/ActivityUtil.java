package com.example.alperozge.avasistani.activities;

import android.util.Log;


import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.selection.OperationMonitor;
import androidx.recyclerview.selection.SelectionTracker;

public class ActivityUtil {
    private static final String TAG = "ActivityUtil";
    private static OperationMonitor.OnChangeListener getOperationMonitorOnChangeForActivity(AVListActivity activity  , ActionMode.Callback callback) {


        return new OperationMonitor.OnChangeListener() {
            @Override
            public void onChanged() {
               if(!activity.isActionMode()) {
                   activity.startSupportActionMode(callback);
               }

            }
        };
    }



    public static OperationMonitor getOperationMonitorForActivity(AVListActivity activity , ActionMode.Callback callback) {
        OperationMonitor op = new OperationMonitor();
        op.addListener(getOperationMonitorOnChangeForActivity(activity , callback));
        return op;
    }
}
