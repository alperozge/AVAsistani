package com.example.alperozge.avasistani.activities;

import android.app.Activity;

import java.lang.ref.WeakReference;

@FunctionalInterface
public interface ActivityReferenceProvider<T extends Activity> {
    public WeakReference<T> getReference();
}
