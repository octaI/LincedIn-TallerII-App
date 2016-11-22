package com.fiuba.tallerii.lincedin.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.network.UserAuthenticationManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import static com.fiuba.tallerii.lincedin.network.UserAuthenticationManager.saveUserAuthInfo;
import static com.fiuba.tallerii.lincedin.utils.InputValidationUtils.validateEmail;
import static com.fiuba.tallerii.lincedin.utils.InputValidationUtils.validateThatAllFieldsAreFilled;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "LogInActivity";

    private static final int REQ_CREATE_ACCOUNT = 10;

    private static CallbackManager callbackManager = CallbackManager.Factory.create();

    private AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setFacebookAccessTokenTracker();
        setListeners();
    }

    private void setListeners() {
        setCancelButtonListener();
        setLincedInLogInButtonListener();
        setFacebookLogInButtonListener();
        setCreateAccountTextListener();
    }

    private void setCancelButtonListener() {
        findViewById(R.id.login_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setLincedInLogInButtonListener() {
        findViewById(R.id.login_lincedin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput() && validateUserIsNotAlreadyLoggedIn(v)) {
                    String email = ((EditText) findViewById(R.id.login_email_edittext)).getText().toString();
                    String password = ((EditText) findViewById(R.id.login_password_edittext)).getText().toString();
                    lincedInLogInUser(email, password);
                }
            }
        });
    }

    private void lincedInLogInUser(final String email, final String password) {
        refreshLoadingIndicator(true);
        UserAuthenticationManager.lincedInLogIn(
                this,
                email,
                password,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onSuccessLincedInLogIn(response, email, password);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onErrorLincedInLogIn(error);
                    }
                }
        );
    }

    private void onSuccessLincedInLogIn(JSONObject response, String email, String password) {
        refreshLoadingIndicator(false);
        Log.d(TAG, new Gson().toJson(response));
        try {
            saveUserAuthInfo(this, response.getString("token"), email, password);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onErrorLincedInLogIn(VolleyError error) {
        refreshLoadingIndicator(false);
        Log.e(TAG, error.toString());
        error.printStackTrace();
        Snackbar.make(findViewById(R.id.login_lincedin_button), getString(R.string.error_login), Snackbar.LENGTH_LONG)
                .show();
    }

    private void setFacebookLogInButtonListener() {
        LoginButton fbLoginButton = (LoginButton) findViewById(R.id.login_facebook_button);
        if (fbLoginButton == null) {
            return;
        }

        fbLoginButton.setReadPermissions("email", "public_profile", "user_location", "user_birthday");
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Facebook login callback success.");
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject json, GraphResponse response) {
                                if (response.getError() != null) {
                                    Log.d(TAG, "Facebook profile Graph request error!");
                                } else {
                                    Log.d(TAG, "Facebook profile Graph request success.");
                                    String jsonResult = new Gson().toJson(json);
                                    Log.d(TAG, jsonResult);
                                    facebookLogInUser(json);
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, first_name, last_name, email, birthday, link, location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Facebook login callback cancelled.");
                UserAuthenticationManager.deleteUserAuthInfo(getApplicationContext());
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "Facebook login callback error!");
                exception.printStackTrace();
                UserAuthenticationManager.deleteUserAuthInfo(getApplicationContext());
            }
        });
    }

    private void refreshLoadingIndicator(boolean loading) {
        if (loading) {
            findViewById(R.id.login_loading_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.login_loading_layout).setVisibility(View.GONE);
        }
    }

    private void setCreateAccountTextListener() {
        findViewById(R.id.login_has_account_textview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });
    }

    private void openSignUpActivity() {
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
        startActivityForResult(signUpIntent, REQ_CREATE_ACCOUNT);
    }

    private void facebookLogInUser(final JSONObject facebookResponse) {
        refreshLoadingIndicator(true);
        UserAuthenticationManager.facebookLogIn(
                this,
                AccessToken.getCurrentAccessToken().getToken(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            onSuccessLincedInLogInWithFacebook(response, facebookResponse.getString("email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Couldn't parse email from Facebook log in response.");
                            onSuccessLincedInLogInWithFacebook(response, null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onErrorLincedInLogInWithFacebook(error);
                    }
                }
        );
    }

    private void onSuccessLincedInLogInWithFacebook(JSONObject response, String email) {
        onSuccessLincedInLogIn(response, email, null);
    }

    private void onErrorLincedInLogInWithFacebook(VolleyError error) {
        onErrorLincedInLogIn(error);
        LoginManager.getInstance().logOut();
    }

    private boolean validateUserIsNotAlreadyLoggedIn(View v) {
        if (UserAuthenticationManager.isUserLoggedIn(this)) {
            Snackbar.make(v, R.string.user_already_logged_in, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateInput() {
        EditText emailEditText = (EditText) findViewById(R.id.login_email_edittext);
        EditText passwordEditText = (EditText) findViewById(R.id.login_password_edittext);

        return validateThatAllFieldsAreFilled(this, emailEditText, passwordEditText)
                && validateEmail(this, emailEditText);
    }

    private void setFacebookAccessTokenTracker() {
        // If the access token is available already assign it.
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Log.d(TAG, "Facebook AccessToken: " + (accessToken != null ? accessToken.getToken() : "null"));
        updateSessionToken(accessToken);

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                Log.i(TAG, "Facebook access token has changed.");
                Log.d(TAG, "New Facebook AccessToken: " + (currentAccessToken != null ? currentAccessToken.getToken() : "null"));
                updateSessionToken(currentAccessToken);
            }
        };
    }

    private void updateSessionToken(AccessToken accessToken) {
        if (accessToken == null) {
            UserAuthenticationManager.deleteUserAuthInfo(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CREATE_ACCOUNT:
                if (resultCode == Activity.RESULT_OK) {
                    Snackbar.make(findViewById(R.id.login_lincedin_button), R.string.account_successfully_created, Snackbar.LENGTH_SHORT).show();
                    ((EditText) findViewById(R.id.login_email_edittext)).setText(data.getStringExtra(SignUpActivity.EXTRA_EMAIL));
                    ((EditText) findViewById(R.id.login_password_edittext)).setText(data.getStringExtra(SignUpActivity.EXTRA_PASSWORD));
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}
