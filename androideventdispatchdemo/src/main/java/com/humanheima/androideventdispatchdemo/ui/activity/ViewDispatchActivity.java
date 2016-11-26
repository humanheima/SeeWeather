package com.humanheima.androideventdispatchdemo.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.humanheima.androideventdispatchdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewDispatchActivity extends AppCompatActivity {

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.img1)
    ImageView img1;
    private String TAG = ViewDispatchActivity.class.getSimpleName();
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dispatch);
        ButterKnife.bind(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick execute");
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ViewDispatchActivity.this,"长按事件",Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onLongClick execute");
                return true;
            }
        });
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "onTouch execute, down action " + event.getAction());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.e(TAG, "onTouch execute, MOVE action " + event.getAction());
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "onTouch execute, UP action " + event.getAction());
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

       /* img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick execute");
            }
        });*/
        img1.setClickable(true);
        img1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "onTouch execute, down action " + event.getAction());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.e(TAG, "onTouch execute, MOVE action " + event.getAction());
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "onTouch execute, UP action " + event.getAction());
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

}
