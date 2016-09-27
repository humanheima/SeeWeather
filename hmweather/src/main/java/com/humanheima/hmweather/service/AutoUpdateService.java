package com.humanheima.hmweather.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.humanheima.hmweather.base.SimpleSubscriber;
import com.humanheima.hmweather.bean.HeWeather;
import com.humanheima.hmweather.bean.LocalWeather;
import com.humanheima.hmweather.bean.WeatherBean;
import com.humanheima.hmweather.bean.WeatherCode;
import com.humanheima.hmweather.network.NetWork;
import com.humanheima.hmweather.utils.GsonUtil;
import com.humanheima.hmweather.utils.ListUtil;
import com.humanheima.hmweather.utils.LogUtil;
import com.humanheima.hmweather.utils.SPUtil;
import com.humanheima.hmweather.utils.WeatherKey;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class AutoUpdateService extends Service {
    private final String tag = AutoUpdateService.class.getSimpleName();
    private CompositeSubscription compositeSubscription;
    private Subscription subscription;
    private boolean isUnsubscribed = true;

    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e(tag, "onStartCommand");
        synchronized (this) {
            unSubscribed();
            if (isUnsubscribed) {
                if (SPUtil.getInstance().getAutoUpdate() != 0) {
                    LogUtil.e(tag, "if onStartCommand");
                    subscription = rx.Observable.interval(SPUtil.getInstance().getAutoUpdate(), TimeUnit.HOURS)
                            .subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    fetchDataByNetWork();
                                }
                            });
                    compositeSubscription.add(subscription);
                } else {
                    LogUtil.e(tag, "禁止自动更新数据");
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
                List<LocalWeather> weathers = DataSupport.select("weaid").find(LocalWeather.class);
                List<WeatherCode> weatherCodes = new ArrayList<>();
                if (ListUtil.notEmpty(weathers)) {
                    for (LocalWeather weather : weathers) {
                        WeatherCode code = new WeatherCode(weather.getWeaId());
                        weatherCodes.add(code);
                    }
                    subscriber.onNext(weatherCodes);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable("数据库中没有天气id"));
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .concatMap(new Func1<List<WeatherCode>, Observable<WeatherCode>>() {
                    @Override
                    public Observable<WeatherCode> call(List<WeatherCode> weatherCodes) {
                        return Observable.from(weatherCodes);
                    }
                }).concatMapDelayError(new Func1<WeatherCode, Observable<WeatherBean>>() {
            @Override
            public Observable<WeatherBean> call(WeatherCode weatherCode) {
                return NetWork.getApi().getWeatherByPost(weatherCode.getCode(), WeatherKey.key);
            }
        }).concatMap(new Func1<WeatherBean, Observable<HeWeather>>() {
            @Override
            public Observable<HeWeather> call(WeatherBean weatherBean) {
                return Observable.just(weatherBean.getWeatherList().get(0));
            }
        }).subscribe(new SimpleSubscriber<HeWeather>() {
            @Override
            public void onNext(HeWeather heWeather) {
                saveWeaInfo(heWeather);
            }
        });
    }

    /**
     * 如果id已存在，则删除再存
     *
     * @param heWeather
     */
    private void saveWeaInfo(final HeWeather heWeather) {
        final LocalWeather localWeather = new LocalWeather();
        localWeather.setWeaId(heWeather.getBasic().getId());
        localWeather.setWeaInfo(GsonUtil.toJson(heWeather));
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                int count = localWeather.updateAll("weaid=?", heWeather.getBasic().getId());
                if (count > 0) {
                    subscriber.onNext(true);
                } else {
                    subscriber.onError(new Throwable("存储天气信息失败"));
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.e(tag, "存储天气信息成功");
                    }
                });
    }

    /**
     * 取消订阅
     */
    private void unSubscribed() {
        LogUtil.e(tag, "unSubscribed");
        isUnsubscribed = true;
        compositeSubscription.remove(subscription);
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }
}
