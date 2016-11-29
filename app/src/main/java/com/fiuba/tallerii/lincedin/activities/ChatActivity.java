package com.fiuba.tallerii.lincedin.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.fragments.ChatFragment;
import com.fiuba.tallerii.lincedin.fragments.ErrorFragment;
import com.fiuba.tallerii.lincedin.fragments.UserNotLoggedFragment;
import com.fiuba.tallerii.lincedin.network.UserAuthenticationManager;
import com.fiuba.tallerii.lincedin.utils.ViewUtils;

public class ChatActivity extends AppCompatActivity
        implements UserNotLoggedFragment.OnUserNotLoggedFragmentInteractionListener,
        ErrorFragment.OnErrorFragmentInteractionListener, ChatFragment.OnChatFragmentInteractionListener {

    private static final String TAG = "ChatActivity";

    public static final String ARG_RECEIVING_USER_ID = "ARG_RECEIVING_USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setToolbar();
        getReceivingUserIdFromIntent();
        showInitialFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    // TODO: 29/11/16 It only supports 1-1 conversations!
    private String getReceivingUserIdFromIntent() {
        return getIntent().getStringExtra(ARG_RECEIVING_USER_ID);
    }

    private void showInitialFragment() {
        if (UserAuthenticationManager.isUserLoggedIn(this)) {
            showChatFragment();
        } else {
            showUserNotLoggedFragment();
        }
    }

    private void showChatFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.chat_container_framelayout, ChatFragment.newInstance(getReceivingUserIdFromIntent()))
                .commit();
    }

    private void showUserNotLoggedFragment() {
        ViewUtils.setUserNotLoggedFragment(R.id.chat_container_framelayout, getSupportFragmentManager());
    }

    private void showErrorFragment() {
        ViewUtils.setErrorFragment(R.id.chat_container_framelayout, getSupportFragmentManager());
    }

    @Override
    public void onRetry() {
        showChatFragment();
    }

    @Override
    public void onLoginButtonTapped() {
        Intent loginIntent = new Intent(this, LogInActivity.class);
        startActivity(loginIntent);
    }

    @Override
    public void onError() {
        showErrorFragment();
    }
}
