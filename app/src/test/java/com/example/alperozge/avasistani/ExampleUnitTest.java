package com.example.alperozge.avasistani;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        SpannableStringBuilder sb = new SpannableStringBuilder("There is RED text");
        sb.setSpan(new ForegroundColorSpan(255),10,12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        System.out.println(sb.getSpans(0,sb.length(),ForegroundColorSpan.class)[0]);
        assertTrue(true);

    }
}