package com.fiuba.tallerii.lincedin.network;

import android.content.Context;

import com.android.volley.Response;
import com.facebook.login.LoginManager;
import com.fiuba.tallerii.lincedin.model.user.login.FacebookLogInUser;
import com.fiuba.tallerii.lincedin.model.user.login.LincedInLogInUser;
import com.fiuba.tallerii.lincedin.model.user.login.LogInUser;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils;

import org.json.JSONObject;

public class UserAuthenticationManager {

    private static final String TAG = "UserAuthentication";

    public static final String LOGIN_TYPE_FACEBOOK = "Facebook";
    public static final String LOGIN_TYPE_NORMAL = "Normal";

    public static void signUp(Context context, String firstName, String lastName, String email, String birthday, String password) {
        // TODO: 30/10/16 Request to app server and call saveUserToken on success.
    }

    public static void facebookLogIn(final Context context, String facebookAccessToken,
                                     final Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {

        Response.Listener<JSONObject> listenerThatSavesSessionType = extendSuccessListenerForSavingSessionType(context, successListener, LOGIN_TYPE_FACEBOOK);
        LogInUser logInUser = new FacebookLogInUser(facebookAccessToken);
        LincedInRequester.logIn(
                logInUser,
                context,
                listenerThatSavesSessionType,
                errorListener
        );
    }

    public static void lincedInLogIn(final Context context, String email, String password,
                                     final Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {

        Response.Listener<JSONObject> listenerThatSavesSessionType = extendSuccessListenerForSavingSessionType(context, successListener, LOGIN_TYPE_NORMAL);
        LogInUser logInUser = new LincedInLogInUser(email, password);
        LincedInRequester.logIn(
                logInUser,
                context,
                listenerThatSavesSessionType,
                errorListener
        );
    }

    private static Response.Listener<JSONObject> extendSuccessListenerForSavingSessionType(final Context context, final Response.Listener<JSONObject> listener, final String sessionType) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                SharedPreferencesUtils.putStringToSharedPreferences(context, SharedPreferencesKeys.SESSION_TYPE, sessionType);
                listener.onResponse(response);
            }
        };
    }

    public static void logOut(Context context) {
        deleteUserAuthInfo(context);
        LoginManager.getInstance().logOut();
    }

    public static boolean isUserLoggedIn(Context context) {
        return SharedPreferencesUtils.getBooleanFromSharedPreferences(
                context,
                SharedPreferencesKeys.USER_LOGGED_IN,
                false
        );
    }

    public static boolean isUserLoggedInWithFacebookAccount(Context context) {
        return isUserLoggedIn(context) &&
                SharedPreferencesUtils.getStringFromSharedPreferences(context, SharedPreferencesKeys.SESSION_TYPE, "").equals(LOGIN_TYPE_NORMAL);
    }

    public static boolean isUserLoggedInWithLincedInAccount(Context context) {
        return isUserLoggedIn(context) &&
                SharedPreferencesUtils.getStringFromSharedPreferences(context, SharedPreferencesKeys.SESSION_TYPE, "").equals(LOGIN_TYPE_FACEBOOK);
    }

    public static String getSessionToken(Context context) {
        return SharedPreferencesUtils.getStringFromSharedPreferences(context, SharedPreferencesKeys.SESSION_TOKEN, null);
    }

    public static void saveUserAuthInfo(Context context, String sessionToken, String email, String password) {
        SharedPreferencesUtils.putBooleanToSharedPreferences(context, SharedPreferencesKeys.USER_LOGGED_IN, true);
        SharedPreferencesUtils.putStringToSharedPreferences(context, SharedPreferencesKeys.SESSION_TOKEN, sessionToken);
        SharedPreferencesUtils.putStringToSharedPreferences(context, SharedPreferencesKeys.USER_EMAIL, email);
        SharedPreferencesUtils.putStringToSharedPreferences(context, SharedPreferencesKeys.USER_PASSWORD, password);
    }

    public static void deleteUserAuthInfo(Context context) {
        SharedPreferencesUtils.removeFromSharedPreferences(context, SharedPreferencesKeys.USER_LOGGED_IN);
        SharedPreferencesUtils.removeFromSharedPreferences(context, SharedPreferencesKeys.SESSION_TOKEN);
        SharedPreferencesUtils.removeFromSharedPreferences(context, SharedPreferencesKeys.USER_EMAIL);
        SharedPreferencesUtils.removeFromSharedPreferences(context, SharedPreferencesKeys.USER_PASSWORD);

        SharedPreferencesUtils.removeFromSharedPreferences(context, SharedPreferencesKeys.SESSION_TYPE);
    }
}
