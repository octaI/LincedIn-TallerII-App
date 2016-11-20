package com.fiuba.tallerii.lincedin.activities;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.fragments.HTTPConfigurationDialogFragment;
import com.fiuba.tallerii.lincedin.network.UserAuthenticationManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setToolbar();
        setOptionsListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setLoginButtonListener();
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setOptionsListeners() {
        findViewById(R.id.settings_option_http_config_textview).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openHTTPConfigurationDialog();
                    }
                }
        );
    }

    private void setLoginButtonListener() {
        final Button loginButton = (Button) findViewById(R.id.settings_login_button);
        if (loginButton != null) {
            if (UserAuthenticationManager.isUserLoggedIn(this)) {
                loginButton.setText(getString(R.string.logout));
                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logOut();
                        finish();
                    }
                });
            } else {
                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startLoginActivity();
                    }
                });
            }
        }
    }

    private void logOut() {
        UserAuthenticationManager.logOut(this);
    }

    private void startLoginActivity() {
        Intent loginIntent = new Intent(this, LogInActivity.class);
        startActivity(loginIntent);
    }

    private void openHTTPConfigurationDialog() {
        DialogFragment httpDialog = new HTTPConfigurationDialogFragment();
        httpDialog.show(getSupportFragmentManager(), "HTTPConfigurationDialogFragment");
    }
}
