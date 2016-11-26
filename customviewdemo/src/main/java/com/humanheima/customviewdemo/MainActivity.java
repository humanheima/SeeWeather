package com.humanheima.customviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.humanheima.customviewdemo.ui.CircleViewActivity;
import com.humanheima.customviewdemo.ui.ScrollerLayoutActivity;
import com.humanheima.customviewdemo.ui.SwitchButtonActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于自定义view
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_start_circle)
    Button btnStartCircle;
    @BindView(R.id.btn_start_scroller)
    Button btnStartScroller;
    @BindView(R.id.btn_start_switch)
    Button btnStartSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_start_circle)
    public void onClick() {
        startActivity(new Intent(MainActivity.this, CircleViewActivity.class));
    }

    @OnClick(R.id.btn_start_scroller)
    public void onClick1() {
        startActivity(new Intent(MainActivity.this, ScrollerLayoutActivity.class));
    }

    @OnClick(R.id.btn_start_switch)
    public void onClick2() {
        startActivity(new Intent(MainActivity.this, SwitchButtonActivity.class));
    }

}
