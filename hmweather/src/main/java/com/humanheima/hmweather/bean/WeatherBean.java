package com.humanheima.hmweather.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dmw on 2016/9/9.
 */
public class WeatherBean implements Serializable {

    @SerializedName("HeWeather data service 3.0")
    private List<HeWeather> weatherList;

    public List<HeWeather> getWeatherList() {
        return weatherList;
    }

}
