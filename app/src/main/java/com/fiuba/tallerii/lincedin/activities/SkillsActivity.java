package com.fiuba.tallerii.lincedin.activities;

import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.user.User;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.google.gson.Gson;

import org.json.JSONObject;

public class SkillsActivity extends AppCompatActivity {

    private static final String TAG = "WorkExperience";

    public static final String ARG_USER = "USER";
    public static final String ARG_IS_OWN_PROFILE = "IS_OWN_PROFILE";

    private User user = new User();
    private boolean isOwnProfile;

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

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
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
        String userJson = getIntent().getStringExtra(ARG_USER);
        if (userJson != null) {
            user = new Gson().fromJson(userJson, User.class);
        }
        isOwnProfile = getIntent().getBooleanExtra(ARG_IS_OWN_PROFILE, false);
        return user;
    }

    private void showAllJobsFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.work_experience_container_framelayout, AllJobsFragment.newInstance(user.jobs, isOwnProfile));
        transaction.addToBackStack("AllJobsFragment");
        transaction.commit();
    }

    private void showEditJobFragment(@Nullable UserJob jobSelected) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.work_experience_container_framelayout, EditJobFragment.newInstance(jobSelected));
        transaction.addToBackStack("EditJobFragment");
        transaction.commit();
    }

    @Override
    public void onAddJobButtonPressed() {
        showEditJobFragment(null);
    }

    @Override
    public void onJobRowClicked(UserJob job) {
        showEditJobFragment(job);
    }

    @Override
    public void onNewJobAdded(final UserJob job) {
        Log.d(TAG, "User job to add: " + new Gson().toJson(job));
        user.jobs.add(job);
        LincedInRequester.editUserProfile(
                user,
                this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Log.i(TAG, "The job was added successfully!");
                        showAllJobsFragment();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        user.jobs.remove(job);
                        showAllJobsFragment();
                    }
                }
        );
    }

    @Override
    public void onJobEdited(final UserJob previousJob, final UserJob updatedJob) {
        Log.d(TAG, "User job to edit: from " + new Gson().toJson(previousJob) + " to " + new Gson().toJson(updatedJob));
        user.jobs.remove(previousJob);
        user.jobs.add(updatedJob);
        LincedInRequester.editUserProfile(
                user,
                this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Log.i(TAG, "The job was edited successfully!");
                        showAllJobsFragment();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        user.jobs.remove(updatedJob);
                        user.jobs.add(previousJob);
                        showAllJobsFragment();
                    }
                }
        );
    }

    @Override
    public void onJobDeleted(final UserJob job) {
        showDeleteJobConfirmationDialog(job);
    }

    private void showDeleteJobConfirmationDialog(final UserJob job) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.are_you_sure_you_want_to_delete_job))
                .setMessage(getString(R.string.this_change_cannot_be_reverted))
                .setCancelable(true)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        confirmJobDeletion(job);
                    }
                })
                .setNegativeButton(android.R.string.no, null);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            }
        });
        dialog.show();
    }

    private void confirmJobDeletion(final UserJob job) {
        Log.d(TAG, "User job to delete: " + new Gson().toJson(job));
        user.jobs.remove(job);
        LincedInRequester.editUserProfile(
                user,
                this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Log.i(TAG, "The job was deleted successfully!");
                        showAllJobsFragment();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        user.jobs.add(job);
                        showAllJobsFragment();
                    }
                }
        );
    }
}
