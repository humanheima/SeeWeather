package com.humanheima.designpatten.observer;

/**
 * Created by dumingwei on 2016/9/21.
 * 具体的观察者要实现Observer接口
 */

public class WeixinObserver implements Observer {
    // 微信用户名
    private String name;

    public WeixinObserver(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println(name + "-" + message);
    }

}
