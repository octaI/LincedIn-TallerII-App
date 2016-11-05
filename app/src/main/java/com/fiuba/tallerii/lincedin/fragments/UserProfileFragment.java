package com.fiuba.tallerii.lincedin.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.adapters.UserSkillsAdapter;
import com.fiuba.tallerii.lincedin.model.user.UserSkill;
import com.fiuba.tallerii.lincedin.network.HttpRequestHelper;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileFragment extends Fragment {

    private static final String TAG = "UserProfile";

    private UserSkillsAdapter userSkillsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userSkillsAdapter = new UserSkillsAdapter(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        requestUserProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);

        setDummySkill(v);

        // TODO: 03/11/16 Hide loading and show container after callback.
        setLoading(v, false);

        return v;
    }

    // TODO: 03/11/16 Delete this!
    @Deprecated
    private void setDummySkill(View v) {
        UserSkill dummySkill = new UserSkill("Java", "Software", "Basics of Java programming language");
        List<UserSkill> skills = new ArrayList<>();
        skills.add(dummySkill);
        userSkillsAdapter.setDataset(skills);
        userSkillsAdapter.notifyDataSetChanged();
        ((ListView) v.findViewById(R.id.user_profile_skills_listview)).setAdapter(userSkillsAdapter);
    }

    private void requestUserProfile() {
        final Map<String, String> requestParams = new HashMap<>();
        final String url = "http://"
                + getStringFromSharedPreferences(SharedPreferencesKeys.SERVER_IP, HTTPConfigurationDialogFragment.DEFAULT_SERVER_IP)
                + ":" + getStringFromSharedPreferences(SharedPreferencesKeys.SERVER_PORT, HTTPConfigurationDialogFragment.DEFAULT_PORT_EXPOSED)
                + "/user"
                + "/me";
        HttpRequestHelper.get(
                url,
                requestParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        error.printStackTrace();
                    }
                },
                "UserProfileRequest"
        );
    }

    private void setLoading(View v, boolean loading) {
        if (loading) {
            v.findViewById(R.id.user_profile_main_container_nestedscrollview).setVisibility(View.INVISIBLE);
            v.findViewById(R.id.user_profile_loading_circular_progress).setVisibility(View.VISIBLE);
        } else {
            v.findViewById(R.id.user_profile_main_container_nestedscrollview).setVisibility(View.VISIBLE);
            v.findViewById(R.id.user_profile_loading_circular_progress).setVisibility(View.GONE);
        }
    }

    private String getStringFromSharedPreferences(String key, String defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getString(key, defaultValue);
    }

}
