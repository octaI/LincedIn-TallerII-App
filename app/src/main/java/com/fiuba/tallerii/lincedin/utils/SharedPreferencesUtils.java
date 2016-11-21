package com.fiuba.tallerii.lincedin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.securepreferences.SecurePreferences;

public class SharedPreferencesUtils {

    public static String getStringFromSharedPreferences(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = new SecurePreferences(context);
        return sharedPreferences.getString(key, defaultValue);
    }

    public static boolean getBooleanFromSharedPreferences(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = new SecurePreferences(context);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void putStringToSharedPreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = new SecurePreferences(context);
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static void putBooleanToSharedPreferences(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = new SecurePreferences(context);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static void removeFromSharedPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = new SecurePreferences(context);
        sharedPreferences.edit().remove(key).apply();
    }
}
