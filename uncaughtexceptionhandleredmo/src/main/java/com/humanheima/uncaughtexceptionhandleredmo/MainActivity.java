package com.humanheima.uncaughtexceptionhandleredmo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_start)
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //AppManager.getAppManager().addActivity(this);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_start)
    public void onClick() {
        startActivity(new Intent(this, Main2Activity.class));
    }
}
