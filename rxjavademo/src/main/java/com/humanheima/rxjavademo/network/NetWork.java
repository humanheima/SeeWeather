package com.humanheima.rxjavademo.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/9/9.
 */
public class NetWork {

    private static API api;
    private static OkHttpClient okHttpClient;
    private static final String BASE_URL = "https://api.heweather.com/x3/";

    public static API getApi() {
        if (api == null) {
            synchronized (NetWork.class) {
                okHttpClient = new OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)
                        .readTimeout(200, TimeUnit.SECONDS)
                        .writeTimeout(300, TimeUnit.SECONDS)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .client(okHttpClient)
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
                api = retrofit.create(API.class);
            }
        }
        return api;
    }
}
