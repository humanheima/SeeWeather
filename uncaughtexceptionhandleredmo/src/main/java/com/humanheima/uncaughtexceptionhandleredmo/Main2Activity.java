package com.humanheima.uncaughtexceptionhandleredmo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.btn_exit)
    Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ActivityCollector.addActivity(this);
        //AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_exit)
    public void onClick() {
        Log.e("Main2Activity", "Main2Activity抛出异常");
        throw new NullPointerException("手动抛出异常");
    }
}
