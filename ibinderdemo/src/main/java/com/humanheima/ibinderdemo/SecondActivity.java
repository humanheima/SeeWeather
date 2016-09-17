package com.humanheima.ibinderdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecondActivity extends AppCompatActivity {

    @BindView(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
        Log.e("tag", "在SecondActivity中，num=" + UserManager.num);
    }

    @OnClick(R.id.btn)
    public void startThirdAct() {
        startActivity(new Intent(SecondActivity.this, ThirdActivity.class));
    }
}
