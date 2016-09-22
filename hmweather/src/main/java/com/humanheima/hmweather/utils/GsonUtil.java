package com.humanheima.hmweather.utils;

import com.google.gson.Gson;

/**
 * Created by dumingwei on 2016/9/22.
 */

public class GsonUtil {
    private static Gson gson = new Gson();

    public static <T extends Object> String toJson(T t) {
        return gson.toJson(t);
    }

    public static <T> T fromJson(String jsonElement, Class<T> classOfT) {
        return gson.fromJson(jsonElement, classOfT);
    }

    public static <T> T toObject(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}
