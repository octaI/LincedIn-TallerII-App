package com.fiuba.tallerii.lincedin.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.events.DatePickedEvent;
import com.fiuba.tallerii.lincedin.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AddJobFragment extends Fragment {

    private static final String TAG = AddJobFragment.class.getName();

    private static final String SINCE_DATE = "sinceDate";
    private static final String UNTIL_DATE = "untilDate";
    private String lastDatePickerClicked;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_job, container, false);

        setListeners(v);

        return v;
    }

    private void setListeners(View v) {
        setDatePickersListeners(v);
        setCheckboxListener(v);
    }

    private void setDatePickersListeners(View v) {
        v.findViewById(R.id.dialog_edit_job_since_date_edittext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastDatePickerClicked = SINCE_DATE;
                openDatePickerDialog();
            }
        });

        v.findViewById(R.id.dialog_edit_job_until_date_edittext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastDatePickerClicked = UNTIL_DATE;
                openDatePickerDialog();
            }
        });
    }

    private void setCheckboxListener(final View parentView) {
        parentView.findViewById(R.id.dialog_edit_job_current_work_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    parentView.findViewById(R.id.dialog_edit_job_until_date_edittext).setVisibility(View.GONE);
                } else {
                    parentView.findViewById(R.id.dialog_edit_job_until_date_edittext).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void openDatePickerDialog() {
        DialogFragment datePickerDialog = new DatePickerDialogFragment();
        datePickerDialog.show(getActivity().getSupportFragmentManager(), "DatePickerDialogFragment");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDatePicked(DatePickedEvent event) {
        if (lastDatePickerClicked != null) {
            if (lastDatePickerClicked.equals(SINCE_DATE)) {
                EditText sinceDateEditText = (EditText) getView().findViewById(R.id.dialog_edit_job_since_date_edittext);
                if (sinceDateEditText != null) {
                    String sinceDate = DateUtils.parseToLocalDate(getContext(), event.day, event.month, event.year);
                    sinceDateEditText.setText(sinceDate);
                }
            } else if (lastDatePickerClicked.equals(UNTIL_DATE)) {
                EditText untilDateEditText = (EditText) getView().findViewById(R.id.dialog_edit_job_until_date_edittext);
                if (untilDateEditText != null) {
                    String untilDate = DateUtils.parseToLocalDate(getContext(), event.day, event.month, event.year);
                    untilDateEditText.setText(untilDate);
                }
            }
        }

    }
}
