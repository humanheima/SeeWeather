package com.humanheima.androideventdispatchdemo.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/11/16.
 */
public class InnerInterceptRelativeLayout extends RelativeLayout {
    public InnerInterceptRelativeLayout(Context context) {
        super(context);
    }

    public InnerInterceptRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            return false;
        } else {
            //拦截除了down以外的事件
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("InterceptRelativeLayout","onTouchEvent");
        return true;
    }
}
