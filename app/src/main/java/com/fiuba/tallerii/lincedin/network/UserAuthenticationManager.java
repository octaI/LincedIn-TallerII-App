package com.fiuba.tallerii.lincedin.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.user.login.FacebookLogInUser;
import com.fiuba.tallerii.lincedin.model.user.login.LincedInLogInUser;
import com.fiuba.tallerii.lincedin.model.user.login.LogInUser;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class UserAuthenticationManager {

    private static final String TAG = "UserAuthentication";

    public static void signUp(Context context, String firstName, String lastName, String email, String birthday, String password) {
        // TODO: 30/10/16 Request to app server and call saveUserToken on success.
    }

    public static void facebookLogIn(final Context context, String facebookAccessToken,
                                     Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        LogInUser logInUser = new FacebookLogInUser(facebookAccessToken);
        LincedInRequester.logIn(
                logInUser,
                context,
                successListener,
                errorListener
        );
    }

    public static void lincedInLogIn(final Context context, String email, String password,
                                     Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        LogInUser logInUser = new LincedInLogInUser(email, password);
        LincedInRequester.logIn(
                logInUser,
                context,
                successListener,
                errorListener
        );
    }

    public static void logOut(Context context) {
        deleteSessionToken(context);    // TODO: 19/11/16 Call this only on success after log out request to app server

        LoginManager.getInstance().logOut();
    }

    public static boolean isUserLoggedIn(Context context) {
        return SharedPreferencesUtils.getBooleanFromSharedPreferences(
                context,
                SharedPreferencesKeys.USER_LOGGED_IN,
                false
        );
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
