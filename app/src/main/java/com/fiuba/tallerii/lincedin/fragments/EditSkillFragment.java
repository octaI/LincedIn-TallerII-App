package com.fiuba.tallerii.lincedin.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.adapters.SkillsSpinnerAdapter;
import com.fiuba.tallerii.lincedin.model.user.UserSkill;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditSkillFragment extends Fragment {

    public interface EditSkillFragmentListener {
        void onNewSkillAdded(UserSkill skill);
        void onSkillEdited(UserSkill previousSkill, UserSkill updatedSkill);
        void onSkillDeleted(UserSkill skill);
    }

    private static final String TAG = EditSkillFragment.class.getName();

    public static final String ARG_SELECTED_SKILL = "SELECTED_SKILL";
    private UserSkill selectedSkill = null;

    public EditSkillFragment() {}

    public static EditSkillFragment newInstance(UserSkill skill) {
        EditSkillFragment fragment = new EditSkillFragment();
        if (skill != null) {
            Bundle args = new Bundle();
            args.putString(ARG_SELECTED_SKILL, new Gson().toJson(skill));
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrieveSelectedSkill();
    }

    private void retrieveSelectedSkill() {
        if (getArguments() != null) {
            String skillJson = getArguments().getString(ARG_SELECTED_SKILL);
            if (skillJson != null) {
                selectedSkill = new Gson().fromJson(skillJson, UserSkill.class);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_skill, container, false);

        if (selectedSkill != null) {
            ((TextView) v.findViewById(R.id.edit_skill_title_textview)).setText(getString(R.string.edit_skill));
            v.findViewById(R.id.edit_skill_delete_button).setVisibility(View.VISIBLE);
        } else {
            ((TextView) v.findViewById(R.id.edit_skill_title_textview)).setText(getString(R.string.add_new_skill));
            v.findViewById(R.id.edit_skill_delete_button).setVisibility(View.GONE);
        }

        populateSkillsSpinner(v);
        setListeners(v);

        return v;
    }

    private void populateSkillsSpinner(final View v) {
        LincedInRequester.getAllSkills(
                getContext(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Log.d(TAG, gson.toJson(response));

                        Type skillListType = new TypeToken<List<UserSkill>>() {}.getType();
                        List<UserSkill> skills = new ArrayList<>();
                        try {
                            skills = gson.fromJson(response.getJSONArray("skills").toString(), skillListType);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setSkillsSpinnerAdapter(v, skills);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        error.printStackTrace();
                    }
                }
        );
    }

    private void setSkillsSpinnerAdapter(View v, List<UserSkill> skills) {
        ((android.support.v7.widget.AppCompatSpinner) v.findViewById(R.id.edit_skills_dropdown))
                .setAdapter(new SkillsSpinnerAdapter(getContext(), skills));
    }

    private void setListeners(View v) {
        setDeleteSkillButtonListener(v);
        setSkillsSpinnerListeners(v);
        setApplyChangesButtonListener(v);
    }

    private void setDeleteSkillButtonListener(View v) {
        v.findViewById(R.id.edit_skill_delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditSkillFragmentListener) getActivity()).onSkillDeleted(selectedSkill);
            }
        });
    }

    private void setSkillsSpinnerListeners(final View v) {
        ((android.support.v7.widget.AppCompatSpinner) v.findViewById(R.id.edit_skills_dropdown))
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if (position == 0) {
                            v.findViewById(R.id.edit_skill_description_textview).setVisibility(View.GONE);
                        } else {
                            v.findViewById(R.id.edit_skill_description_textview).setVisibility(View.VISIBLE);

                            ((TextView) v.findViewById(R.id.edit_skill_description_textview))
                                    .setText(
                                            ((UserSkill) ((AppCompatSpinner) v.findViewById(R.id.edit_skills_dropdown)).getAdapter().getItem(position)).description
                                    );
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {}

                });
    }

    private void setApplyChangesButtonListener(final View parentView) {
        if (selectedSkill != null) {
            parentView.findViewById(R.id.edit_skill_apply_changes_fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateInput(parentView)) {
                        UserSkill skill = buildSkillFromInput(parentView);
                        ((EditSkillFragmentListener) getActivity()).onSkillEdited(selectedSkill, skill);
                    }
                }
            });
        } else {
            parentView.findViewById(R.id.edit_skill_apply_changes_fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateInput(parentView)) {
                        UserSkill skill = buildSkillFromInput(parentView);
                        ((EditSkillFragmentListener) getActivity()).onNewSkillAdded(skill);
                    }
                }
            });
        }
    }

    private boolean validateInput(View v) {
        if (((android.support.v7.widget.AppCompatSpinner) v.findViewById(R.id.edit_skills_dropdown)).getChildCount() == 0
                || ((android.support.v7.widget.AppCompatSpinner) v.findViewById(R.id.edit_skills_dropdown)).getSelectedItemPosition() == 0) {
            Snackbar.make(v, getString(R.string.must_select_skill), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private UserSkill buildSkillFromInput(View v) {
        return (UserSkill) ((android.support.v7.widget.AppCompatSpinner) v.findViewById(R.id.edit_skills_dropdown)).getSelectedItem();
    }

}
