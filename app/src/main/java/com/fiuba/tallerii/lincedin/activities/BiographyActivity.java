package com.fiuba.tallerii.lincedin.activities;

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
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.fiuba.tallerii.lincedin.utils.DateUtils;
import com.fiuba.tallerii.lincedin.utils.InputValidationUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import static com.fiuba.tallerii.lincedin.utils.InputValidationUtils.validateEmail;
import static com.fiuba.tallerii.lincedin.utils.InputValidationUtils.validateThatAllFieldsAreFilled;

public class BiographyActivity extends AppCompatActivity {

    private static final String TAG = BiographyActivity.class.getName();

    public static final String ARG_USER = "USER";

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biography);
        EventBus.getDefault().register(this);
        setToolbar();
        getUserFromIntent();
        setListeners();
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

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_biography);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private User getUserFromIntent() {
        String userJson = getIntent().getStringExtra(ARG_USER);
        if (userJson != null) {
            user = new Gson().fromJson(userJson, User.class);
        }
        return user;
    }

    private void setListeners() {
        setDateOfBirthEditTextListener();
        setApplyChangesFABListener();
    }

    private void setDateOfBirthEditTextListener() {
        findViewById(R.id.biography_date_of_birth_edittext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBirthdayDatePickerDialog();
            }
        });
    }

    private void openBirthdayDatePickerDialog() {
        DialogFragment datePickerDialog = new DatePickerDialogFragment();
        datePickerDialog.show(getSupportFragmentManager(), "DatePickerDialogFragment");
    }

    private void setApplyChangesFABListener() {
        findViewById(R.id.biography_apply_changes_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    editUserBiography(v);
                }
            }
        });
    }

    private boolean validateInput() {
        EditText firstNameEditText = (EditText) findViewById(R.id.biography_firstname_edittext);
        EditText lastNameEditText = (EditText) findViewById(R.id.biography_lastname_edittext);
        EditText dateOfBirthEditText = (EditText) findViewById(R.id.biography_date_of_birth_edittext);
        EditText emailEditText = (EditText) findViewById(R.id.biography_email_edittext);

        return validateThatAllFieldsAreFilled(this, firstNameEditText, lastNameEditText, dateOfBirthEditText, emailEditText)
                && validateEmail(this, emailEditText);
    }

    private void editUserBiography(final View applyButton) {
        Log.d(TAG, "New user biography: " + new Gson().toJson(user));
        refreshLoadingIndicator(true);
        insertInputIntoUser();
        LincedInRequester.editUserProfile(
                user,
                this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Log.i(TAG, "The biography was edited successfully!");
                        refreshLoadingIndicator(false);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(applyButton, R.string.error_update_biography, Snackbar.LENGTH_LONG).show();
                        Log.e(TAG, error.toString());
                        error.printStackTrace();
                        refreshLoadingIndicator(false);
                    }
                }
        );
    }

    private void insertInputIntoUser() {
        user.firstName = ((EditText) findViewById(R.id.biography_firstname_edittext)).getText().toString();
        user.lastName = ((EditText) findViewById(R.id.biography_lastname_edittext)).getText().toString();
        user.dateOfBirth = ((EditText) findViewById(R.id.biography_date_of_birth_edittext)).getText().toString();
        user.email = ((EditText) findViewById(R.id.biography_email_edittext)).getText().toString();
        user.description = ((EditText) findViewById(R.id.biography_description_edittext)).getText().toString();
    }

    private void refreshLoadingIndicator(boolean loading) {
        if (loading) {
            findViewById(R.id.biography_loading_circular_progress).setVisibility(View.VISIBLE);
            findViewById(R.id.biography_container_scrollview).setVisibility(View.GONE);
            findViewById(R.id.biography_apply_changes_fab).setVisibility(View.GONE);
        } else {
            findViewById(R.id.biography_loading_circular_progress).setVisibility(View.GONE);
            findViewById(R.id.biography_container_scrollview).setVisibility(View.VISIBLE);
            findViewById(R.id.biography_apply_changes_fab).setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDatePicked(DatePickedEvent event) {
        EditText dateOfBirthEditText = (EditText) findViewById(R.id.biography_date_of_birth_edittext);
        if (dateOfBirthEditText != null) {
            String dateOfBirth = DateUtils.parseToDatetime(event.day, event.month, event.year);
            dateOfBirthEditText.setText(DateUtils.parseDatetimeToDateWithoutTime(dateOfBirth));
        }
    }
}
