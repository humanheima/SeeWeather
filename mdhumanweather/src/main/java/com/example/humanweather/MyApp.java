package com.example.humanweather;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by dumingwei on 2016/4/14.
 */
public class MyApp extends Application {

    private static Context context;

    private static RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();
        // 得到了一个应用程序级别的 Context
        context = getApplicationContext();
        queue= Volley.newRequestQueue(context);
    }
    public static Context getContex() {
        return context;
    }

    public static RequestQueue getVolleyQueue(){
        return queue;
    }
}
