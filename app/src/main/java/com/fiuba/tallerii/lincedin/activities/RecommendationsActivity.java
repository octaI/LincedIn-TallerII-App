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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.events.RecommendationErrorEvent;
import com.fiuba.tallerii.lincedin.events.RecommendationPostedEvent;
import com.fiuba.tallerii.lincedin.fragments.RecommendUserDialogFragment;
import com.fiuba.tallerii.lincedin.fragments.RecommendationsMadeFragment;
import com.fiuba.tallerii.lincedin.fragments.RecommendationsReceivedFragment;
import com.fiuba.tallerii.lincedin.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        DialogFragment datePickerDialog = RecommendUserDialogFragment.newInstance(userId);
        datePickerDialog.show(getSupportFragmentManager(), "RecommendUserDialogFragment");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecommendationError(RecommendationErrorEvent event) {
        ViewUtils.setSnackbar(
                findViewById(R.id.fragment_recommendations_add_recommendation_fab),
                R.string.error_recommend_user,
                Snackbar.LENGTH_LONG
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
