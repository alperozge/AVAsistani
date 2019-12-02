package com.example.alperozge.avasistani.activities;

import android.os.Parcelable;

import androidx.annotation.Nullable;

/**
 * Interface that enable AddEditItemFragment to perform action on activity that it is attached to.
 * */
public interface EditableItemsActivity {
    /**
     * Method called when AddListDialogFragment's OperationMode is {@code OperationMode.ADD} and AddLiatDialogFragment's positive ("OK") button is pressed.
     *
     * @param fragmentFeedback user's entered value.
     * @param  fragmentTag optional tag provided to AddListDialogFragment. It is null if no tag is provided.
     * */
     void onAddReturnPositive(String fragmentFeedback , @Nullable Parcelable fragmentTag);

    /**
     * Method called when AddListDialogFragment's OperationMode is {@code OperationMode.EDIT} and AddLiatDialogFragment's positive ("OK") button is pressed.
     *
     * @param fragmentFeedback user's entered value.
     * @param  fragmentTag optional tag provided to AddListDialogFragment. It is null if no tag is provided.
     * */
    void onEditReturnPositive(String fragmentFeedback , @Nullable Parcelable fragmentTag);

     void onReturnNegative();
}
