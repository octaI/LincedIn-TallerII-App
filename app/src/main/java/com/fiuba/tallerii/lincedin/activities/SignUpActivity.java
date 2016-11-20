package com.fiuba.tallerii.lincedin.activities;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.events.DatePickedEvent;
import com.fiuba.tallerii.lincedin.fragments.DatePickerDialogFragment;
import com.fiuba.tallerii.lincedin.utils.DateUtils;
import com.fiuba.tallerii.lincedin.utils.InputValidationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        EventBus.getDefault().register(this);

        setToolbar();
        setListeners();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_signup);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setListeners() {
        setBirthdayFieldListener();
        setSubmitButtonListener();
    }

    private void setBirthdayFieldListener() {
        findViewById(R.id.signup_date_of_birth_edittext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBirthdayDatePickerDialog();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDatePicked(DatePickedEvent event) {
        EditText dateOfBirthEditText = (EditText) findViewById(R.id.signup_date_of_birth_edittext);
        if (dateOfBirthEditText != null) {
            String dateOfBirth = DateUtils.parseToDatetime(event.day, event.month, event.year);
            dateOfBirthEditText.setText(DateUtils.parseDatetimeToDateWithoutTime(dateOfBirth));
        }
    }

    private void openBirthdayDatePickerDialog() {
        DialogFragment datePickerDialog = new DatePickerDialogFragment();
        datePickerDialog.show(getSupportFragmentManager(), "DatePickerDialogFragment");
    }

    private void setSubmitButtonListener() {
        findViewById(R.id.signup_submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateUserInput()) {
                    createUserAccount();
                }
            }
        });
    }

    private boolean validateUserInput() {
        EditText firstNameEditText = (EditText) findViewById(R.id.signup_first_name_edittext);
        EditText lastNameEditText = (EditText) findViewById(R.id.signup_last_name_edittext);
        EditText dateOfBirthEditText = (EditText) findViewById(R.id.signup_date_of_birth_edittext);
        EditText emailEditText = (EditText) findViewById(R.id.signup_email_edittext);
        EditText passwordEditText = (EditText) findViewById(R.id.signup_password_edittext);
        EditText confirmationPasswordEditText = (EditText) findViewById(R.id.signup_confirmation_password_edittext);

        return InputValidationUtils.validateThatAllFieldsAreFilled(this, firstNameEditText, lastNameEditText, dateOfBirthEditText, emailEditText, passwordEditText, confirmationPasswordEditText)
                && InputValidationUtils.validateEmail(this, emailEditText)
                && InputValidationUtils.validatePasswords(this, passwordEditText, confirmationPasswordEditText);
    }

    private void createUserAccount() {

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
}
