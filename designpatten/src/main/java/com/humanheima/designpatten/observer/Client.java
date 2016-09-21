package com.humanheima.designpatten.observer;

/**
 * Created by Administrator on 2016/9/21.
 */

public class Client {
    void fun() {
        SpecificObservable observable = new SpecificObservable();
        //创建微信用户
        WeixinObserver user1 = new WeixinObserver("杨影枫");
        WeixinObserver user2 = new WeixinObserver("月眉儿");
        WeixinObserver user3 = new WeixinObserver("紫轩");
        //订阅公众号
        observable.attach(user1);
        observable.attach(user2);
        observable.attach(user3);
        //公众号更新发出消息给订阅的微信用户
        observable.notify("刘望舒的专栏更新了");
    }
    /**
     * 输出结果
     * 杨影枫-刘望舒的专栏更新了
     月眉儿-刘望舒的专栏更新了
     紫轩-刘望舒的专栏更新了
     */
}
