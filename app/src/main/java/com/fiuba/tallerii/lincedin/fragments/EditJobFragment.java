package com.fiuba.tallerii.lincedin.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.adapters.JobPositionsSpinnerAdapter;
import com.fiuba.tallerii.lincedin.events.DatePickedEvent;
import com.fiuba.tallerii.lincedin.model.user.UserJob;
import com.fiuba.tallerii.lincedin.model.user.UserJobPosition;
import com.fiuba.tallerii.lincedin.network.HttpRequestHelper;
import com.fiuba.tallerii.lincedin.utils.DateUtils;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils.getStringFromSharedPreferences;

public class EditJobFragment extends Fragment {

    public interface EditJobFragmentListener {
        void onNewJobAdded(UserJob job);
        void onJobEdited(UserJob previousJob, UserJob updatedJob);
        void onJobDeleted(UserJob job);
    }

    private static final String TAG = EditJobFragment.class.getName();

    public static final String ARG_SELECTED_JOB = "SELECTED_JOB";
    private UserJob selectedJob = null;

    private static final String SINCE_DATE = "sinceDate";
    private static final String UNTIL_DATE = "untilDate";
    private String lastDatePickerClicked;

    public EditJobFragment() {}

    public static EditJobFragment newInstance(UserJob job) {
        EditJobFragment fragment = new EditJobFragment();
        if (job != null) {
            Bundle args = new Bundle();
            args.putString(ARG_SELECTED_JOB, new Gson().toJson(job));
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        retrieveSelectedJob();
    }

    private void retrieveSelectedJob() {
        if (getArguments() != null) {
            String jobJson = getArguments().getString(ARG_SELECTED_JOB);
            if (jobJson != null) {
                selectedJob = new Gson().fromJson(jobJson, UserJob.class);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_job, container, false);

        if (selectedJob != null) {
            ((TextView) v.findViewById(R.id.edit_job_title_textview)).setText(getString(R.string.edit_job));
            v.findViewById(R.id.edit_job_delete_button).setVisibility(View.VISIBLE);
        } else {
            ((TextView) v.findViewById(R.id.edit_job_title_textview)).setText(getString(R.string.add_new_job));
            v.findViewById(R.id.edit_job_delete_button).setVisibility(View.GONE);
        }

        populatePositionSpinner(v);
        setListeners(v);

        return v;
    }

    private void populatePositionSpinner(final View v) {
        final Map<String, String> requestParams = new HashMap<>();
        final String url = "http://"
                + getStringFromSharedPreferences(getContext(), SharedPreferencesKeys.SERVER_IP, HTTPConfigurationDialogFragment.DEFAULT_SERVER_IP)
                + ":" + getStringFromSharedPreferences(getContext(), SharedPreferencesKeys.SERVER_PORT, HTTPConfigurationDialogFragment.DEFAULT_PORT_EXPOSED)
                + "/shared"
                + "/job_positions";
        HttpRequestHelper.get(
                url,
                requestParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Log.d(TAG, gson.toJson(response));

                        Type jobPositionListType = new TypeToken<List<UserJobPosition>>() {}.getType();
                        List<UserJobPosition> jobPositions = new ArrayList<>();
                        try {
                            jobPositions = gson.fromJson(response.getJSONArray("job_positions").toString(), jobPositionListType);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setJobPositionsSpinnerAdapter(v, jobPositions);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        error.printStackTrace();
                    }
                },
                "UserJobPositionsRequest"
        );
    }

    private void setJobPositionsSpinnerAdapter(View v, List<UserJobPosition> jobPositions) {
        ((android.support.v7.widget.AppCompatSpinner) v.findViewById(R.id.edit_job_positions_dropdown))
                .setAdapter(new JobPositionsSpinnerAdapter(getContext(), jobPositions));
    }

    private void setListeners(View v) {
        setDeleteJobButtonListener(v);
        setJobPositionsSpinnerListeners(v);
        setDatePickersListeners(v);
        setCheckboxListener(v);
        setApplyChangesButtonListener(v);
    }

    private void setDeleteJobButtonListener(View v) {
        v.findViewById(R.id.edit_job_delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditJobFragmentListener) getActivity()).onJobDeleted(selectedJob);
            }
        });
    }

    private void setJobPositionsSpinnerListeners(final View v) {
        ((android.support.v7.widget.AppCompatSpinner) v.findViewById(R.id.edit_job_positions_dropdown))
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    v.findViewById(R.id.edit_job_position_description_textview).setVisibility(View.GONE);
                } else {
                    v.findViewById(R.id.edit_job_position_description_textview).setVisibility(View.VISIBLE);

                    ((TextView) v.findViewById(R.id.edit_job_position_description_textview))
                            .setText(
                                    ((UserJobPosition) ((AppCompatSpinner) v.findViewById(R.id.edit_job_positions_dropdown)).getAdapter().getItem(position)).description
                            );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}

        });
    }

    private void setDatePickersListeners(View v) {
        v.findViewById(R.id.edit_job_since_date_edittext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastDatePickerClicked = SINCE_DATE;
                openDatePickerDialog();
            }
        });

        v.findViewById(R.id.edit_job_until_date_edittext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastDatePickerClicked = UNTIL_DATE;
                openDatePickerDialog();
            }
        });
    }

    private void setCheckboxListener(final View parentView) {
        parentView.findViewById(R.id.edit_job_current_work_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    parentView.findViewById(R.id.edit_job_until_date_edittext).setVisibility(View.GONE);
                    parentView.findViewById(R.id.edit_job_since_to_dates_separator).setVisibility(View.GONE);
                } else {
                    parentView.findViewById(R.id.edit_job_until_date_edittext).setVisibility(View.VISIBLE);
                    parentView.findViewById(R.id.edit_job_since_to_dates_separator).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setApplyChangesButtonListener(final View parentView) {
        if (selectedJob != null) {
            parentView.findViewById(R.id.edit_job_apply_changes_fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateInput(parentView)) {
                        UserJob job = buildJobFromInput(parentView);
                        ((EditJobFragmentListener) getActivity()).onJobEdited(selectedJob, job);
                    }
                }
            });
        } else {
            parentView.findViewById(R.id.edit_job_apply_changes_fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateInput(parentView)) {
                        UserJob job = buildJobFromInput(parentView);
                        ((EditJobFragmentListener) getActivity()).onNewJobAdded(job);
                    }
                }
            });
        }
    }

    private void openDatePickerDialog() {
        DialogFragment datePickerDialog = new DatePickerDialogFragment();
        datePickerDialog.show(getActivity().getSupportFragmentManager(), "DatePickerDialogFragment");
    }

    private boolean validateInput(View v) {
        if (((EditText) v.findViewById(R.id.edit_job_company_edittext)).getText().toString().equals("")) {
            v.findViewById(R.id.edit_job_company_edittext).requestFocus();
            ((EditText) v.findViewById(R.id.edit_job_company_edittext)).setError(getString(R.string.field_is_required));
            return false;
        }

        if (((android.support.v7.widget.AppCompatSpinner) v.findViewById(R.id.edit_job_positions_dropdown)).getSelectedItemPosition() == 0) {
            Snackbar.make(v, getString(R.string.must_select_job_position), Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return validateInputDates(v);
    }

    private boolean validateInputDates(View v) {
        EditText sinceDateEditText = ((EditText) v.findViewById(R.id.edit_job_since_date_edittext));
        EditText untilDateEditText = ((EditText) v.findViewById(R.id.edit_job_until_date_edittext));

        if (sinceDateEditText.getText().toString().equals("")) {
            Snackbar.make(v, getString(R.string.must_spicify_start_date), Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if ( !((CheckBox) v.findViewById(R.id.edit_job_current_work_checkbox)).isChecked() ) {
            if (untilDateEditText.getText().toString().equals("")) {
                Snackbar.make(v, getString(R.string.must_spicify_end_date), Snackbar.LENGTH_SHORT).show();
                return false;
            }

            if (sinceDateEditText.getText().toString().compareTo(untilDateEditText.getText().toString()) > 0) {
                sinceDateEditText.requestFocus();
                Snackbar.make(v, getString(R.string.start_date_after_end_date), Snackbar.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private UserJob buildJobFromInput(View v) {
        UserJob job = new UserJob();

        job.company = ((EditText) v.findViewById(R.id.edit_job_company_edittext)).getText().toString();

        job.position = (UserJobPosition) ((android.support.v7.widget.AppCompatSpinner) v.findViewById(R.id.edit_job_positions_dropdown)).getSelectedItem();

        job.since = DateUtils.parseDateWithoutTimeToDatetime(((EditText) v.findViewById(R.id.edit_job_since_date_edittext)).getText().toString());
        job.to = ((CheckBox) v.findViewById(R.id.edit_job_current_work_checkbox)).isChecked() ?
                ""
                : DateUtils.parseDateWithoutTimeToDatetime(((EditText) v.findViewById(R.id.edit_job_until_date_edittext)).getText().toString());

        return job;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDatePicked(DatePickedEvent event) {
        if (lastDatePickerClicked != null) {
            if (lastDatePickerClicked.equals(SINCE_DATE)) {
                EditText sinceDateEditText = (EditText) getView().findViewById(R.id.edit_job_since_date_edittext);
                if (sinceDateEditText != null) {
                    String sinceDate = DateUtils.parseToDatetime(event.day, event.month, event.year);
                    sinceDateEditText.setText(DateUtils.parseDatetimeToDateWithoutTime(sinceDate));
                }
            } else if (lastDatePickerClicked.equals(UNTIL_DATE)) {
                EditText untilDateEditText = (EditText) getView().findViewById(R.id.edit_job_until_date_edittext);
                if (untilDateEditText != null) {
                    String untilDate = DateUtils.parseToDatetime( event.day, event.month, event.year);
                    untilDateEditText.setText(DateUtils.parseDatetimeToDateWithoutTime(untilDate));
                }
            }
        }
    }
}
