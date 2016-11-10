package com.fiuba.tallerii.lincedin.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.activities.WorkExperienceActivity;
import com.fiuba.tallerii.lincedin.adapters.AllJobsAdapter;
import com.fiuba.tallerii.lincedin.model.user.UserJob;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllJobsFragment extends Fragment {

    private AllJobsAdapter allJobsAdapter;

    public AllJobsFragment() {}

    public static AllJobsFragment newInstance(List<UserJob> jobs) {
        AllJobsFragment fragment = new AllJobsFragment();
        Bundle args = new Bundle();
        args.putString(WorkExperienceActivity.ARG_JOBS, new Gson().toJson(jobs));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_jobs, container, false);
        setAdapter(v);
        return v;
    }

    private void setAdapter(View v) {
        if (getArguments() != null) {
            String jobsJson = getArguments().getString(WorkExperienceActivity.ARG_JOBS);
            Type jobListType = new TypeToken<List<UserJob>>() {
            }.getType();
            List<UserJob> jobs = new ArrayList<>();
            if (jobsJson != null) {
                jobs = new Gson().fromJson(jobsJson, jobListType);
            }
            allJobsAdapter = new AllJobsAdapter(getContext(), jobs);
            ((ListView) v.findViewById(R.id.fragment_all_jobs_listview)).setAdapter(allJobsAdapter);
        }
    }

}
