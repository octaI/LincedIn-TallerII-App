package com.fiuba.tallerii.lincedin.services;

import android.util.Log;

import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class LincedInFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseIIdService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        SharedPreferencesUtils.putStringToSharedPreferences(getApplicationContext(), SharedPreferencesKeys.FIREBASE_ID, refreshedToken);
    }
}
