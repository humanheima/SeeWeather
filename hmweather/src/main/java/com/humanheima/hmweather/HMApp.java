package com.humanheima.hmweather;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.humanheima.hmweather.utils.LogUtil;
import com.humanheima.hmweather.utils.SPUtil;

import org.litepal.LitePalApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

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

    private static final String COPIED = "copied";//标志数据库文件是否已经拷贝
    private static final String ICON_INITIALIZED = "initialized";//标识天气图标是否已经初始化
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //初始化litepal数据库
        LitePalApplication.initialize(context);
        //把数据库文件拷贝到本地
        if (!SPUtil.getInstance().getBoolean(COPIED)) {
            LogUtil.e("COPIED","COPIED");
            copyDataBase(DB_FILE);
        }
        //初始化图片
        if (!SPUtil.getInstance().getBoolean(ICON_INITIALIZED)) {
            initWeatherIcon();
        }
    }

    public static Context getAppContext() {
        return context;
    }

    /**
     * 拷贝数据库文件
     */
    public static void copyDataBase(final String dbFile) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                LogUtil.e("database", "doInBackground");
                copyDB(dbFile);
                if (new File(dbFile).exists()) {
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable("拷贝数据库文件失败"));
                }
            }
        }).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                SPUtil.getInstance().putBoolan(COPIED, true);
            }

            @Override
            public void onError(Throwable e) {
                SPUtil.getInstance().putBoolan(COPIED, false);
            }

            @Override
            public void onNext(Boolean succeed) {
                if (succeed) {
                    LogUtil.e("database", "拷贝成功");
                }
            }
        });
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

    /**
     * 初始化天气图标
     */
    private void initWeatherIcon() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                SPUtil.getInstance().putInt("未知", R.mipmap.none);
                SPUtil.getInstance().putInt("晴", R.mipmap.type_one_sunny);
                SPUtil.getInstance().putInt("阴", R.mipmap.type_one_cloudy);
                SPUtil.getInstance().putInt("多云", R.mipmap.type_one_cloudy);
                SPUtil.getInstance().putInt("少云", R.mipmap.type_one_cloudy);
                SPUtil.getInstance().putInt("晴间多云", R.mipmap.type_one_cloudytosunny);
                SPUtil.getInstance().putInt("小雨", R.mipmap.type_one_light_rain);
                SPUtil.getInstance().putInt("中雨", R.mipmap.type_one_light_rain);
                SPUtil.getInstance().putInt("大雨", R.mipmap.type_one_heavy_rain);
                SPUtil.getInstance().putInt("阵雨", R.mipmap.type_one_thunderstorm);
                SPUtil.getInstance().putInt("雷阵雨", R.mipmap.type_one_thunder_rain);
                SPUtil.getInstance().putInt("霾", R.mipmap.type_one_fog);
                SPUtil.getInstance().putInt("雾", R.mipmap.type_one_fog);
                SPUtil.getInstance().putBoolan(ICON_INITIALIZED, true);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();


    }
}

