package com.example.mad.lab2;

/**
 * Created by daniel on 08/04/17.
 */

import com.firebase.client.Firebase;



        import com.facebook.FacebookSdk;
        import com.facebook.appevents.AppEventsLogger;
        import com.firebase.client.Firebase;

/**
 * Created by AndroidBash on 10/07/16
 */
public class CustomApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}