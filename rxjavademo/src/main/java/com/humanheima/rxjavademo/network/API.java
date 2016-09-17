package com.humanheima.rxjavademo.network;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by dmw on 2016/9/9.
 */
public interface API {
    //@GET("weather?cityid=CN101020100&key=fcaa02b41e9048e7aa5854b1e279e1c6")
    //Observable<WeatherBean> getWeather();
    @GET("weather")
    Observable<String> getWeather(@Query("cityid") String cityId, @Query("key") String key);

    //测试RxJava的操作符这个方法根据输入的字符串返回一个网站的url列表
    Observable<List<String>> query(String text);

    // 返回网站的标题，如果404了就返回null
    Observable<String> getTitle(String URL);
}

