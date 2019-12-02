package com.example.alperozge.avasistani.viewadapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alperozge.avasistani.R;
import com.example.alperozge.avasistani.appdata.dbentity.ToBuyItem;
import com.example.alperozge.avasistani.databinding.ToBuyItemBinding;

import java.util.List;
import java.util.Objects;

public class ToBuyItemsAdapter extends RecyclerView.Adapter<ToBuyItemsAdapter.ToBuyItemViewHolder> {

    private List<? extends ToBuyItem> mToBuyItems;
    private SelectionTracker<Long> mSelectionTracker;


    public ToBuyItemsAdapter() {
        setHasStableIds(true);
    }

    public ToBuyItemsAdapter(SelectionTracker<Long> selectionTracker) {
        this();
        mSelectionTracker = selectionTracker;
    }

    /*Override RecyclerView.Adapter methods*/
    @NonNull
    @Override
    public ToBuyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ToBuyItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.to_buy_item,
                        parent, false);
//        if(null != onClick)
//            binding.setCallback(onClick);
        return new ToBuyItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ToBuyItemViewHolder holder, int position) {
        holder.mBinding.setToBuyItem(mToBuyItems.get(position));
        holder.mBinding.executePendingBindings();
        holder.mBinding.toBuyItemView.setActivated(mSelectionTracker.isSelected(getItemId(position)));
    }

    @Override
    public int getItemCount() {
        return mToBuyItems.size();
    }

    public void setToBuyItems(List<? extends ToBuyItem> items) {
        if(null == items) {
            return;
        }
        if (null == mToBuyItems) {
            mToBuyItems = items;
            notifyItemRangeInserted(0,items.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mToBuyItems.size();
                }

                @Override
                public int getNewListSize() {
                    return items.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mToBuyItems.get(oldItemPosition).getId() == items.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    ToBuyItem newItem = items.get(newItemPosition);
                    ToBuyItem oldItem = items.get(oldItemPosition);
                    return newItem.getId() == oldItem.getId()
                            && Objects.equals(newItem.getItem(), oldItem.getItem())
                            && Objects.equals(newItem.getListNo() , oldItem.getListNo());
                }
            });
            mToBuyItems = items;
            result.dispatchUpdatesTo(this);
        }
    }

    public void setSelectionTracker(SelectionTracker<Long> tracker) {
        this.mSelectionTracker = tracker;
    }

    public static class ToBuyItemViewHolder extends RecyclerView.ViewHolder {
        ToBuyItemBinding mBinding;

        final ItemDetailsLookup.ItemDetails<Long> mItemDetails = new ItemDetailsLookup.ItemDetails<Long>() {
            @Override
            public int getPosition() {
                return getAdapterPosition();
            }

            @Nullable
            @Override
            public Long getSelectionKey() {
                return (long)mBinding.getToBuyItem().getId();
            }
        };

        public ToBuyItemViewHolder(ToBuyItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
            return mItemDetails;
        }
    }
}
