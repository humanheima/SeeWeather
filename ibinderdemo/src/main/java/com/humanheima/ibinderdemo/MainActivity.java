package com.humanheima.ibinderdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        UserManager.num = 2;
        Log.e("tag", "在MainActivity中，num=" + UserManager.num);
    }

    @OnClick(R.id.btn)
    public void startSecondAct() {
        startActivity(new Intent(MainActivity.this, SecondActivity.class));
    }
}
