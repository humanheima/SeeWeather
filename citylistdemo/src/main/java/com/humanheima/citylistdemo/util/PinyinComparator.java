package com.humanheima.citylistdemo.util;

import com.humanheima.citylistdemo.bean.City;

import java.util.Comparator;

public class PinyinComparator implements Comparator<City> {
    @Override
    public int compare(City cityFirst, City citySecond) {
        return cityFirst.getCityPinyin().compareTo(citySecond.getCityPinyin());
    }
}