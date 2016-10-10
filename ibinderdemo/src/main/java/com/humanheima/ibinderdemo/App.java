package com.humanheima.ibinderdemo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by Administrator on 2016/9/14.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("tag", getCurProgressName());
    }

    private String getCurProgressName() {
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appInfo : manager.getRunningAppProcesses()
                ) {
            if (appInfo.pid == pid) {
                return "pid:" + pid + "appInfo.processName" + appInfo.processName;
            }
        }
        return "null";
    }
}
