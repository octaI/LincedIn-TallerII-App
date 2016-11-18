package com.fiuba.tallerii.lincedin.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.adapters.AllEducationAdapter;
import com.fiuba.tallerii.lincedin.model.user.UserEducation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllEducationFragment extends Fragment {


    public interface AllEducationFragmentListener {
        void onAddEducationButtonPressed();
        void onEducationRowClicked(UserEducation education);
    }

    private static final String ARG_EDUCATION = "EDUCATION";
    private static final String ARG_IS_OWN_PROFILE = "IS_OWN_PROFILE";

    private AllEducationAdapter allEducationAdapter;

    public AllEducationFragment() {}

    public static AllEducationFragment newInstance(List<UserEducation> education, boolean isOwnProfile) {
        AllEducationFragment fragment = new AllEducationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EDUCATION, new Gson().toJson(education));
        args.putBoolean(ARG_IS_OWN_PROFILE, isOwnProfile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_education, container, false);
        setAdapter(v);
        setListeners(v);
        setButtonVisibility(v);
        return v;
    }

    private void setAdapter(View v) {
        if (getArguments() != null) {
            String educationJson = getArguments().getString(ARG_EDUCATION);
            Type educationListType = new TypeToken<List<UserEducation>>() {
            }.getType();
            List<UserEducation> education = new ArrayList<>();
            if (educationJson != null) {
                education = new Gson().fromJson(educationJson, educationListType);
            }
            allEducationAdapter = new AllEducationAdapter(getContext(), education);
            ((ListView) v.findViewById(R.id.fragment_all_education_listview)).setAdapter(allEducationAdapter);
        }
    }

    private void setListeners(View v) {
        setAddEducationButtonListener(v);
        setEducationRowOnLongClickListener(v);
    }

    private void setButtonVisibility(View v) {
        if (getArguments() != null) {
            if (getArguments().getBoolean(ARG_IS_OWN_PROFILE, false)) {
                v.findViewById(R.id.all_jobs_add_education_fab).setVisibility(View.VISIBLE);
            } else {
                v.findViewById(R.id.all_jobs_add_education_fab).setVisibility(View.GONE);
            }
        }
    }

    private void setAddEducationButtonListener(View parentView) {
        parentView.findViewById(R.id.all_jobs_add_education_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AllEducationFragmentListener) getActivity()).onAddEducationButtonPressed();
            }
        });
    }

    private void setEducationRowOnLongClickListener(View v) {
        if (getArguments() != null) {
            if (getArguments().getBoolean(ARG_IS_OWN_PROFILE, false)) {
                ((ListView) v.findViewById(R.id.fragment_all_education_listview)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((AllEducationFragmentListener) getActivity()).onEducationRowClicked(allEducationAdapter.getItem(position));
                    }
                });
            }
        }
    }

}
