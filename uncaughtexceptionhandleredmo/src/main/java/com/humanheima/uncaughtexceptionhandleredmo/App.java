package com.humanheima.uncaughtexceptionhandleredmo;

import android.app.Application;

/**
 * Created by Administrator on 2016/10/12.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }
}
