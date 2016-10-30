package com.fiuba.tallerii.lincedin.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class UserAccountManager {
    private static UserAccount userAccount;

    public static UserAccount getUserAccount() {
        return userAccount;
    }

    public static void logIn(Context context) {
        // TODO: 30/10/16 Request to app server and call saveUserAccount on success.
    }

    public static void logOut(Context context) {
        // TODO: 30/10/16 Request to app server and call deleteUserAccount on success.
    }

    private static void saveUserAccount(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(SharedPreferencesKeys.USER_LOGGED_IN, true);
        editor.putString(SharedPreferencesKeys.USER_ACCOUNT, new Gson().toJson(userAccount));
        editor.apply();
    }

    private static void deleteUserAccount(Context context) {
        userAccount = null;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(SharedPreferencesKeys.USER_LOGGED_IN, false);
        editor.remove(SharedPreferencesKeys.USER_ACCOUNT);
        editor.apply();
    }

    public static UserAccount createUserAccountFromFacebookResponse(JSONObject jsonResponse) {
        try {
            userAccount = new UserAccount();
            userAccount.setUserId(jsonResponse.getString("id"));
            userAccount.setFirstName(jsonResponse.getString("first_name"));
            userAccount.setLastName(jsonResponse.getString("last_name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userAccount;
    }
}
