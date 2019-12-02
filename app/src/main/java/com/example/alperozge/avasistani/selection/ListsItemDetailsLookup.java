package com.example.alperozge.avasistani.selection;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alperozge.avasistani.viewadapters.ToBuyListsAdapter;

public class ListsItemDetailsLookup extends ItemDetailsLookup<Long> {
    private RecyclerView mRecyclerView;
    public ListsItemDetailsLookup(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        View view = mRecyclerView.findChildViewUnder(e.getX(),e.getY());
        if(null != view) {
            return ((ToBuyListsAdapter.ToBuyListsViewHolder)mRecyclerView.getChildViewHolder(view)).getItemDetails();
        } else {
            return null;
        }
    }
}
