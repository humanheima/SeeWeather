package com.humanheima.hmweather.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by dmw on 2016/9/9.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(bindLayout());
        ButterKnife.bind(this);
        initData();
        bindEvent();
    }

    /**
     * 绑定布局文件
     *
     * @return
     */
    protected abstract int bindLayout();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 绑定控件事件
     */
    protected abstract void bindEvent();

}
