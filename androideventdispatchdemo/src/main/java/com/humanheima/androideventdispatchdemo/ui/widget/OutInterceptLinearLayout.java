package com.humanheima.androideventdispatchdemo.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * 外部拦截
 */
public class OutInterceptLinearLayout extends LinearLayout {

    private int lastX, lastY;

    public OutInterceptLinearLayout(Context context) {
        super(context);
    }

    public OutInterceptLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("MyLayout", "MyLayout dispatchTouchEvent" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int delX = x - lastX;
                int delY = y - lastY;
                //允许拦截
                Log.e("OutInterceptLinear", "dispatchTouchEvent ACTION_MOVE" + Math.abs(delX - delY));
                if (Math.abs(delX - delY) > 50) {
                    //拦截move事件，onTouchEvent(MotionEvent event)返回true
                    intercepted = true;
                } else {
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
        }
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("MyLayout", "MyLayout onTouchEvent" + event.getAction());
        return true;
    }
}
