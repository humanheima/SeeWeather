package com.humanheima.hmweather.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.humanheima.hmweather.bean.LocalWeather;
import com.humanheima.hmweather.bean.WeatherCode;
import com.humanheima.hmweather.utils.SPUtil;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class AutoUpdateService extends Service {
    private final String TAG = AutoUpdateService.class.getSimpleName();
    private CompositeSubscription compositeSubscription;
    private Subscription subscription;
    private boolean isUnsubscribed = true;

    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //  throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synchronized (this) {
            unSubscribed();
            if (isUnsubscribed) {
                if (SPUtil.getInstance().getAutoUpdate() != 0) {
                    subscription = rx.Observable.interval(SPUtil.getInstance().getAutoUpdate(), TimeUnit.HOURS)
                            .subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    fetchDataByNetWork();
                                }
                            });
                    compositeSubscription.add(subscription);
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 从网络加载数据并保存到数据库
     */
    private void fetchDataByNetWork() {
        Observable.create(new Observable.OnSubscribe<List<WeatherCode>>() {
            @Override
            public void call(Subscriber<? super List<WeatherCode>> subscriber) {
              List<LocalWeather> weathers=  DataSupport.select("weaid").find(LocalWeather.class);

            }
        });
    }

    /**
     * 取消订阅
     */
    private void unSubscribed() {
        isUnsubscribed = true;
        compositeSubscription.remove(subscription);
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }
}
