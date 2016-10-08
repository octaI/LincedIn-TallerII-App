package com.fiuba.tallerii.lincedin.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.fiuba.tallerii.lincedin.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {

    public RegistrationIntentService() {
        super("RegistrationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            InstanceID instanceID = InstanceID.getInstance(this);
            try {
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                // TODO: 08/10/16 Save token and send it to server if the user is already logged in.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
