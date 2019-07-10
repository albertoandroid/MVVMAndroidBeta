package com.example.mvvm.db;

import androidx.room.TypeConverter;
import androidx.room.util.StringUtil;

import java.util.Collections;
import java.util.List;

public class GithubTypeConverters {
    @TypeConverter
    public static List<Integer> stringToIntList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        //SplitToIntList -> Coge un String y lo convierte en una lista de enteros.
        return StringUtil.splitToIntList(data);
    }

    @TypeConverter
    public static String intListToString(List<Integer> ints) {
        //joinIntoString Coge una lista de entereros y lo pasa a String.
        return StringUtil.joinIntoString(ints);
    }
}
