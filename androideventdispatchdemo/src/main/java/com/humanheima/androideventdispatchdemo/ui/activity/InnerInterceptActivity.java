package com.humanheima.androideventdispatchdemo.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.humanheima.androideventdispatchdemo.R;
import com.humanheima.androideventdispatchdemo.ui.widget.InnerInterceptRelativeLayout;
import com.humanheima.androideventdispatchdemo.ui.widget.MyButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 内部拦截
 */
public class InnerInterceptActivity extends AppCompatActivity {

    @BindView(R.id.my_btn_inner)
    MyButton myBtnInner;
    @BindView(R.id.rl_inner_intercept)
    InnerInterceptRelativeLayout rlInnerIntercept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_intercept);
        ButterKnife.bind(this);
    }
}
