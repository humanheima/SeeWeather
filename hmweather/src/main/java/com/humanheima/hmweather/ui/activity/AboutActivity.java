package com.humanheima.hmweather.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.humanheima.hmweather.R;
import com.humanheima.hmweather.base.BaseActivity;
import com.humanheima.hmweather.utils.CheckVersionUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dumingwei on 2016/9/26.
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.bannner)
    ImageView imgBannner;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.btn_update)
    Button btnUpdate;

    @Override
    protected int bindLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        tvVersion.setText(CheckVersionUtil.getCurrentVersion(this));
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void bindEvent() {

    }

    @OnClick(R.id.btn_update)
    public void checkUpdate() {
        CheckVersionUtil.checkVersion(this);
    }

}
