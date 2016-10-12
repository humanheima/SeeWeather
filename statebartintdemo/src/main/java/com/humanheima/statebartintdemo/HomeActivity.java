package com.humanheima.statebartintdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.btn_main)
    Button btnMain;
    @BindView(R.id.btn_main2)
    Button btnMain2;
    @BindView(R.id.btn_main3)
    Button btnMain3;
    @BindView(R.id.btn_main4)
    Button btnMain4;
    @BindView(R.id.btn_main5)
    Button btnMain5;
    @BindView(R.id.activity_home)
    LinearLayout activityHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_main, R.id.btn_main2, R.id.btn_main3, R.id.btn_main4, R.id.btn_main5, R.id.activity_home})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_main:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btn_main2:
                startActivity(new Intent(this, Main2Activity.class));
                break;
            case R.id.btn_main3:
                startActivity(new Intent(this, Main3Activity.class));
                break;
            case R.id.btn_main4:
                startActivity(new Intent(this, Main4Activity.class));
                break;
            case R.id.btn_main5:
                startActivity(new Intent(this, Main5Activity.class));
                break;
            default:
                break;
        }
    }
}
