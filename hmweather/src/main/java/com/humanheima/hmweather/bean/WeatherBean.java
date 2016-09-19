package com.humanheima.hmweather.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dmw on 2016/9/9.
 */
public class WeatherBean {

    @SerializedName("HeWeather data service 3.0")
    private List<HeWeather> weatherList;

    public List<HeWeather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<HeWeather> weatherList) {
        this.weatherList = weatherList;
    }
}
