package com.mcho.recipient;

import android.app.Application;

public class AppController extends Application

{
    private static AppController appInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        appInstance = this;
    }

    public static AppController getAppInstance() {
        return appInstance;
    }
}
