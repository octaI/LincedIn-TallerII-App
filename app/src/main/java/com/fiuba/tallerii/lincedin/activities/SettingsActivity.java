package com.fiuba.tallerii.lincedin.activities;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.fragments.HTTPConfigurationDialogFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setToolbar();
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setListeners() {
        findViewById(R.id.settings_option_login_textview).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startLoginActivity();
                    }
                }
        );

        findViewById(R.id.settings_option_http_config_textview).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openHTTPConfigurationDialog();
                    }
                }
        );
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
