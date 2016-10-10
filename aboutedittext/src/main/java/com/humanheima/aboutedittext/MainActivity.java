package com.humanheima.aboutedittext;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {

    private ScrollView mScrollView;
    private EditText usernamelogin_username, usernamelogin_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mScrollView = (ScrollView) findViewById(R.id.scrollview);
        usernamelogin_username = (EditText) findViewById(R.id.et_usernamelogin_username);
        usernamelogin_password = (EditText) findViewById(R.id.et_usernamelogin_password);
        usernamelogin_username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeScrollView();

                return false;
            }
        });
        usernamelogin_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeScrollView();

                return false;
            }
        });
    }


    /**
     * 使ScrollView指向底部
     */
    private void changeScrollView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("haha", "hhhhh" + mScrollView.getHeight());
                mScrollView.scrollTo(0, mScrollView.getHeight()-300);
            }
        }, 300);
    }
}
