package com.humanheima.hmweather.base;

import com.humanheima.hmweather.utils.LogUtil;

import rx.Subscriber;

/**
 * Created by dumignwei on 2016/9/27.
 */

public abstract class SimpleSubscriber<T> extends Subscriber<T> {
    private static final String tag = SimpleSubscriber.class.getSimpleName();

    @Override
    public void onCompleted() {
        LogUtil.e(tag, "onCompleted");
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(tag, e.getMessage());
    }

}
