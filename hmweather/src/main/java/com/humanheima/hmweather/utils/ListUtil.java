package com.humanheima.hmweather.utils;

import java.util.List;

/**
 * Created by dumingwei on 2016/9/23.
 * 判断list是否为空
 */

public class ListUtil {

    public static boolean notEmpty(List list) {
        return (list != null && list.size() > 0);
    }
}
