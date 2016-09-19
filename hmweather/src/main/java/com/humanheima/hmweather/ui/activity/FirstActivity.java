package com.humanheima.hmweather.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.humanheima.hmweather.R;
import com.humanheima.hmweather.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class FirstActivity extends BaseActivity {

    private static final int START_MAINACTIVITY = 1;
    private static final int START_DELAY = 1;//延迟时间1秒
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == START_MAINACTIVITY) {
                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(intent);
                //activity切换的淡入淡出效果
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }

        }
    };

    @Override
    protected int bindLayout() {
        return R.layout.activity_first;
    }

    @Override
    protected void initData() {
      /*  if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/
    }

    @Override
    protected void bindEvent() {
        //handler.sendEmptyMessageDelayed(START_MAINACTIVITY, START_DELAY);
        startActDelay();
    }

    private void startActDelay() {
        Observable.timer(START_DELAY, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {
                        Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                        startActivity(intent);
                        //activity切换的淡入淡出效果
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        return null;
                    }
                }).subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(START_MAINACTIVITY);
        handler = null;
    }
}
