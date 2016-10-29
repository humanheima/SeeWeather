package com.humanheima.androideventdispatchdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.img1)
    ImageView img1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "onClick execute");
            }
        });
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("TAG", "onTouch execute, down action " + event.getAction());
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        Log.e("TAG", "onTouch execute, MOVE action " + event.getAction());
                        return false;
                    case MotionEvent.ACTION_UP:
                        Log.e("TAG", "onTouch execute, UP action " + event.getAction());
                        return false;
                    default:
                        return false;
                }
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "onClick execute");
            }
        });
        /*img1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("TAG", "onTouch execute, down action " + event.getAction());
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        Log.e("TAG", "onTouch execute, MOVE action " + event.getAction());
                        return true;
                    case MotionEvent.ACTION_UP:
                        Log.e("TAG", "onTouch execute, UP action " + event.getAction());
                        return true;
                    default:
                        return true;
                }
            }
        });*/
    }

}
