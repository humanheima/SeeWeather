package com.humanheima.hmweather.ui.fragment;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.humanheima.hmweather.R;
import com.humanheima.hmweather.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by dmw on 2016/9/9.
 * 展示天气的fragment
 */
public class WeatherFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void initData() {
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.rainyDark, R.color.colorAccent);
    }

    @Override
    protected void bindEvent() {

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWeatherData();
            }
        });
    }

    private void getWeatherData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
            }
        }, 3000);
    }

}
