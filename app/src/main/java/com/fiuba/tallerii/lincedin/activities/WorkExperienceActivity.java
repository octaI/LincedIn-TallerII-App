package com.fiuba.tallerii.lincedin.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.fragments.AddJobFragment;
import com.fiuba.tallerii.lincedin.fragments.AllJobsFragment;
import com.fiuba.tallerii.lincedin.model.user.UserJob;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WorkExperienceActivity extends AppCompatActivity
        implements AllJobsFragment.AllJobsFragmentListener, AddJobFragment.AddJobFragmentListener {

    private static final String TAG = "WorkExperience";

    public static final String ARG_JOBS = "JOBS";
    public static final String ARG_IS_OWN_PROFILE = "IS_OWN_PROFILE";

    private List<UserJob> jobs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_experience);
        setToolbar();
        getAllJobsFromIntent();
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

    private List<UserJob> getAllJobsFromIntent() {
        String jobsJson = getIntent().getStringExtra(ARG_JOBS);
        Type jobListType = new TypeToken<List<UserJob>>() {}.getType();
        if (jobsJson != null) {
            jobs = new Gson().fromJson(jobsJson, jobListType);
        }
        return jobs;
    }

    private void showAllJobsFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.work_experience_container_framelayout, AllJobsFragment.newInstance(jobs));
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
        // TODO: 10/11/16 Make PUT request!
        Log.i(TAG, "User job to save: " + new Gson().toJson(job));
        jobs.add(job);  // TODO: 10/11/16 Find workaround for when the job is being edited, not created from scratch.
        showAllJobsFragment();
    }
}
