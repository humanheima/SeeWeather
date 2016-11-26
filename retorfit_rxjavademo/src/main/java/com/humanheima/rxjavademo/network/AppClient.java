package com.humanheima.rxjavademo.network;

import com.humanheima.rxjavademo.App;
import com.humanheima.rxjavademo.util.NetWorkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/25.
 */
public class AppClient {

    private static String tag = "AppClient";
    //缓存大小
    private static final long CACHE_SIZE = 1024 * 1024 * 50;
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;
    //private static final String BASE_URL="http://api.k780.com:88/?app=weather.history&weaid=1&date=2015-07-20&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
    private static final String BASE_URL = "http://api.k780.com:88";

    /**
     * 构建OkHttpClient实例（配置一些请求的全局参数）
     */
    private static void initClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        File cacheFile = new File(App.getInstance().getExternalCacheDir(), "okHttpCache");
        Cache cache = new Cache(cacheFile, CACHE_SIZE);
        builder.cache(cache);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        if (builder.interceptors() != null) {
            builder.interceptors().clear();
        }
        //处理拦截器，主要是做了个header和连接超时、读取超时设置，
       /* builder.addInterceptor(
                new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        String query = request.url().query();
                        Buffer buffer = new Buffer();
                        if (request.body() != null) {
                            request.body().writeTo(buffer);
                        }
                        Response response = chain.proceed(request);
                        BufferedSource source = response.body().source();
                        source.request(Long.MAX_VALUE);
                        Log.e(tag, "request path-->" + request.url());
                        Log.e(tag, "request query-->" + query);
                        Log.e(tag, "request body" + buffer.readUtf8());
                        Log.e(tag, "request response" + source.buffer().clone().readUtf8());
                        Log.e(tag, "request response" + source.buffer().clone().readUtf8());
                        Log.e(tag, "request response head" + response.headers());
                        return chain.proceed(request);
                    }
                }

        );*/
        builder.addInterceptor(new CacheInterceptor())
        .addNetworkInterceptor(new CacheInterceptor());
        builder.connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        builder.addInterceptor(loggingInterceptor);
        //通过build模式构建实例
        okHttpClient = builder.build();
    }

    /**
     * 构建retrofit实例
     *
     * @return
     */
    public static Retrofit retrofit() {
        if (retrofit == null) {
            initClient();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    static  class CacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //没有网络就读取本地缓存的数据
            if (!NetWorkUtil.isConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetWorkUtil.isConnected()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置(注掉部分)
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        //.header("Cache-Control", cacheControl)//这是从接口的注解@Headers上读到的head信息
                        .header("Cache-Control", "max-age=30")//有网络的时候保存30秒
                        .removeHeader("Pragma")
                        .build();
            } else {
                //没网络的时候保存6分钟
                int maxAge = 60 * 60;
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-age=" + maxAge)//only-if-cached:(仅为请求标头)请求:告知缓存者,我希望内容来自缓存，我并不关心被缓存响应,是否是新鲜的.
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }
}
