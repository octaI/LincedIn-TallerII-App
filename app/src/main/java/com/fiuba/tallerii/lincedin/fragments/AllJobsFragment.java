package com.fiuba.tallerii.lincedin.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    public interface AllJobsFragmentListener {
        void onAddJobButtonPressed();
        void onJobRowClicked(UserJob job);
    }

    private static final String ARG_JOBS = "JOBS";
    private static final String ARG_IS_OWN_PROFILE = "IS_OWN_PROFILE";

    private AllJobsAdapter allJobsAdapter;

    public AllJobsFragment() {}

    public static AllJobsFragment newInstance(List<UserJob> jobs, boolean isOwnProfile) {
        AllJobsFragment fragment = new AllJobsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_JOBS, new Gson().toJson(jobs));
        args.putBoolean(ARG_IS_OWN_PROFILE, isOwnProfile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_jobs, container, false);
        setAdapter(v);
        setListeners(v);
        setButtonVisibility(v);
        return v;
    }

    private void setAdapter(View v) {
        if (getArguments() != null) {
            String jobsJson = getArguments().getString(ARG_JOBS);
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

    private void setListeners(View v) {
        setAddJobButtonListener(v);
        setJobRowOnLongClickListener(v);
    }

    private void setButtonVisibility(View v) {
        if (getArguments() != null) {
            if (getArguments().getBoolean(ARG_IS_OWN_PROFILE, false)) {
                v.findViewById(R.id.all_jobs_add_job_fab).setVisibility(View.VISIBLE);
            } else {
                v.findViewById(R.id.all_jobs_add_job_fab).setVisibility(View.GONE);
            }
        }
    }

    private void setAddJobButtonListener(View parentView) {
        parentView.findViewById(R.id.all_jobs_add_job_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AllJobsFragmentListener) getActivity()).onAddJobButtonPressed();
            }
        });
    }

    private void setJobRowOnLongClickListener(View v) {
        if (getArguments() != null) {
            if (getArguments().getBoolean(ARG_IS_OWN_PROFILE, false)) {
                ((ListView) v.findViewById(R.id.fragment_all_jobs_listview)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((AllJobsFragmentListener) getActivity()).onJobRowClicked(allJobsAdapter.getItem(position));
                    }
                });
            }
        }
    }

}
