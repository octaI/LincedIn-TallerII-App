package com.fiuba.tallerii.lincedin.activities;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.events.RecommendationPostedEvent;
import com.fiuba.tallerii.lincedin.fragments.RecommendUserDialogFragment;
import com.fiuba.tallerii.lincedin.fragments.RecommendationsMadeFragment;
import com.fiuba.tallerii.lincedin.fragments.RecommendationsReceivedFragment;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.fiuba.tallerii.lincedin.utils.ViewUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

public class RecommendationsActivity extends AppCompatActivity
        implements RecommendationsReceivedFragment.OnRecommendationsReceivedFragmentInteractionListener {

    private static final String TAG = "Recommendations";

    public static final String ARG_USER_ID = "USER_ID";
    public static final String ARG_IS_OWN_PROFILE = "IS_OWN_PROFILE";

    private RecommendationsSectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private String userId;
    private boolean isOwnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);
        EventBus.getDefault().register(this);

        setToolbar();
        setTabs();

        getArgsFromIntent();
        setButtonListener();
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_recommendations);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setTabs() {
        mSectionsPagerAdapter = new RecommendationsSectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.recommendations_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.recommendations_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(1);

        tabLayout.getTabAt(0).setText(R.string.recommendations_received);
        tabLayout.getTabAt(1).setText(R.string.recommendations_made);
    }

    private void getArgsFromIntent() {
        userId = getIntent().getStringExtra(ARG_USER_ID);
        isOwnProfile = getIntent().getBooleanExtra(ARG_IS_OWN_PROFILE, false);
    }

    private void setButtonVisibility() {
        if (isOwnProfile) {
            findViewById(R.id.fragment_recommendations_add_recommendation_fab).setVisibility(View.GONE);
        } else {
            findViewById(R.id.fragment_recommendations_add_recommendation_fab).setVisibility(View.VISIBLE);
        }
    }

    private void setButtonListener() {
        findViewById(R.id.fragment_recommendations_add_recommendation_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecommendUserDialog();
            }
        });
    }

    private void openRecommendUserDialog() {
        DialogFragment datePickerDialog = new RecommendUserDialogFragment();
        datePickerDialog.show(getSupportFragmentManager(), "RecommendUserDialogFragment");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecommendationPosted(RecommendationPostedEvent event) {
        LincedInRequester.recommendUser(
                userId,
                event.message,
                this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, new Gson().toJson(response));
                        // TODO: 01/12/16 Reload Received fragment
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error posting recommendation: " + error.toString());
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            Log.e(TAG, new String(error.networkResponse.data));
                        }
                        ViewUtils.setSnackbar(
                                findViewById(R.id.fragment_recommendations_add_recommendation_fab),
                                R.string.error_recommend_user,
                                Snackbar.LENGTH_LONG
                        );
                    }
                }
        );
    }

    @Override
    public void onUserNotRecommended() {
        setButtonVisibility();
    }

    public class RecommendationsSectionsPagerAdapter extends FragmentPagerAdapter {
        private static final int COUNT_SECTIONS = 2;

        public RecommendationsSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return RecommendationsReceivedFragment.newInstance(userId);
            } else if (position == 1) {
                return RecommendationsMadeFragment.newInstance(userId, isOwnProfile);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show total pages.
            return COUNT_SECTIONS;
        }

    }
}
