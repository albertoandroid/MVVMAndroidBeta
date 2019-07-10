package com.example.mvvm.util;

import androidx.lifecycle.LiveData;

/*
Una clase livedata qeu tiene un valoor null
 */
@SuppressWarnings("unchecked")
public class AbsentLiveData extends LiveData {
    private AbsentLiveData() {
        postValue(null);
    }
    public static <T> LiveData<T> create() {
        //noinspection unchecked
        return new AbsentLiveData();
    }
}

