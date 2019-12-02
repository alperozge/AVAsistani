package com.example.alperozge.avasistani.ui;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MargineItemDecoration extends RecyclerView.ItemDecoration {
    private int mVerticalMargin;
    public MargineItemDecoration(int verticalMargin) {
        super();
        mVerticalMargin = verticalMargin;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if(position != 0) {
            outRect.top = mVerticalMargin;
        }

        if(position != parent.getAdapter().getItemCount()) {
            outRect.bottom = mVerticalMargin;
        }
    }
}
