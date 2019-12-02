package com.example.alperozge.avasistani.selection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

public class CustomStableIdProvider extends ItemKeyProvider<Long> {
    /**
     * Creates a new provider with the given scope.
     *
     */
    RecyclerView mRecyclerView;

    public CustomStableIdProvider(RecyclerView recyclerView) {
        super(ItemKeyProvider.SCOPE_MAPPED);
        mRecyclerView = recyclerView;
    }

    @Nullable
    @Override
    public Long getKey(int position) {
        return null != mRecyclerView.getAdapter() ? mRecyclerView.getAdapter().getItemId(position) : null;
    }

    @Override
    public int getPosition(@NonNull Long key) {
        RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForItemId(key);
        return null != holder ? holder.getLayoutPosition() : RecyclerView.NO_POSITION;
    }
}
