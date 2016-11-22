package com.fiuba.tallerii.lincedin.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.events.DatePickedEvent;
import com.fiuba.tallerii.lincedin.fragments.DatePickerDialogFragment;
import com.fiuba.tallerii.lincedin.model.user.User;
import com.fiuba.tallerii.lincedin.model.user.signup.SignUpUser;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.fiuba.tallerii.lincedin.utils.DateUtils;
import com.fiuba.tallerii.lincedin.utils.InputValidationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    public static final String EXTRA_EMAIL = "email";
    public static final String EXTRA_PASSWORD = "password";

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
        refreshLoadingIndicator(true);
        LincedInRequester.createUser(
                buildUserFromInput(),
                this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        refreshLoadingIndicator(false);
                        onSuccessUserAccountCreation();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e(TAG, "Error on creating new user account: " + error.getLocalizedMessage());
                        refreshLoadingIndicator(false);
                        Snackbar.make(findViewById(R.id.signup_submit_button), R.string.error_create_account, Snackbar.LENGTH_LONG).show();
                    }
                }
        );
    }

    private SignUpUser buildUserFromInput() {
        return new SignUpUser(
                ((EditText) findViewById(R.id.signup_first_name_edittext)).getText().toString(),
                ((EditText) findViewById(R.id.signup_last_name_edittext)).getText().toString(),
                ((EditText) findViewById(R.id.signup_date_of_birth_edittext)).getText().toString(),
                ((EditText) findViewById(R.id.signup_email_edittext)).getText().toString(),
                ((EditText) findViewById(R.id.signup_password_edittext)).getText().toString()
        );
    }

    private void onSuccessUserAccountCreation() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_EMAIL, ((EditText) findViewById(R.id.signup_email_edittext)).getText().toString());
        returnIntent.putExtra(EXTRA_PASSWORD, ((EditText) findViewById(R.id.signup_password_edittext)).getText().toString());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void refreshLoadingIndicator(boolean loading) {
        if (loading) {
            findViewById(R.id.signup_loading_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.signup_loading_layout).setVisibility(View.GONE);
        }
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
