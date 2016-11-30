package com.fiuba.tallerii.lincedin.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.fragments.UserProfileFragment;

public class UserProfileActivity extends AppCompatActivity {

    public static final String ARG_USER_ID = "ARG_USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setToolbar();
        loadUserProfileFragment();
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user_profile);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void loadUserProfileFragment() {
        String userId = getIntent().getStringExtra(ARG_USER_ID);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_user_profile_container_framelayout, UserProfileFragment.newInstance(userId))
                .commit();
    }
}
