package com.fiuba.tallerii.lincedin.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.model.user.login.FacebookLogInUser;
import com.fiuba.tallerii.lincedin.model.user.login.LogInUser;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class UserAuthenticationManager {

    private static final String TAG = "UserAuthentication";

    public static void signUp(Context context, String firstName, String lastName, String email, String birthday, String password) {
        // TODO: 30/10/16 Request to app server and call saveUserToken on success.
    }

    public static void facebookLogIn(final Context context, String facebookAccessToken) {
        LogInUser logInUser = new FacebookLogInUser(facebookAccessToken);
        LincedInRequester.logIn(
                logInUser,
                context,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, new Gson().toJson(response));

                        try {
                            saveSessionToken(context, response.getString("token"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        error.printStackTrace();
                    }
                }
        );
    }

    public static void lincedinLogIn(Context context, String password) {
        // TODO: 30/10/16 Request to app server and call saveUserToken on success.
    }

    public static void logOut(Context context) {
        // TODO: 30/10/16 Request to app server and call deleteSessionToken on success.
    }

    public static String getSessionToken(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SharedPreferencesKeys.SESSION_TOKEN, null);
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
