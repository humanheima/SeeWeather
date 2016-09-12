package com.humanheima.hmweather.utils;

import com.humanheima.hmweather.bean.CityInfo;

import java.util.Comparator;

/**
 * Created by dmw on 2016/9/12.
 * 拼音比较器
 */
public class PinYinComparator implements Comparator<CityInfo> {

    @Override
    public int compare(CityInfo c1, CityInfo c2) {
        return c1.getCityPinyin().compareTo(c2.getCityPinyin());
    }
}
