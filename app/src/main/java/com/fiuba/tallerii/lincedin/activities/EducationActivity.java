package com.fiuba.tallerii.lincedin.activities;

import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.fragments.AllEducationFragment;
import com.fiuba.tallerii.lincedin.fragments.EditEducationFragment;
import com.fiuba.tallerii.lincedin.model.user.User;
import com.fiuba.tallerii.lincedin.model.user.UserEducation;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.google.gson.Gson;

import org.json.JSONObject;

public class EducationActivity extends AppCompatActivity
        implements AllEducationFragment.AllEducationFragmentListener, EditEducationFragment.EditEducationFragmentListener {

    private static final String TAG = "Education";

    public static final String ARG_USER = "USER";
    public static final String ARG_IS_OWN_PROFILE = "IS_OWN_PROFILE";

    private User user = new User();
    private boolean isOwnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
        setToolbar();
        getUserFromIntent();
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_education);
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
        isOwnProfile = getIntent().getBooleanExtra(ARG_IS_OWN_PROFILE, false);
        return user;
    }

    private void showInitialFragment() {
        if (user.education != null && !user.education.isEmpty()) {
            showAllEducationFragment();
        } else {
            showEditEducationFragment(null);
        }
    }

    private void showAllEducationFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.education_container_framelayout, AllEducationFragment.newInstance(user.education, isOwnProfile));
        transaction.addToBackStack("AllEducationFragment");
        transaction.commit();
    }

    private void showEditEducationFragment(@Nullable UserEducation educationSelected) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.education_container_framelayout, EditEducationFragment.newInstance(educationSelected));
        transaction.addToBackStack("EditEducationFragment");
        transaction.commit();
    }

    @Override
    public void onAddEducationButtonPressed() {
        showEditEducationFragment(null);
    }

    @Override
    public void onEducationRowClicked(UserEducation education) {
        showEditEducationFragment(education);
    }

    @Override
    public void onNewEducationAdded(final UserEducation education) {
        Log.d(TAG, "User education to add: " + new Gson().toJson(education));
        user.education.add(education);
        LincedInRequester.editUserProfile(
                user,
                this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Log.i(TAG, "The education was added successfully!");
                        showAllEducationFragment();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        error.printStackTrace();
                        user.education.remove(education);
                        showAllEducationFragment();
                    }
                }
        );
    }

    @Override
    public void onEducationEdited(final UserEducation previousEducation, final UserEducation updatedEducation) {
        Log.d(TAG, "User education to edit: from " + new Gson().toJson(previousEducation) + " to " + new Gson().toJson(updatedEducation));
        user.education.remove(previousEducation);
        user.education.add(updatedEducation);
        LincedInRequester.editUserProfile(
                user,
                this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Log.i(TAG, "The education was edited successfully!");
                        showAllEducationFragment();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        error.printStackTrace();
                        user.education.remove(updatedEducation);
                        user.education.add(previousEducation);
                        showAllEducationFragment();
                    }
                }
        );
    }

    @Override
    public void onEducationDeleted(final UserEducation education) {
        showDeleteEducationConfirmationDialog(education);
    }

    private void showDeleteEducationConfirmationDialog(final UserEducation education) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.are_you_sure_you_want_to_delete_education))
                .setMessage(getString(R.string.this_change_cannot_be_reverted))
                .setCancelable(true)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        confirmEducationDeletion(education);
                    }
                })
                .setNegativeButton(android.R.string.no, null);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            }
        });
        dialog.show();
    }

    private void confirmEducationDeletion(final UserEducation education) {
        Log.d(TAG, "User education to delete: " + new Gson().toJson(education));
        user.education.remove(education);
        LincedInRequester.editUserProfile(
                user,
                this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Log.i(TAG, "The education was deleted successfully!");
                        showAllEducationFragment();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        error.printStackTrace();
                        user.education.add(education);
                        showAllEducationFragment();
                    }
                }
        );
    }
}
