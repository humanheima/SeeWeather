package com.humanheima.customviewdemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.humanheima.customviewdemo.R;
import com.humanheima.customviewdemo.widget.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SwitchButtonActivity extends AppCompatActivity {

    @BindView(R.id.my_switch)
    SwitchButton mySwitch;
    @BindView(R.id.activity_switch_button)
    LinearLayout activitySwitchButton;
    @BindView(R.id.btn_control)
    Button btnControl;
    @BindView(R.id.my_switch_selected)
    SwitchButton mySwitchSelected;
    private boolean checked;

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
        btnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked=!checked;
                mySwitch.setCheck(checked);
            }
        });
    }
}
