package com.humanheima.rxjavademo;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/9/14.
 */
public class LiftAllTransformer implements Observable.Transformer<Integer, String> {

    @Override
    public Observable<String> call(Observable<Integer> integerObservable) {
        return integerObservable.lift(new Observable.Operator<String, Integer>() {
            @Override
            public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber) {
                return new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        subscriber.onNext("hello" + integer);
                    }
                };
            }
        }).lift(new Observable.Operator<String, String>() {
            @Override
            public Subscriber<? super String> call(final Subscriber<? super String> subscriber) {
                return new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        subscriber.onNext("world" + s);
                    }
                };
            }
        });
    }

}
