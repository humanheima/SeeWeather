package com.humanheima.hmweather;

import android.app.Application;
import android.content.Context;

/**
 * Created by dmw on 2016/9/9.
 */
public class HMApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getAppContext() {
        return context;
    }
}
