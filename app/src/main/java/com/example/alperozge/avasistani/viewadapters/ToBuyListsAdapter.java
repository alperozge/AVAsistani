package com.example.alperozge.avasistani.viewadapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alperozge.avasistani.R;
import com.example.alperozge.avasistani.appdata.dbentity.ToBuyList;
import com.example.alperozge.avasistani.appdata.vievmodel.OnAvailableListsClicked;
import com.example.alperozge.avasistani.databinding.ToBuyListBinding;

import java.util.List;
import java.util.Objects;

public class ToBuyListsAdapter extends RecyclerView.Adapter<ToBuyListsAdapter.ToBuyListsViewHolder> {
    List<? extends ToBuyList> mToBuyLists;

    // A callback whenever an item is clicked
    private OnAvailableListsClicked onClick;

    /**
     * This field makes it possible to track selected items can be tracked. There are necessary additions to
     * fully implement selection behavior.
     *
     * 1. Extend ItemDetailsLookup<Long> and override abstract method {@code getItemDetails(MotionEvent e)}
     *
     * 2. Create a field in ViewHolder class with type ItemDetailsLookup.ItemDetails<Long>. It is abstract, so
     *    implement the abstract methods {@code public int getPosition()} and {@code getSelectionKey()}. Also,
     *    add a getter for the field (e.g public ItemDetailsLookup.ItemDetails<Long> getItemDetails())
     *
     * 3. Implement highlighting selection. This can be an animation, here implemented as changing the background
     *    color of the item view. Add a Selector resource in {@code drawable} folder.
     *
     * 4. Add the SelectionTracker...
     */

    private SelectionTracker<Long> mSelectionTracker;

    public ToBuyListsAdapter() {
        setHasStableIds(true);
    }

    public void setOnAvailableListClicked(OnAvailableListsClicked onClick) {
        this.onClick = onClick;
//        notifyDataSetChanged();
    }

    public void setToBuyLists(List<? extends ToBuyList> toBuyLists) {

        if(null == toBuyLists) {
            return;
        }
        if (null == mToBuyLists) {
            mToBuyLists = toBuyLists;
            notifyItemRangeInserted(0,toBuyLists.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mToBuyLists.size();
                }

                @Override
                public int getNewListSize() {
                    return toBuyLists.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mToBuyLists.get(oldItemPosition).getListId() == toBuyLists.get(newItemPosition).getListId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    ToBuyList newList = toBuyLists.get(newItemPosition);
                    ToBuyList oldList = mToBuyLists.get(oldItemPosition);
                    return newList.getListId() == oldList.getListId()
                            && Objects.equals(newList.getListName(), oldList.getListName());
                }
            });
            mToBuyLists = toBuyLists;
            result.dispatchUpdatesTo(this);
        }
    }

    public void setSelectionTracker(SelectionTracker<Long> tracker) {
        mSelectionTracker = tracker;
    }

    @Override
    public ToBuyListsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ToBuyListBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.to_buy_list,
                        parent, false);
        if(null != onClick)
        binding.setCallback(onClick);
        return new ToBuyListsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ToBuyListsViewHolder holder, int position) {

        holder.binding.setToBuyList(mToBuyLists.get(position));
        holder.binding.executePendingBindings();
        holder.binding.toBuyListView.setActivated(mSelectionTracker.isSelected(getItemId(position)));
    }

    @Override
    public int getItemCount() {
        return mToBuyLists == null ? 0 : mToBuyLists.size();
    }

    @Override
    public long getItemId(int position) {
        return mToBuyLists.get(position).getListId();
    }

    public static class ToBuyListsViewHolder extends RecyclerView.ViewHolder {
        final ToBuyListBinding binding;

        //Field to provide stable selection. Required to use SlectionTracker API
        final ItemDetailsLookup.ItemDetails<Long> itemDetails = new ItemDetailsLookup.ItemDetails<Long>() {
            @Override
            public int getPosition() {
                return getAdapterPosition();
            }

            @Nullable
            @Override
            public Long getSelectionKey() {
                return (long)binding.getToBuyList().getListId();
            }
        };


        public ToBuyListsViewHolder(ToBuyListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
            return itemDetails;
        }
    }
}
