package com.fiuba.tallerii.lincedin.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.fragments.HTTPConfigurationDialogFragment;
import com.fiuba.tallerii.lincedin.model.user.User;
import com.fiuba.tallerii.lincedin.model.user.login.LogInUser;
import com.fiuba.tallerii.lincedin.model.user.signup.SignUpUser;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils.getStringFromSharedPreferences;

public class LincedInRequester {

    private static String getAppServerBaseURL(Context context) {
        return "http://"
                + "lincedin.ddns.net";
                //+ getStringFromSharedPreferences(context, SharedPreferencesKeys.SERVER_IP, HTTPConfigurationDialogFragment.DEFAULT_SERVER_IP)
                //+ ":" + getStringFromSharedPreferences(context, SharedPreferencesKeys.SERVER_PORT, HTTPConfigurationDialogFragment.DEFAULT_PORT_EXPOSED);
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
            HttpRequestHelper.put(
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
}
