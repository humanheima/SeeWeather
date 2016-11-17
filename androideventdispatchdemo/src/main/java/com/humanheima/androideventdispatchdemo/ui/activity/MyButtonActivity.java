package com.humanheima.androideventdispatchdemo.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.humanheima.androideventdispatchdemo.R;
import com.humanheima.androideventdispatchdemo.ui.widget.MyButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyButtonActivity extends AppCompatActivity {

    private String TAG = MyButtonActivity.class.getSimpleName();
    @BindView(R.id.my_btn)
    MyButton myBtn;
    View view;
    ViewGroup viewGroup;
    private int nums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_button);
        ButterKnife.bind(this);
        myBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                Log.e(TAG, "onTouch nums" + nums);
                nums++;
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "onTouch ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.e(TAG, "onTouch ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "onTouch ACTION_UP");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
}
