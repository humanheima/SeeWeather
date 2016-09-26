package com.humanheima.hmweather.network;

import com.humanheima.hmweather.bean.Version;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/9/26.
 */

public interface VersionApi {

    //而且在Retrofit 2.0中我们还可以在@Url里面定义完整的URL：这种情况下Base URL会被忽略。
    @GET("http://api.fir.im/apps/latest/57e8b0e8959d690950000b28")
    Observable<Version> mVersionAPI(@Query("api_token") String api_token);
}
