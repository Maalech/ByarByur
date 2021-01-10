package com.byarbyur.myapplication.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPref {
    static final String KEY_ROLE ="user";
    private static SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static void setRole(Context context, String role){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }
    public static String getRole(Context context){
        return getSharedPreference(context).getString(KEY_ROLE,"");
    }

}
