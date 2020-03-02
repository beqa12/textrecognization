package me.shuza.textrecognization.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPref {
    public static final String COLUMN_QUANTITY_PREF = "pref";
    private static final String NUMBER_COLUMN = "column_number";
    private static final String CHECKED = "checked";

    public static void saveColumnNumber(Context context, int value){
        SharedPreferences preferences = context.getSharedPreferences(COLUMN_QUANTITY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(NUMBER_COLUMN, value);
        editor.apply();
    }

    public static int getColumnNumber(Context context){
        SharedPreferences preferences = context.getSharedPreferences(COLUMN_QUANTITY_PREF, Context.MODE_PRIVATE);
        return preferences.getInt(NUMBER_COLUMN, -1);
    }


    public static void saveChecked(Context context, int value){
        SharedPreferences preferences = context.getSharedPreferences(COLUMN_QUANTITY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(CHECKED, value);
        editor.apply();
    }

    public static int getChecked(Context context){
        SharedPreferences preferences = context.getSharedPreferences(COLUMN_QUANTITY_PREF, Context.MODE_PRIVATE);
        return preferences.getInt(CHECKED, -1);
    }

}
