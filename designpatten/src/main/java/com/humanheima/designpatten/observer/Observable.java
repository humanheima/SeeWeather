package com.humanheima.designpatten.observer;

/**
 * Created by Administrator on 2016/9/21.
 * 抽象的被观察者
 */

public interface Observable {
    /**
     * 增加订阅者
     *
     * @param observer
     */
    void attach(Observer observer);

    /**
     * 删除订阅者
     *
     * @param observer
     */
    void detach(Observer observer);

    /**
     * 通知订阅者更新消息
     */
    void notify(String message);
}
