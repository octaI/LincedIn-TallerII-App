package com.fiuba.tallerii.lincedin.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.events.DatePickedEvent;
import com.fiuba.tallerii.lincedin.model.user.UserEducation;
import com.fiuba.tallerii.lincedin.utils.DateUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditEducationFragment extends Fragment {

    public interface EditEducationFragmentListener {
        void onNewEducationAdded(UserEducation education);
        void onEducationEdited(UserEducation previousEducation, UserEducation updatedEducation);
        void onEducationDeleted(UserEducation education);
    }

    private static final String TAG = EditEducationFragment.class.getName();

    public static final String ARG_SELECTED_EDUCATION = "SELECTED_EDUCATION";
    private UserEducation selectedEducation = null;

    private static final String SINCE_DATE = "sinceDate";
    private static final String UNTIL_DATE = "untilDate";
    private String lastDatePickerClicked;

    public EditEducationFragment() {}

    public static EditEducationFragment newInstance(UserEducation education) {
        EditEducationFragment fragment = new EditEducationFragment();
        if (education != null) {
            Bundle args = new Bundle();
            args.putString(ARG_SELECTED_EDUCATION, new Gson().toJson(education));
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
            String educationJson = getArguments().getString(ARG_SELECTED_EDUCATION);
            if (educationJson != null) {
                selectedEducation = new Gson().fromJson(educationJson, UserEducation.class);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_education, container, false);

        if (selectedEducation != null) {
            ((TextView) v.findViewById(R.id.edit_education_title_textview)).setText(getString(R.string.edit_education));
            v.findViewById(R.id.edit_education_delete_button).setVisibility(View.VISIBLE);
        } else {
            ((TextView) v.findViewById(R.id.edit_education_title_textview)).setText(getString(R.string.add_new_education));
            v.findViewById(R.id.edit_education_delete_button).setVisibility(View.GONE);
        }

        setListeners(v);

        return v;
    }

    private void setListeners(View v) {
        setDeleteEducationButtonListener(v);
        setDatePickersListeners(v);
        setCheckboxListener(v);
        setApplyChangesButtonListener(v);
    }

    private void setDeleteEducationButtonListener(View v) {
        v.findViewById(R.id.edit_education_delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditEducationFragmentListener) getActivity()).onEducationDeleted(selectedEducation);
            }
        });
    }

    private void setDatePickersListeners(View v) {
        v.findViewById(R.id.edit_education_since_date_edittext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastDatePickerClicked = SINCE_DATE;
                openDatePickerDialog();
            }
        });

        v.findViewById(R.id.edit_education_until_date_edittext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastDatePickerClicked = UNTIL_DATE;
                openDatePickerDialog();
            }
        });
    }

    private void setCheckboxListener(final View parentView) {
        parentView.findViewById(R.id.edit_education_current_education_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    parentView.findViewById(R.id.edit_education_until_date_edittext).setVisibility(View.GONE);
                    parentView.findViewById(R.id.edit_education_since_to_dates_separator).setVisibility(View.GONE);
                } else {
                    parentView.findViewById(R.id.edit_education_until_date_edittext).setVisibility(View.VISIBLE);
                    parentView.findViewById(R.id.edit_education_since_to_dates_separator).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setApplyChangesButtonListener(final View parentView) {
        if (selectedEducation != null) {
            parentView.findViewById(R.id.edit_education_apply_changes_fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateInput(parentView)) {
                        UserEducation education = buildEducationFromInput(parentView);
                        ((EditEducationFragmentListener) getActivity()).onEducationEdited(selectedEducation, education);
                    }
                }
            });
        } else {
            parentView.findViewById(R.id.edit_education_apply_changes_fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateInput(parentView)) {
                        UserEducation education = buildEducationFromInput(parentView);
                        ((EditEducationFragmentListener) getActivity()).onNewEducationAdded(education);
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
        if (((EditText) v.findViewById(R.id.edit_education_degree_edittext)).getText().toString().equals("")) {
            v.findViewById(R.id.edit_education_degree_edittext).requestFocus();
            ((EditText) v.findViewById(R.id.edit_education_degree_edittext)).setError(getString(R.string.field_is_required));
            return false;
        }

        if (((EditText) v.findViewById(R.id.edit_education_school_edittext)).getText().toString().equals("")) {
            v.findViewById(R.id.edit_education_school_edittext).requestFocus();
            ((EditText) v.findViewById(R.id.edit_education_school_edittext)).setError(getString(R.string.field_is_required));
            return false;
        }

        return validateInputDates(v);
    }

    private boolean validateInputDates(View v) {
        EditText sinceDateEditText = ((EditText) v.findViewById(R.id.edit_education_since_date_edittext));
        EditText untilDateEditText = ((EditText) v.findViewById(R.id.edit_education_until_date_edittext));

        if (sinceDateEditText.getText().toString().equals("")) {
            Snackbar.make(v, getString(R.string.must_spicify_start_date), Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if ( !((CheckBox) v.findViewById(R.id.edit_education_current_education_checkbox)).isChecked() ) {
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

    private UserEducation buildEducationFromInput(View v) {
        UserEducation education = new UserEducation();

        education.degree = ((EditText) v.findViewById(R.id.edit_education_degree_edittext)).getText().toString();
        education.schoolName = ((EditText) v.findViewById(R.id.edit_education_school_edittext)).getText().toString();

        education.startDate = DateUtils.parseDateWithoutTimeToDatetime(((EditText) v.findViewById(R.id.edit_education_since_date_edittext)).getText().toString());
        education.endDate = ((CheckBox) v.findViewById(R.id.edit_education_current_education_checkbox)).isChecked() ?
                ""
                : DateUtils.parseDateWithoutTimeToDatetime(((EditText) v.findViewById(R.id.edit_education_until_date_edittext)).getText().toString());

        return education;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDatePicked(DatePickedEvent event) {
        if (lastDatePickerClicked != null) {
            if (lastDatePickerClicked.equals(SINCE_DATE)) {
                EditText sinceDateEditText = (EditText) getView().findViewById(R.id.edit_education_since_date_edittext);
                if (sinceDateEditText != null) {
                    String sinceDate = DateUtils.parseToDatetime(event.day, event.month, event.year);
                    sinceDateEditText.setText(DateUtils.parseDatetimeToDateWithoutTime(sinceDate));
                }
            } else if (lastDatePickerClicked.equals(UNTIL_DATE)) {
                EditText untilDateEditText = (EditText) getView().findViewById(R.id.edit_education_until_date_edittext);
                if (untilDateEditText != null) {
                    String untilDate = DateUtils.parseToDatetime( event.day, event.month, event.year);
                    untilDateEditText.setText(DateUtils.parseDatetimeToDateWithoutTime(untilDate));
                }
            }
        }
    }
}
