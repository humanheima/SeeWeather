package com.humanheima.hmweather.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.humanheima.hmweather.R;
import com.humanheima.hmweather.base.BaseFragment;
import com.humanheima.hmweather.bean.HeWeather;
import com.humanheima.hmweather.bean.LocalWeather;
import com.humanheima.hmweather.bean.WeatherBean;
import com.humanheima.hmweather.network.NetWork;
import com.humanheima.hmweather.ui.adapter.WeatherRVAdapter;
import com.humanheima.hmweather.utils.LogUtil;
import com.humanheima.hmweather.utils.NetWorkUtil;
import com.humanheima.hmweather.utils.WeatherKey;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
    private HeWeather heWeather;
    private static final String WEA_ID = "weaid";
    private static final String tag = "WeatherFragment";
    public String weaId;
    private String weaInfo;
    private Gson gson;

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
        gson = new Gson();
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

    /**
     * 如果有网络，就从网路加载数据，否则，就从本地加载数据
     *
     * @param code
     */
    private void loadWeather(String code) {
        final String weatherCode = code;
        if (NetWorkUtil.isConnected()) {
            NetWork.getApi().getWeatherByPost(weatherCode, WeatherKey.key)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(new Func1<WeatherBean, HeWeather>() {
                        @Override
                        public HeWeather call(WeatherBean weatherBean) {
                            return weatherBean.getWeatherList().get(0);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<HeWeather>() {
                        @Override
                        public void call(HeWeather weather) {
                            heWeather = weather;
                            setAdapter();
                            //把天气信息存起来
                            saveWeaInfo(heWeather);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            LogUtil.e(tag, throwable.getMessage());
                        }
                    });
        } else {
            loadDBWeather(code);
        }

    }

    /**
     * 加载本地天气
     *
     * @param code
     */
    private void loadDBWeather(final String code) {

        Observable.create(new Observable.OnSubscribe<LocalWeather>() {
            @Override
            public void call(Subscriber<? super LocalWeather> subscriber) {
                LocalWeather localWeather = DataSupport.where("weaid=?", code).findFirst(LocalWeather.class);
                if (localWeather != null) {
                    subscriber.onNext(localWeather);
                } else {
                    subscriber.onError(new Throwable("查找本地天气失败"));
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<LocalWeather, HeWeather>() {
                    @Override
                    public HeWeather call(LocalWeather localWeather) {
                        HeWeather heWeather = gson.fromJson(localWeather.getWeaInfo(), HeWeather.class);
                        return heWeather;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HeWeather>() {
                    @Override
                    public void call(HeWeather weather) {
                        heWeather = weather;
                        setAdapter();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtil.e(tag, throwable.getMessage());
                    }
                });
    }


    /**
     * 如果id已存在，则删除再存
     *
     * @param heWeather
     */
    private void saveWeaInfo(HeWeather heWeather) {
        final LocalWeather localWeather = new LocalWeather();
        localWeather.setWeaId(heWeather.getBasic().getId());
        localWeather.setWeaInfo(gson.toJson(heWeather));
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                DataSupport.deleteAll(LocalWeather.class, "weaid=?", weaId);
                if (localWeather.save()) {
                    subscriber.onNext(true);
                } else {
                    subscriber.onError(new Throwable("存储天气信息失败"));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean succeed) {
                        LogUtil.e(tag, "存储天气信息成功");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtil.e(tag, throwable.getMessage());
                    }
                });
    }

    private void setAdapter() {
        if (adapter == null) {
            adapter = new WeatherRVAdapter(getContext(), heWeather);
            //adapter = new WeatherRVAdapter(getContext(), heWeatherList);
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
