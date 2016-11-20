package com.fiuba.tallerii.lincedin.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
import com.fiuba.tallerii.lincedin.utils.InputValidationUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import static com.fiuba.tallerii.lincedin.network.UserAuthenticationManager.saveSessionToken;
import static com.fiuba.tallerii.lincedin.utils.InputValidationUtils.validateEmail;
import static com.fiuba.tallerii.lincedin.utils.InputValidationUtils.validateThatAllFieldsAreFilled;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "LogInActivity";

    private static CallbackManager callbackManager = CallbackManager.Factory.create();

    private AccessTokenTracker accessTokenTracker;
    private static String facebookAccessToken;

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
                    LincedInLogInUser(email, password);
                }
            }
        });
    }

    private void LincedInLogInUser(String email, String password) {
        refreshLoadingIndicator(true);
        UserAuthenticationManager.lincedInLogIn(
                this,
                email,
                password,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onSuccessLincedInLogIn(response);
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

    private void onSuccessLincedInLogIn(JSONObject response) {
        refreshLoadingIndicator(false);
        Log.d(TAG, new Gson().toJson(response));
        try {
            saveSessionToken(this, response.getString("token"));
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
                                    facebookLogInUser();
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
                UserAuthenticationManager.deleteSessionToken(getApplicationContext());
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "Facebook login callback error!");
                exception.printStackTrace();
                UserAuthenticationManager.deleteSessionToken(getApplicationContext());
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
        startActivity(signUpIntent);
    }

    private void facebookLogInUser() {
        refreshLoadingIndicator(true);
        UserAuthenticationManager.facebookLogIn(
                this,
                facebookAccessToken,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onSuccessLincedInLogInWithFacebook(response);
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

    private void onSuccessLincedInLogInWithFacebook(JSONObject response) {
        refreshLoadingIndicator(false);
        Log.d(TAG, new Gson().toJson(response));
        try {
            saveSessionToken(this, response.getString("token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onErrorLincedInLogInWithFacebook(VolleyError error) {
        refreshLoadingIndicator(false);
        Log.e(TAG, error.toString());
        error.printStackTrace();
        Snackbar.make(findViewById(R.id.login_facebook_button), getString(R.string.error_login), Snackbar.LENGTH_LONG)
                .show();
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
        if (accessToken != null) {
            facebookAccessToken = accessToken.getToken();
        } else {
            facebookAccessToken = null;
            UserAuthenticationManager.deleteSessionToken(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}
