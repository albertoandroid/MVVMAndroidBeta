package com.example.mvvm.binding;

import android.view.View;

import androidx.databinding.BindingAdapter;

/*
Data Binding adapter especifico para la app
 */
public class BindingAdapters {
    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
