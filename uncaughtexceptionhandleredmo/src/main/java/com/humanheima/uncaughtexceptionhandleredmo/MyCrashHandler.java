package com.humanheima.uncaughtexceptionhandleredmo;

import android.content.Context;

/**
 * Created by Administrator on 2016/10/12.
 */

public class MyCrashHandler implements Thread.UncaughtExceptionHandler {

    private static Thread.UncaughtExceptionHandler defaultHandler;
    private Context context;
    private final String TAG = MyCrashHandler.class.getSimpleName();

    public MyCrashHandler(Context context) {
        this.context = context;
    }

    /**
     * 初始化,设置该CrashHandler为程序的默认处理器
     */
    public static void init(MyCrashHandler crashHandler) {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

    }
}
