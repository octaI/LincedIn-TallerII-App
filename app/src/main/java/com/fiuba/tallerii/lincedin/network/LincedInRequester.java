package com.fiuba.tallerii.lincedin.network;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.fragments.HTTPConfigurationDialogFragment;
import com.fiuba.tallerii.lincedin.model.user.User;
import com.fiuba.tallerii.lincedin.model.user.login.LogInUser;
import com.fiuba.tallerii.lincedin.model.user.signup.SignUpUser;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils.getBooleanFromSharedPreferences;
import static com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils.getStringFromSharedPreferences;

public class LincedInRequester {

    private static String getAppServerBaseURL(Context context) {
        if (getBooleanFromSharedPreferences(context, SharedPreferencesKeys.SERVER_IS_LOCAL, false)) {
            return "http://"
                    + getStringFromSharedPreferences(context, SharedPreferencesKeys.SERVER_IP, HTTPConfigurationDialogFragment.DEFAULT_SERVER_IP)
                    + ":" + getStringFromSharedPreferences(context, SharedPreferencesKeys.SERVER_PORT, HTTPConfigurationDialogFragment.DEFAULT_PORT_EXPOSED);
        } else {
            return "http://"
                    + "lincedin.ddns.net";
        }
    }

    public static void logIn(LogInUser logInUser, Context context, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        final Map<String, String> requestParams = new HashMap<>();
        final String url = getAppServerBaseURL(context)
                + "/login";

        try {
            HttpRequestHelper.post(
                    url,
                    requestParams,
                    new JSONObject(new Gson().toJson(logInUser)),
                    successListener,
                    errorListener,
                    "LogInUser"
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getUserProfile(Context context, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        final Map<String, String> requestParams = new HashMap<>();
        final String url = getAppServerBaseURL(context)
                + "/user"
                + "/me";

        HttpRequestHelper.get(
                url,
                requestParams,
                successListener,
                errorListener,
                "GetUserProfile"
        );
    }

    public static void createUser(SignUpUser user, Context context, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        final Map<String, String> requestParams = new HashMap<>();
        final String url = getAppServerBaseURL(context)
                + "/user";

        try {
            HttpRequestHelper.post(
                    url,
                    requestParams,
                    new JSONObject(new Gson().toJson(user)),
                    successListener,
                    errorListener,
                    "CreateUser"
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void editUserProfile(User user, Context context, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        final Map<String, String> requestParams = new HashMap<>();
        final String url = getAppServerBaseURL(context)
                + "/user"
                + "/me";

        try {
            HttpRequestHelper.put(
                    url,
                    requestParams,
                    new JSONObject(new Gson().toJson(user)),
                    successListener,
                    errorListener,
                    "EditUserProfile"
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getUserReceivedRecommendations(String userId, Context context, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        final Map<String, String> requestParams = new HashMap<>();
        final String url = getAppServerBaseURL(context)
                + "/recommendations/"
                + userId;

        HttpRequestHelper.get(
                url,
                requestParams,
                successListener,
                errorListener,
                "GetUserReceivedRecommendations"
        );
    }

    public static void getUserMadeRecommendations(String userId, Context context, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        final Map<String, String> requestParams = new HashMap<>();
        final String url = getAppServerBaseURL(context)
                + "/recommendations/"
                + userId;

        HttpRequestHelper.get(
                url,
                requestParams,
                successListener,
                errorListener,
                "GetUserMadeRecommendations"
        );
    }

    public static void getAllUserChats(Context context, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        final Map<String, String> requestParams = new HashMap<>();
        final String url = getAppServerBaseURL(context)
                + "/chat";

        HttpRequestHelper.get(
                url,
                requestParams,
                successListener,
                errorListener,
                "GetAllUserChats"
        );
    }

    public static void getAllJobPositions(Context context, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        final Map<String, String> requestParams = new HashMap<>();
        final String url = getAppServerBaseURL(context)
                + "/shared"
                + "/job_positions";

        HttpRequestHelper.get(
                url,
                requestParams,
                successListener,
                errorListener,
                "GetAllJobPositions"
        );
    }

    public static void getAllSkills(Context context, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        final Map<String, String> requestParams = new HashMap<>();
        final String url = getAppServerBaseURL(context)
                + "/shared"
                + "/skills";

        HttpRequestHelper.get(
                url,
                requestParams,
                successListener,
                errorListener,
                "GetAllSkills"
        );
    }

    public static void getUserFriends(Context context, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener, @Nullable String anId) {
        /*
        You can use this request either to get an own user's friends or
        some other user's friends.
        anId field can either be null (an own user's friends) or not null
        (some other user's friends)
         */
        final Map<String,String> requestParams = new HashMap<>();
        final String url = getAppServerBaseURL(context) + "/friends/" + anId;

        HttpRequestHelper.get(url,requestParams,successListener,errorListener,"GetUserFriends");
    }

    public static void getUserProfileImage(Context context, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener, String imgId) {
        final String url = getAppServerBaseURL(context) + imgId;
        final Map<String,String> requestParams = new HashMap<>();
        HttpRequestHelper.get(url,requestParams,successListener,errorListener,"GetUserProfilePicture");
    }
}
