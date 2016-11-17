package com.humanheima.androideventdispatchdemo.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.humanheima.androideventdispatchdemo.R;
import com.humanheima.androideventdispatchdemo.ui.widget.OutInterceptLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 外部拦截ACTION_MOVE事件
 */
public class OutInterceptActivity extends AppCompatActivity {

    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.activity_my_layout)
    OutInterceptLinearLayout myLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_layout);
        ButterKnife.bind(this);
        /**
         * 因为myLayout拦截了ACTION_MOVE事件，所以button的OnClick事件无法执行。
         */
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MyLayout", "You clicked button2");
            }
        });
    }
}
