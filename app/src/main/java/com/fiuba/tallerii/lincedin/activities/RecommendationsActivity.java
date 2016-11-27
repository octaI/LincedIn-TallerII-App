package com.fiuba.tallerii.lincedin.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fiuba.tallerii.lincedin.R;

public class RecommendationsActivity extends AppCompatActivity {

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

        setToolbar();
        setTabs();

        getArgsFromIntent();
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
    }

    private void getArgsFromIntent() {
        userId = getIntent().getStringExtra(ARG_USER_ID);
        isOwnProfile = getIntent().getBooleanExtra(ARG_IS_OWN_PROFILE, false);
    }


    public class RecommendationsSectionsPagerAdapter extends FragmentPagerAdapter {
        private static final int COUNT_SECTIONS = 2;

        public RecommendationsSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // TODO: 27/11/16 Implement!
            return null;
        }

        @Override
        public int getCount() {
            // Show total pages.
            return COUNT_SECTIONS;
        }

    }
}
