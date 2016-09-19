package com.humanheima.hmweather.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by dmw on 2016/9/19.
 * 存在本地的天气信息
 */
public class LocalWeather extends DataSupport {

    private String weaId;
    private String weaInfo;

    public String getWeaId() {
        return weaId;
    }

    public void setWeaId(String weaId) {
        this.weaId = weaId;
    }

    public String getWeaInfo() {
        return weaInfo;
    }

    public void setWeaInfo(String weaInfo) {
        this.weaInfo = weaInfo;
    }
}
