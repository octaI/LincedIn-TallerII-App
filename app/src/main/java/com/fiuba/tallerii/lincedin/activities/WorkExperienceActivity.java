package com.fiuba.tallerii.lincedin.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.fragments.AddJobFragment;
import com.fiuba.tallerii.lincedin.fragments.AllJobsFragment;
import com.fiuba.tallerii.lincedin.fragments.HTTPConfigurationDialogFragment;
import com.fiuba.tallerii.lincedin.model.user.User;
import com.fiuba.tallerii.lincedin.model.user.UserJob;
import com.fiuba.tallerii.lincedin.network.HttpRequestHelper;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils.getStringFromSharedPreferences;

public class WorkExperienceActivity extends AppCompatActivity
        implements AllJobsFragment.AllJobsFragmentListener, AddJobFragment.AddJobFragmentListener {

    private static final String TAG = "WorkExperience";

    public static final String ARG_USER = "USER";
    public static final String ARG_IS_OWN_PROFILE = "IS_OWN_PROFILE";

    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_experience);
        setToolbar();
        getUserFromIntent();
        showAllJobsFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_work_experience);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private User getUserFromIntent() {
        String jobsJson = getIntent().getStringExtra(ARG_USER);
        if (jobsJson != null) {
            user = new Gson().fromJson(jobsJson, User.class);
        }
        return user;
    }

    private void showAllJobsFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.work_experience_container_framelayout, AllJobsFragment.newInstance(user.jobs));
        transaction.addToBackStack("AllJobsFragment");
        transaction.commit();
    }

    private void showAddJobFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.work_experience_container_framelayout, new AddJobFragment());
        transaction.addToBackStack("AddJobFragment");
        transaction.commit();
    }

    @Override
    public void onAddJobButtonPressed() {
        showAddJobFragment();
    }

    @Override
    public void onApplyChangesButtonPressed(UserJob job) {
        Log.i(TAG, "User job to save: " + new Gson().toJson(job));
        user.jobs.add(job);  // TODO: 10/11/16 Find workaround for when the job is being edited, not created from scratch.

        final Map<String, String> requestParams = new HashMap<>();
        final String url = "http://"
                + getStringFromSharedPreferences(this, SharedPreferencesKeys.SERVER_IP, HTTPConfigurationDialogFragment.DEFAULT_SERVER_IP)
                + ":" + getStringFromSharedPreferences(this, SharedPreferencesKeys.SERVER_PORT, HTTPConfigurationDialogFragment.DEFAULT_PORT_EXPOSED)
                + "/user"
                + "/me";

        try {
            HttpRequestHelper.put(
                    url,
                    requestParams,
                    new JSONObject(new Gson().toJson(user)),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            showAllJobsFragment();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            showAllJobsFragment();
                        }
                    },
                    "EditUserProfile"
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
