package com.humanheima.designpatten.adapter;

/**
 * description:实际情况：小伙只会中文，所以得需要个有道词典啥的。
 * 有道词典就相当于一个转化器，可以把chinese 转成Ukrainian。
 */
public class Chinese {
    void sayChinese(String string) {
        System.out.println("【中文版】 " + string);
    }
}
