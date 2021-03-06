package com.humanheima.hmweather.network;

import com.humanheima.hmweather.bean.CityInfoList;
import com.humanheima.hmweather.bean.Version;
import com.humanheima.hmweather.bean.WeatherBean;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by dmw on 2016/9/9.
 */
public interface API {
    //@GET("weather?cityid=CN101020100&key=fcaa02b41e9048e7aa5854b1e279e1c6")
    //Observable<WeatherBean> getWeather();
    @GET("weather")
    Observable<WeatherBean> getWeather(@Query("cityid") String cityId, @Query("key") String key);

    @GET("citylist")
    Observable<CityInfoList> getCityInfoList(@Query("search") String type, @Query("key") String key);
    //https://api.heweather.com/x3/citylist?search=allchina&key=fcaa02b41e9048e7aa5854b1e279e1c6

    @GET("weather")
    Observable<WeatherBean> getWeather(@QueryMap() Map<String, String> map);

    @POST("weather")
    Observable<WeatherBean> getWeatherByPost(@Query("cityid") String cityId, @Query("key") String key);

    @FormUrlEncoded
    @POST("weather")
    Observable<WeatherBean> getWeatherByPostField(@Field("cityid") String cityId, @Field("key") String key);

    @POST("weather")
    Observable<WeatherBean> getWeatherByPostMap(@QueryMap() Map<String, String> map);

    //而且在Retrofit 2.0中我们还可以在@Url里面定义完整的URL：这种情况下Base URL会被忽略。
    @GET("http://api.fir.im/apps/latest/57e8b0e8959d690950000b28")
    Observable<Version> mVersionAPI(@Query("api_token") String api_token);
}

