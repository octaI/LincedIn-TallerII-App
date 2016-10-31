package com.fiuba.tallerii.lincedin.activities;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.events.DatePickedEvent;
import com.fiuba.tallerii.lincedin.fragments.DatePickerDialogFragment;
import com.fiuba.tallerii.lincedin.network.UserAuthenticationManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        findViewById(R.id.signup_date_of_birth_textview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBirthdayDatePickerDialog();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDatePicked(DatePickedEvent event) {
        TextView birthdayTextView = (TextView) findViewById(R.id.signup_date_of_birth_textview);
        if (birthdayTextView != null) {
            String day = event.day >= 10 ? String.valueOf(event.day) : "0" + String.valueOf(event.day);
            String month = event.month >= 10 ? String.valueOf(event.month) : "0" + String.valueOf(event.month);
            String year = String.valueOf(event.year);

            String dateOfBirth = day + "/" + month + "/" + year;

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = sdf.parse(dateOfBirth);
                java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(this);
                dateOfBirth = dateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            birthdayTextView.setText(dateOfBirth);
            birthdayTextView.setTextColor(ContextCompat.getColor(this, R.color.white));
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
                validateUserInput();
                createUserAccount();
            }
        });
    }

    private void validateUserInput() {

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
