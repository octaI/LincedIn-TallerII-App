package com.fiuba.tallerii.lincedin.fragments;


import android.content.Intent;
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
import com.fiuba.tallerii.lincedin.activities.EducationActivity;
import com.fiuba.tallerii.lincedin.activities.WorkExperienceActivity;
import com.fiuba.tallerii.lincedin.adapters.UserEducationAdapter;
import com.fiuba.tallerii.lincedin.adapters.UserJobsAdapter;
import com.fiuba.tallerii.lincedin.adapters.UserSkillsAdapter;
import com.fiuba.tallerii.lincedin.model.user.User;
import com.fiuba.tallerii.lincedin.model.user.UserJob;
import com.fiuba.tallerii.lincedin.network.HttpRequestHelper;
import com.fiuba.tallerii.lincedin.utils.DateUtils;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils.getStringFromSharedPreferences;

public class UserProfileFragment extends Fragment {

    private static final String TAG = "UserProfile";

    private static final String ARG_USER_ID = "USER_ID";

    private View convertView;

    private User user;
    private boolean isOwnProfile;

    private UserJobsAdapter userJobsAdapter;
    private UserEducationAdapter userEducationAdapter;
    private UserSkillsAdapter userSkillsAdapter;

    public UserProfileFragment() {}

    public static UserProfileFragment newInstance(String userId) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setIsOwnProfileFlag();

        userSkillsAdapter = new UserSkillsAdapter(getContext());
        userJobsAdapter = new UserJobsAdapter(getContext());
        userEducationAdapter = new UserEducationAdapter(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();

        // Profile could be updated from other activities, e.g. WorkExperience, Skills, etc.
        requestUserProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        setAdapters(convertView);
        setButtonsListeners(convertView);
        setButtonsVisibility(convertView);
        requestUserProfile();

        return convertView;
    }

    private void setIsOwnProfileFlag() {
        isOwnProfile = getArguments() == null || getArguments().getString(ARG_USER_ID) == null;
    }

    private void setButtonsListeners(final View parentView) {
        parentView.findViewById(R.id.user_profile_work_experience_see_more_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserWorkExperience();
            }
        });

        parentView.findViewById(R.id.user_profile_education_see_more_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserEducation();
            }
        });
    }

    private void setButtonsVisibility(View v) {
        if (isOwnProfile) {
            v.findViewById(R.id.user_profile_public_buttons_layout).setVisibility(View.GONE);
            v.findViewById(R.id.user_own_profile_edit_button).setVisibility(View.VISIBLE);
        } else {
            v.findViewById(R.id.user_profile_public_buttons_layout).setVisibility(View.VISIBLE);
            v.findViewById(R.id.user_own_profile_edit_button).setVisibility(View.GONE);
        }
    }

    private void requestUserProfile() {
        if (convertView != null) {
            final Map<String, String> requestParams = new HashMap<>();
            final String url = "http://"
                    + getStringFromSharedPreferences(getContext(), SharedPreferencesKeys.SERVER_IP, HTTPConfigurationDialogFragment.DEFAULT_SERVER_IP)
                    + ":" + getStringFromSharedPreferences(getContext(), SharedPreferencesKeys.SERVER_PORT, HTTPConfigurationDialogFragment.DEFAULT_PORT_EXPOSED)
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

                            user = gson.fromJson(response.toString(), User.class);
                            populateProfile(convertView, user);
                            refreshLoadingIndicator(convertView, false);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.toString());
                            error.printStackTrace();
                            refreshLoadingIndicator(convertView, false);
                        }
                    },
                    "UserProfileRequest"
            );
        }
    }

    private void setAdapters(View v) {
        ((ListView) v.findViewById(R.id.user_profile_work_experience_listview)).setAdapter(userJobsAdapter);
        ((ListView) v.findViewById(R.id.user_profile_education_listview)).setAdapter(userEducationAdapter);
        ((ListView) v.findViewById(R.id.user_profile_skills_listview)).setAdapter(userSkillsAdapter);
    }

    private void populateProfile(View v, User user) {
        populateBasicInfo(v, user);
        populateCurrentWork(v, user);
        populateBiography(v, user);
        populateWorkExperience(user);
        populateEducation(user);
        populateSkills(user);
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

        ((TextView) v.findViewById(R.id.user_profile_biography_date_of_birth_textview))
                .setText("(" + DateUtils.parseDatetimeToLocalDate(getContext(), user.dateOfBirth) + ")");

        ((TextView) v.findViewById(R.id.user_profile_biography_age_textview))
                .setText(
                        ((TextView) v.findViewById(R.id.user_profile_biography_age_textview)).getText().toString()
                        .replace(":1", DateUtils.getAgeFromDatetime(user.dateOfBirth))
                );

        ((TextView) v.findViewById(R.id.user_profile_biography_email_textview)).setText(user.email);

        // TODO: 05/11/16 Set location when API supports it.
    }

    private void populateWorkExperience(User user) {
        List<UserJob> jobsToShow = user.jobs.subList(0, user.jobs.size() - 1);
        if (user.isCurrentlyWorking()) {
            jobsToShow.remove(user.getCurrentWork());
        }
        userJobsAdapter.setDataset(jobsToShow);
        userJobsAdapter.notifyDataSetChanged();
    }

    private void populateCurrentWork(View v, User user) {
        if (user.isCurrentlyWorking()) {
            v.findViewById(R.id.user_profile_current_job_cardview).setVisibility(View.VISIBLE);

            ((TextView) v.findViewById(R.id.user_profile_current_job_company_textview)).setText(user.getCurrentWork().company);
            ((TextView) v.findViewById(R.id.user_profile_current_job_position_textview)).setText(user.getCurrentWork().position.name);
            ((TextView) v.findViewById(R.id.user_profile_current_job_since_date_textview))
                    .setText(
                            ((TextView) v.findViewById(R.id.user_profile_current_job_since_date_textview)).getText().toString()
                                    .replace(":1", DateUtils.extractYearFromDatetime(user.getCurrentWork().since))
                    );
        } else {
            v.findViewById(R.id.user_profile_current_job_cardview).setVisibility(View.GONE);
        }
    }

    private void populateEducation(User user) {
        userEducationAdapter.setDataset(user.education);
        userEducationAdapter.notifyDataSetChanged();
    }

    private void populateSkills(User user) {
        userSkillsAdapter.setDataset(user.skills);
        userSkillsAdapter.notifyDataSetChanged();
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

    private void openUserWorkExperience() {
        Intent workExperienceIntent = new Intent(getContext(), WorkExperienceActivity.class);
        if (user != null) {
            workExperienceIntent.putExtra(WorkExperienceActivity.ARG_USER, new Gson().toJson(user));
        }
        workExperienceIntent.putExtra(WorkExperienceActivity.ARG_IS_OWN_PROFILE, isOwnProfile);
        startActivity(workExperienceIntent);
    }

    private void openUserEducation() {
        Intent educationIntent = new Intent(getContext(), EducationActivity.class);
        if (user != null) {
            educationIntent.putExtra(EducationActivity.ARG_USER, new Gson().toJson(user));
        }
        educationIntent.putExtra(EducationActivity.ARG_IS_OWN_PROFILE, isOwnProfile);
        startActivity(educationIntent);
    }

}
