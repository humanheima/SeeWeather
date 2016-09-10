package com.humanheima.hmweather;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.humanheima.hmweather.utils.LogUtil;

import org.litepal.LitePalApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dmw on 2016/9/9.
 */
public class HMApp extends Application {

    private static Context context;
    private final static int BUFFER_SIZE = 102400;
    public static final String DB_NAME = "hmweather.db"; //数据库名字
    public static final String PACKAGE_NAME = "com.humanheima.hmweather";
    //在手机里存放数据库的位置(/data/data/com.example.humanweather/china_city.db)
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME + "/databases";
    public static final String DB_FILE = DB_PATH + "/" + DB_NAME; //数据库文件

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        LitePalApplication.initialize(context);
        copyDataBase(DB_FILE);
    }

    public static Context getAppContext() {
        return context;
    }


    /**
     * 拷贝数据库文件
     */
    public static void copyDataBase(final String dbFile) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                LogUtil.e("database", "doInBackground");
                copyDB(dbFile);
                if (new File(dbFile).exists()) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean successful) {
                if (successful) {
                    LogUtil.e("database", "exists");
                }
            }
        }.execute();


    }

    private static void copyDB(String dbFile) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            if (!new File(DB_PATH).exists()) {
                File file = new File(DB_PATH);
                file.mkdirs();
                if (!new File(dbFile).exists()) {
                    LogUtil.e("database", "copy");
                    //如果数据库文件不存在
                    is = context.getResources().getAssets().open("hmweather.db");
                    fos = new FileOutputStream(dbFile);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int count = -1;
                    while ((count = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, count);
                        fos.flush();
                    }
                }
            }

        } catch (IOException e) {
            LogUtil.e("database", "IOException" + e.getMessage());
            e.printStackTrace();
        } finally {

            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

