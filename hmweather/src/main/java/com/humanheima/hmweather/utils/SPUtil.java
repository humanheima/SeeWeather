package com.humanheima.hmweather.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.humanheima.hmweather.HMApp;
import com.humanheima.hmweather.R;

/**
 * Created by dmw on 2016/9/12.
 */
public class SPUtil {

    private static SPUtil spUtil;
    private static SharedPreferences hmSpref;
    private final static String SP_NAME = "hmSpref";
    private static SharedPreferences.Editor editor;

    private SPUtil() {
        hmSpref = HMApp.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = hmSpref.edit();
    }

    public static SPUtil getInstance() {
        if (spUtil == null) {
            synchronized (SPUtil.class) {
                if (spUtil == null) {
                    spUtil = new SPUtil();
                }
            }
        }
        return spUtil;
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return hmSpref.getInt(key, R.mipmap.none);
    }

    public void putBoolan(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return hmSpref.getBoolean(key, false);
    }

}