package com.example.recyclerview.support;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;

/**
 * Support class to check networking and internet
 */
public class NetworkingChecker {
    public static boolean isInternetAvailable(Activity activity) {
        if (!isNetworkConnected(activity))
            return false;

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager)
                activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm != null && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnected();
    }
}
