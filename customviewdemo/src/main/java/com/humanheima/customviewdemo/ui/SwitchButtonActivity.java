package com.humanheima.customviewdemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.humanheima.customviewdemo.R;
import com.humanheima.customviewdemo.widget.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SwitchButtonActivity extends AppCompatActivity {

    @BindView(R.id.android_switch)
    Switch androidSwitch;
    @BindView(R.id.my_switch)
    SwitchButton mySwitch;
    @BindView(R.id.activity_switch_button)
    LinearLayout activitySwitchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_button);
        ButterKnife.bind(this);
        mySwitch.setOnSwitchChangeListener(new SwitchButton.OnSwitchChangeListener() {
            @Override
            public void onChange(boolean isChecked) {
                Log.e("onChange", "isChecked:" + isChecked);
            }
        });
    }
}
