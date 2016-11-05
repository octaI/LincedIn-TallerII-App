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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.adapters.UserJobsAdapter;
import com.fiuba.tallerii.lincedin.adapters.UserSkillsAdapter;
import com.fiuba.tallerii.lincedin.model.user.User;
import com.fiuba.tallerii.lincedin.model.user.UserSkill;
import com.fiuba.tallerii.lincedin.network.HttpRequestHelper;
import com.fiuba.tallerii.lincedin.utils.DateUtils;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileFragment extends Fragment {

    private static final String TAG = "UserProfile";

    private User user;

    private UserJobsAdapter userJobsAdapter;
    private UserSkillsAdapter userSkillsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userSkillsAdapter = new UserSkillsAdapter(getContext());
        userJobsAdapter = new UserJobsAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);

        requestUserProfile(v);

        return v;
    }

    // TODO: 03/11/16 Delete this!
    @Deprecated
    private void setDummySkill(View v) {
        UserSkill dummySkill = new UserSkill();
        List<UserSkill> skills = new ArrayList<>();
        skills.add(dummySkill);
        userSkillsAdapter.setDataset(skills);
        userSkillsAdapter.notifyDataSetChanged();
        ((ListView) v.findViewById(R.id.user_profile_skills_listview)).setAdapter(userSkillsAdapter);
    }

    private void requestUserProfile(final View v) {
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
                        Gson gson = new Gson();
                        Log.d(TAG, gson.toJson(response));

                        User user = gson.fromJson(response.toString(), User.class);
                        populateProfile(v, user);
                        refreshLoadingIndicator(v, false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        error.printStackTrace();
                        refreshLoadingIndicator(v, false);
                    }
                },
                "UserProfileRequest"
        );
    }

    private void populateProfile(View v, User user) {
        populateBasicInfo(v, user);
        populateCurrentWork(v, user);
        populateBiography(v, user);
        populateWorkExperience(v, user);
        /*populateEducation(user);
        populateRecommendations(user);
        populateSkills(user);*/
    }

    private void populateBasicInfo(View v, User user) {
        Glide.with(getContext())
                .load(user.profilePicture)
                .centerCrop()
                .into((ImageView) v.findViewById(R.id.user_profile_picture_imageview));

        ((TextView) v.findViewById(R.id.user_profile_username_textview)).setText(user.fullName);

        ((TextView) v.findViewById(R.id.user_profile_user_description_textview)).setText(user.description);
    }

    private void populateBiography(View v, User user) {
        ((TextView) v.findViewById(R.id.user_profile_biography_fullname_textview)).setText(user.fullName);

        //((TextView) v.findViewById(R.id.user_profile_biography_age_textview)).setText(DateUtils.getAgeFromDatetime(user.dateOfBirth));
        ((TextView) v.findViewById(R.id.user_profile_biography_date_of_birth_textview)).setText(user.dateOfBirth);

        ((TextView) v.findViewById(R.id.user_profile_biography_email_textview)).setText(user.email);

        // TODO: 05/11/16 Set location when API supports it.
    }

    private void populateWorkExperience(View v, User user) {
        userJobsAdapter.setDataset(user.jobs);
        userJobsAdapter.notifyDataSetChanged();
        ((ListView) v.findViewById(R.id.user_profile_work_experience_jobs_listview)).setAdapter(userJobsAdapter);
    }

    private void populateCurrentWork(View v, User user) {
        // TODO: 05/11/16 Implement
    }

    private void refreshLoadingIndicator(View v, boolean loading) {
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
