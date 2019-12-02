package com.example.alperozge.avasistani.activities;

import androidx.annotation.Nullable;

public interface SimpleAlertDisplayerActivity {

    /**
     * Calback when SipleAlertFragment returns by pressing positive button.
     * @param inputTexts The 2nd input parameter CharSequence passed when creating the Fragment - <b> highlightedInput</b>. Any subclass
     *                   version is truncated, and a bare CharSequence instance is used here
     * */

    void onAlertReturnPositive(@Nullable CharSequence[] inputTexts);

    void onAlertReturnNegative();

}
