package com.humanheima.citylistdemo.bean;


/**
 * Created by tangyangkai on 16/7/26.
 */
public class City {

    private String cityPinyin;
    private String cityName;
    private String firstPinYin;


    public String getCityPinyin() {
        return cityPinyin;
    }

    public void setCityPinyin(String cityPinyin) {
        this.cityPinyin = cityPinyin;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }


    public String getFirstPinYin() {
        firstPinYin = cityPinyin.substring(0, 1);
        return firstPinYin;
    }
}
