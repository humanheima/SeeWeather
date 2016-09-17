package com.humanheima.hmweather.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by dmw on 2016/9/12.
 */
public class RxBus {
    private static volatile RxBus rxBus;

    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    private RxBus() {

    }

    public static RxBus getInstance() {
        if (rxBus == null) {
            synchronized (RxBus.class) {
                if (rxBus == null) {
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }

    public void send(Object obj) {
        bus.onNext(obj);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }
}
