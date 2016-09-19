package com.humanheima.hmweather.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by dmw on 2016/9/12.
 */
public class WeatherCode extends DataSupport {

    private String code;//天气代码

    public WeatherCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
