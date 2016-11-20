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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.fragments.AllSkillsFragment;
import com.fiuba.tallerii.lincedin.fragments.EditSkillFragment;
import com.fiuba.tallerii.lincedin.model.user.User;
import com.fiuba.tallerii.lincedin.model.user.UserSkill;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.fiuba.tallerii.lincedin.utils.ViewUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

public class SkillsActivity extends AppCompatActivity
        implements AllSkillsFragment.AllSkillsFragmentListener, EditSkillFragment.EditSkillFragmentListener {

    private static final String TAG = "Skills";

    public static final String ARG_USER = "USER";
    public static final String ARG_IS_OWN_PROFILE = "IS_OWN_PROFILE";

    private User user = new User();
    private boolean isOwnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
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
        isOwnProfile = getIntent().getBooleanExtra(ARG_IS_OWN_PROFILE, false);
        return user;
    }

    private void showInitialFragment() {
        if (user.skills != null && !user.skills.isEmpty()) {
            showAllSkillsFragment();
        } else {
            showEditSkillFragment(null);
        }
    }

    private void showAllSkillsFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.skills_container_framelayout, AllSkillsFragment.newInstance(user.skills, isOwnProfile));
        transaction.addToBackStack("AllSkillsFragment");
        transaction.commit();
    }

    private void showEditSkillFragment(@Nullable UserSkill skillSelected) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.skills_container_framelayout, EditSkillFragment.newInstance(skillSelected));
        transaction.addToBackStack("AddSkillFragment");
        transaction.commit();
    }

    @Override
    public void onAddSkillButtonPressed() {
        showEditSkillFragment(null);
    }

    @Override
    public void onSkillRowClicked(UserSkill skill) {
        showEditSkillFragment(skill);
    }

    @Override
    public void onNewSkillAdded(final UserSkill skill) {
        Log.d(TAG, "User skill to add: " + new Gson().toJson(skill));
        user.skills.add(skill);
        LincedInRequester.editUserProfile(
                user,
                this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        setToast(getString(R.string.skill_was_added_successfully), Toast.LENGTH_SHORT);
                        Log.d(TAG, response.toString());
                        Log.i(TAG, "The skill was added successfully!");
                        showAllSkillsFragment();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setToast(getString(R.string.error_add_skill), Toast.LENGTH_LONG);
                        Log.e(TAG, error.toString());
                        error.printStackTrace();
                        user.skills.remove(skill);
                        showAllSkillsFragment();
                    }
                }
        );
    }

    @Override
    public void onSkillEdited(final UserSkill previousSkill, final UserSkill updatedSkill) {
        Log.d(TAG, "User skill to edit: from " + new Gson().toJson(previousSkill) + " to " + new Gson().toJson(updatedSkill));
        user.skills.remove(previousSkill);
        user.skills.add(updatedSkill);
        LincedInRequester.editUserProfile(
                user,
                this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        setToast(getString(R.string.skill_was_edited_successfully), Toast.LENGTH_SHORT);
                        Log.d(TAG, response.toString());
                        Log.i(TAG, "The skill was edited successfully!");
                        showAllSkillsFragment();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setToast(getString(R.string.error_edit_skill), Toast.LENGTH_LONG);
                        Log.e(TAG, error.toString());
                        error.printStackTrace();
                        user.skills.remove(updatedSkill);
                        user.skills.add(previousSkill);
                        showAllSkillsFragment();
                    }
                }
        );
    }

    @Override
    public void onSkillDeleted(final UserSkill skill) {
        showDeleteSkillConfirmationDialog(skill);
    }

    private void showDeleteSkillConfirmationDialog(final UserSkill skill) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.are_you_sure_you_want_to_delete_skill))
                .setMessage(getString(R.string.this_change_cannot_be_reverted))
                .setCancelable(true)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        confirmSkillDeletion(skill);
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

    private void confirmSkillDeletion(final UserSkill skill) {
        Log.d(TAG, "User skill to delete: " + new Gson().toJson(skill));
        user.skills.remove(skill);
        LincedInRequester.editUserProfile(
                user,
                this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        setToast(getString(R.string.skill_was_deleted_successfully), Toast.LENGTH_SHORT);
                        Log.d(TAG, response.toString());
                        Log.i(TAG, "The skill was deleted successfully!");
                        showAllSkillsFragment();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setToast(getString(R.string.error_delete_skill), Toast.LENGTH_LONG);
                        Log.e(TAG, error.toString());
                        error.printStackTrace();
                        user.skills.add(skill);
                        showAllSkillsFragment();
                    }
                }
        );
    }

    private void setToast(String message, int duration) {
        ViewUtils.setToast(this, message, duration);
    }
}
