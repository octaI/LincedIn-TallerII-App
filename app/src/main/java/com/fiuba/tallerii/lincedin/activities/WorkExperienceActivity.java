package com.fiuba.tallerii.lincedin.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.fragments.AddJobFragment;
import com.fiuba.tallerii.lincedin.fragments.AllJobsFragment;
import com.fiuba.tallerii.lincedin.model.user.UserJob;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WorkExperienceActivity extends AppCompatActivity {

    private static final String TAG = "WorkExperience";

    public static final String ARG_JOBS = "JOBS";
    public static final String ARG_IS_OWN_PROFILE = "IS_OWN_PROFILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_experience);
        setToolbar();

        showAllJobsFragment();
        setButtonsVisibility();
        setListeners();
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

    private List<UserJob> getAllJobs() {
        String jobsJson = getIntent().getStringExtra(ARG_JOBS);
        Type jobListType = new TypeToken<List<UserJob>>() {
        }.getType();
        List<UserJob> jobs = new ArrayList<>();
        if (jobsJson != null) {
            jobs = new Gson().fromJson(jobsJson, jobListType);
        }
        return jobs;
    }

    private void setButtonsVisibility() {
        if (getIntent().getBooleanExtra(ARG_IS_OWN_PROFILE, false)) {
            findViewById(R.id.work_experience_add_job_fab).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.work_experience_add_job_fab).setVisibility(View.GONE);
        }
    }

    private void setListeners() {
        // TODO: 09/11/16 onLongClickListener to edit job
        findViewById(R.id.work_experience_add_job_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddJobFragment();
            }
        });
    }

    private void showAllJobsFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.work_experience_container_framelayout, AllJobsFragment.newInstance(getAllJobs()));
        transaction.addToBackStack("AllJobsFragment");
        transaction.commit();
    }

    private void showAddJobFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.work_experience_container_framelayout, new AddJobFragment());
        transaction.addToBackStack("AddJobFragment");
        transaction.commit();
    }
}
