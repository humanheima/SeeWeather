package com.humanheima.designpatten.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 * 具体的观察者
 */

public class SpecificObservable implements Observable {

    //储存订阅公众号的微信用户
    private List<Observer> weixinUserlist = new ArrayList<Observer>();

    @Override
    public void attach(Observer observer) {
        weixinUserlist.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        weixinUserlist.remove(observer);
    }

    @Override
    public void notify(String message) {
        for (Observer obsever : weixinUserlist) {
            obsever.update(message);
        }
    }
}
