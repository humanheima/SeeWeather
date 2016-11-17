package com.humanheima.androideventdispatchdemo.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by Administrator on 2016/11/14.
 */
public class MyButton extends Button {

    private String TAG = MyButton.class.getSimpleName();
    private int times = 0;
    private int lastX, lastY;

    public MyButton(Context context) {
        super(context);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                Log.e(TAG, "dispatchTouchEvent ACTION_DOWN");
                //不允许拦截touch事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int delX = x - lastX;
                int delY = y - lastY;
                //允许拦截
                Log.e(TAG, "dispatchTouchEvent ACTION_MOVE" + Math.abs(delX - delY));
                if (Math.abs(delX - delY) > 50) {
                    //允许拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "dispatchTouchEvent ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "dispatchTouchEvent ACTION_CANCEL");
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }


}
