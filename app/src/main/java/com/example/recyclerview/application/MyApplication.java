package com.example.recyclerview.application;

import android.app.Application;

/**
 * Application instance lives until the app destroyed
 * ApplicationGraph lives in the Application class to share its lifecycle
 */
public class MyApplication extends Application {
    public static final String APP_TAG = "RECYCLE_VIEW";

    // Reference to the application graph that is used across the whole app
    public ApplicationGraph applicationGraph = DaggerApplicationGraph.create();
}