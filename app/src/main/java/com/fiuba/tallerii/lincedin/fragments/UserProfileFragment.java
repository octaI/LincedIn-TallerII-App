package com.fiuba.tallerii.lincedin.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.adapters.UserSkillsAdapter;
import com.fiuba.tallerii.lincedin.model.UserSkill;

import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends Fragment {

    private UserSkillsAdapter userSkillsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userSkillsAdapter = new UserSkillsAdapter(getContext());

        requestUserProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);

        setDummySkill(v);

        // TODO: 03/11/16 Hide loading and show container after callback.
        setLoading(v, false);

        return v;
    }

    // TODO: 03/11/16 Delete this!
    @Deprecated
    private void setDummySkill(View v) {
        UserSkill dummySkill = new UserSkill("Java", "Software", "Basics of Java programming language");
        List<UserSkill> skills = new ArrayList<>();
        skills.add(dummySkill);
        userSkillsAdapter.setDataset(skills);
        userSkillsAdapter.notifyDataSetChanged();
        ((ListView) v.findViewById(R.id.user_profile_skills_listview)).setAdapter(userSkillsAdapter);
    }

    private void requestUserProfile() {
        // TODO: 03/11/16 Implement!
    }

    private void setLoading(View v, boolean loading) {
        if (loading) {
            v.findViewById(R.id.user_profile_main_container_nestedscrollview).setVisibility(View.INVISIBLE);
            v.findViewById(R.id.user_profile_loading_circular_progress).setVisibility(View.VISIBLE);
        } else {
            v.findViewById(R.id.user_profile_main_container_nestedscrollview).setVisibility(View.VISIBLE);
            v.findViewById(R.id.user_profile_loading_circular_progress).setVisibility(View.GONE);
        }
    }

}
