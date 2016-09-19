package com.humanheima.hmweather.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.humanheima.hmweather.R;
import com.humanheima.hmweather.base.BaseFragment;
import com.humanheima.hmweather.bean.HeWeather;
import com.humanheima.hmweather.bean.WeatherBean;
import com.humanheima.hmweather.network.NetWork;
import com.humanheima.hmweather.ui.adapter.WeatherRVAdapter;
import com.humanheima.hmweather.utils.LogUtil;
import com.humanheima.hmweather.utils.WeatherKey;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dmw on 2016/9/9.
 * 展示天气的fragment
 */
public class WeatherFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    WeatherRVAdapter adapter;
    private List<HeWeather> heWeatherList;
    private static final String WEA_ID = "weaid";
    private static final String tag = "WeatherFragment";
    private String weaId;

    public static WeatherFragment newInstance(String weaId) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(WEA_ID, weaId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void initData() {
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.rainyDark, R.color.colorAccent);
        if (getArguments() != null) {
            weaId = getArguments().getString(WEA_ID);
            LogUtil.e(tag, weaId);
            loadWeather(weaId);
        }

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

    private void loadWeather(String code) {
        String weatherCode = code;
        NetWork.getApi().getWeatherByPost(weatherCode, WeatherKey.key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherBean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.e("getWeather", "onCompleted");
                        setAdapter();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("getWeather", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(WeatherBean weatherBean) {
                        heWeatherList=weatherBean.getWeatherList();
                        //LogUtil.e("getWeather", "" + heWeather.getStatus() + "," + heWeather.getBasic().getCity());
                    }
                });
    }

    private void setAdapter() {
        if (adapter == null) {
           // adapter = new WeatherRVAdapter(getContext(), mWeatherBean);
            adapter = new WeatherRVAdapter(getContext(), heWeatherList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }
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
