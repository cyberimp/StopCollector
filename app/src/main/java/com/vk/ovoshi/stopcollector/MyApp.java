package com.vk.ovoshi.stopcollector;

import android.app.Application;
import android.content.Context;

/**
 * Created by Ovoshi on 11/21/2016.
 */

public class MyApp extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
