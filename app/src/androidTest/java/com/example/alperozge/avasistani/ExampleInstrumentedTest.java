package com.example.alperozge.avasistani;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;


import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        SpannableStringBuilder sb = new SpannableStringBuilder("There is RED text");
        sb.setSpan(new ForegroundColorSpan(255),10,12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Log.d("", "useAppContext: " + (sb.getSpans(0,sb.length(),ForegroundColorSpan.class)[0]));
        assertEquals("com.example.alperozge.avasistani", appContext.getPackageName());
    }
}
