package com.fiuba.tallerii.lincedin;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.fiuba.tallerii.lincedin.network.HttpRequestHelper;

public class AppInitializer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HttpRequestHelper.initialize(getApplicationContext());

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
