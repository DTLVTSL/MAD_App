package com.example.mad.lab2;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by root on 10/05/17.
 */

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
