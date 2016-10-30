package com.fiuba.tallerii.lincedin.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class UserAuthenticationManager {

    public static void signUp(Context context, String firstName, String lastName, String email, String birthday, String password) {
        // TODO: 30/10/16 Request to app server and call saveUserToken on success.
    }

    public static void facebookLogIn(Context context, JSONObject fbUserInfo) {
        // TODO: 30/10/16 Request to app server and call saveUserToken on success.
    }

    public static void logIn(Context context, String password) {
        // TODO: 30/10/16 Request to app server and call saveUserToken on success.
    }

    public static void logOut(Context context) {
        // TODO: 30/10/16 Request to app server and call deleteSessionToken on success.
    }

    public static void saveSessionToken(Context context, String sessionToken) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(SharedPreferencesKeys.USER_LOGGED_IN, true);
        editor.putString(SharedPreferencesKeys.SESSION_TOKEN, sessionToken);
        editor.apply();
    }

    public static void deleteSessionToken(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(SharedPreferencesKeys.USER_LOGGED_IN, false);
        editor.remove(SharedPreferencesKeys.SESSION_TOKEN);
        editor.apply();
    }
}
