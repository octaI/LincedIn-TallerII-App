package com.fiuba.tallerii.lincedin.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.adapters.AllSkillsAdapter;
import com.fiuba.tallerii.lincedin.model.user.UserSkill;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllSkillsFragment extends Fragment {

    public interface AllSkillsFragmentListener {
        void onAddSkillButtonPressed();
        void onSkillRowClicked(UserSkill skill);
    }

    private static final String ARG_SKILLS = "SKILLS";
    private static final String ARG_IS_OWN_PROFILE = "IS_OWN_PROFILE";

    private AllSkillsAdapter allSkillsAdapter;

    public AllSkillsFragment() {}

    public static AllSkillsFragment newInstance(List<UserSkill> skill, boolean isOwnProfile) {
        AllSkillsFragment fragment = new AllSkillsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SKILLS, new Gson().toJson(skill));
        args.putBoolean(ARG_IS_OWN_PROFILE, isOwnProfile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_skills, container, false);
        setAdapter(v);
        setListeners(v);
        setButtonVisibility(v);
        return v;
    }

    private void setAdapter(View v) {
        if (getArguments() != null) {
            String skillsJson = getArguments().getString(ARG_SKILLS);
            Type skillListType = new TypeToken<List<UserSkill>>() {
            }.getType();
            List<UserSkill> skills = new ArrayList<>();
            if (skillsJson != null) {
                skills = new Gson().fromJson(skillsJson, skillListType);
            }
            allSkillsAdapter = new AllSkillsAdapter(getContext(), skills);
            ((ListView) v.findViewById(R.id.fragment_all_skills_listview)).setAdapter(allSkillsAdapter);
        }
    }

    private void setListeners(View v) {
        setAddSkillButtonListener(v);
        setSkillRowOnLongClickListener(v);
    }

    private void setButtonVisibility(View v) {
        if (getArguments() != null) {
            if (getArguments().getBoolean(ARG_IS_OWN_PROFILE, false)) {
                v.findViewById(R.id.all_skills_add_skill_fab).setVisibility(View.VISIBLE);
            } else {
                v.findViewById(R.id.all_skills_add_skill_fab).setVisibility(View.GONE);
            }
        }
    }

    private void setAddSkillButtonListener(View parentView) {
        parentView.findViewById(R.id.all_skills_add_skill_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AllSkillsFragmentListener) getActivity()).onAddSkillButtonPressed();
            }
        });
    }

    private void setSkillRowOnLongClickListener(View v) {
        if (getArguments() != null) {
            if (getArguments().getBoolean(ARG_IS_OWN_PROFILE, false)) {
                ((ListView) v.findViewById(R.id.fragment_all_skills_listview)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((AllSkillsFragmentListener) getActivity()).onSkillRowClicked(allSkillsAdapter.getItem(position));
                    }
                });
            }
        }
    }

}
