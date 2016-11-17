package com.humanheima.androideventdispatchdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.humanheima.androideventdispatchdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends AppCompatActivity {

    @BindView(R.id.btn_start_MyButtonActivity)
    Button btnStartMyButtonActivity;
    @BindView(R.id.btn_start_MainActivity)
    Button btnStartMainActivity;
    @BindView(R.id.btn_start_MyLayoutActivity)
    Button btnStartMyLayoutActivity;
    @BindView(R.id.btn_start_InnerIntercept_Activity)
    Button btnStartInnerInterceptActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_start_MyButtonActivity)
    public void startMyButtonActivity() {
        startActivity(new Intent(StartActivity.this, MyButtonActivity.class));
    }

    @OnClick(R.id.btn_start_MainActivity)
    public void startMainActivity() {
        startActivity(new Intent(StartActivity.this, MainActivity.class));
    }

    @OnClick(R.id.btn_start_MyLayoutActivity)
    public void startMyLayoutActivity() {
        startActivity(new Intent(StartActivity.this, OutInterceptActivity.class));
    }

    @OnClick(R.id.btn_start_InnerIntercept_Activity)
    public void onClick() {
        startActivity(new Intent(StartActivity.this, InnerInterceptActivity.class));
    }
}
