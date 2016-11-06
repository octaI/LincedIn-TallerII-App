package com.fiuba.tallerii.lincedin.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.adapters.WorkExperienceAdapter;
import com.fiuba.tallerii.lincedin.model.user.UserJob;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class WorkExperienceActivity extends AppCompatActivity {

    private static final String TAG = "WorkExperience";

    public static final String ARG_JOBS = "JOBS";

    private WorkExperienceAdapter workExperienceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_experience);
        setToolbar();
        setListeners();
        setAdapter();
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

    private void setListeners() {
        // TODO: 06/11/16 Implement dialog to add new job to work experience.
    }

    private void setAdapter() {
        String jobsJson = getIntent().getStringExtra(ARG_JOBS);
        Type jobListType = new TypeToken<List<UserJob>>(){}.getType();
        List<UserJob> jobs = new Gson().fromJson(jobsJson, jobListType);
        workExperienceAdapter = new WorkExperienceAdapter(this, jobs);
        ((ListView) findViewById(R.id.work_experience_listview)).setAdapter(workExperienceAdapter);
    }
}
