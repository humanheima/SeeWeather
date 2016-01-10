package com.humanheima.litepaldemo;

import android.app.Application;

import org.litepal.LitePalApplication;

/**
 * Created by Administrator on 2016/9/10.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePalApplication.initialize(this);
    }
}
