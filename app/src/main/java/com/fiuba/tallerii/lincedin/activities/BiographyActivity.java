package com.fiuba.tallerii.lincedin.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.user.User;
import com.google.gson.Gson;

public class BiographyActivity extends AppCompatActivity {

    private static final String TAG = BiographyActivity.class.getName();

    public static final String ARG_USER = "USER";

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biography);
        setToolbar();
        getUserFromIntent();
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_skills);
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
}
