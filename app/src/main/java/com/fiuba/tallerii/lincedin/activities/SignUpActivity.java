package com.fiuba.tallerii.lincedin.activities;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.events.DatePickedEvent;
import com.fiuba.tallerii.lincedin.fragments.DatePickerDialogFragment;
import com.fiuba.tallerii.lincedin.network.UserAuthenticationManager;
import com.fiuba.tallerii.lincedin.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Date;
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
            String dateOfBirth = DateUtils.parseToLocalDate(this, event.day, event.month, event.year);
            dateOfBirthEditText.setText(dateOfBirth);
        }
    }

    private void openBirthdayDatePickerDialog() {
        DialogFragment httpDialog = new DatePickerDialogFragment();
        httpDialog.show(getSupportFragmentManager(), "DatePickerDialogFragment");
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
        EditText repeatPasswordEditText = (EditText) findViewById(R.id.signup_repeat_password_edittext);

        return validateThatAllFieldsAreFilled(firstNameEditText, lastNameEditText, dateOfBirthEditText, emailEditText, passwordEditText, repeatPasswordEditText)
                && validateEmail(emailEditText);
    }

    private void createUserAccount() {

    }

    private boolean validateThatAllFieldsAreFilled(EditText... fields) {
        boolean allFieldsAreFilled = true;
        for (EditText field : fields) {
            field.setError(null);   // Clears error icon. Fixes date of birth bug.
            if (field.getText() == null || field.getText().toString().equals("")) {
                field.requestFocus();
                field.setError(getString(R.string.field_is_required));
                Log.e(TAG, "'" + field.getHint().toString() + "'" + " cannot be empty. User account creation was cancelled.");
                allFieldsAreFilled = false;
                break;
            }
        }
        return allFieldsAreFilled;
    }

    private boolean validateEmail(EditText emailField) {
        // See http://stackoverflow.com/questions/8204680/java-regex-email for pattern reference
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        String email = emailField.getText().toString();
        if (!pattern.matcher(email).matches()) {
            emailField.requestFocus();
            emailField.setError(getString(R.string.invalid_email));
            Log.e(TAG, "The user e-mail " + email + " does not match the RFC822 pattern for valid e-mails. User account creation was cancelled.");
            return false;
        } else {
            return true;
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
