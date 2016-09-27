package com.humanheima.hmweather.ui.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.humanheima.hmweather.R;
import com.humanheima.hmweather.base.BaseActivity;
import com.humanheima.hmweather.base.SimpleSubscriber;
import com.humanheima.hmweather.bean.HeWeather;
import com.humanheima.hmweather.bean.LocalWeather;
import com.humanheima.hmweather.bean.WeatherBean;
import com.humanheima.hmweather.bean.WeatherCode;
import com.humanheima.hmweather.network.NetWork;
import com.humanheima.hmweather.service.AutoUpdateService;
import com.humanheima.hmweather.ui.adapter.RvAdapter;
import com.humanheima.hmweather.utils.DoubleClickExit;
import com.humanheima.hmweather.utils.GsonUtil;
import com.humanheima.hmweather.utils.ListUtil;
import com.humanheima.hmweather.utils.LogUtil;
import com.humanheima.hmweather.utils.NetWorkUtil;
import com.humanheima.hmweather.utils.RxBus;
import com.humanheima.hmweather.utils.SPUtil;
import com.humanheima.hmweather.utils.T;
import com.humanheima.hmweather.utils.WeatherKey;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dmw on 2016/9/9.
 * 1：第一次使用的时候默认加载闵行的天气，为什么呢》因为mh倒过来就是hm啊！
 * 如果是2G，或者没有网络的情况下，就从数据库查询
 * 如果是添加城市的话有网络就加载数据，更新adapter，否则提示网络
 */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    private final static String tag = "MainActivity";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rl_loading)
    RelativeLayout rlLoading;
    private String FIRST_USE = "firstUse";
    private static String MINHANG = "CN101020200";//默认城市上海闵行
    private List<HeWeather> weatherList;
    private List<HeWeather> updateWeatherList;
    private RvAdapter rvAdapter;
    private LinearLayoutManager layoutManager;
    LinearSnapHelper helper;

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        weatherList = new ArrayList<>();
        updateWeatherList = new ArrayList<>();
        if (!SPUtil.getInstance().getBoolean(FIRST_USE)) {
            //第一次使用，加载默认城市的天气预报，
            LogUtil.e(tag, "first use");
            addCityWeather(new WeatherCode(MINHANG));
        } else {
            //从多个数据源获取数据
            getWeatherFromDB();
        }

    }


    @Override
    protected void bindEvent() {
        RxBus.getInstance().toObservable(WeatherCode.class).subscribe(new Action1<WeatherCode>() {
            @Override
            public void call(WeatherCode weatherCode) {
                //添加一个城市的天气fragment
                addCityWeather(weatherCode);
            }
        });
        LogUtil.e(tag, "开启自动更新服务");
        startService(new Intent(this, AutoUpdateService.class));
    }

    /**
     * 从数据库获取天气信息
     */
    private void getWeatherFromDB() {
        Observable.create(new Observable.OnSubscribe<List<LocalWeather>>() {
            @Override
            public void call(Subscriber<? super List<LocalWeather>> subscriber) {
                List<LocalWeather> localWeatherList = DataSupport.findAll(LocalWeather.class);
                if (localWeatherList.size() > 0) {
                    subscriber.onNext(localWeatherList);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Exception("没有本地信息"));
                }
            }
        }).map(new Func1<List<LocalWeather>, List<HeWeather>>() {
            @Override
            public List<HeWeather> call(List<LocalWeather> localWeathers) {
                if (localWeathers.size() > 0) {
                    List<HeWeather> heWeathers = new ArrayList<HeWeather>();
                    for (int i = 0; i < localWeathers.size(); i++) {
                        heWeathers.add(GsonUtil.fromJson(localWeathers.get(i).getWeaInfo(), HeWeather.class));
                    }
                    return heWeathers;
                }
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        rlLoading.setVisibility(View.VISIBLE);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<HeWeather>>() {
                    @Override
                    public void onCompleted() {
                        rlLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(tag, e.getMessage());
                        rlLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(List<HeWeather> heWeathers) {
                        if (heWeathers != null && heWeathers.size() > 0) {
                            weatherList.addAll(heWeathers);
                            setRvAdapter();
                        }
                    }
                });
    }

    /**
     * 首先存储weatherCode，
     * 然后增加天气信息，
     * 现在还没有判断weaId是否重复
     *
     * @param weatherCode
     */
    private void addCityWeather(final WeatherCode weatherCode) {
        if (NetWorkUtil.isConnected()) {
            Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    LocalWeather localWeather = DataSupport.where("weaid=?", weatherCode.getCode()).findFirst(LocalWeather.class);
                    if (localWeather != null) {
                        subscriber.onNext(true);
                    } else {
                        subscriber.onNext(false);
                    }
                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.io())
                    .flatMap(new Func1<Boolean, Observable<WeatherBean>>() {
                        @Override
                        public Observable<WeatherBean> call(Boolean succeed) {
                            if (!succeed) {
                                return NetWork.getApi().getWeatherByPost(weatherCode.getCode(), WeatherKey.key);
                            }
                            return null;
                        }
                    })
                    .flatMap(new Func1<WeatherBean, Observable<HeWeather>>() {
                        @Override
                        public Observable<HeWeather> call(WeatherBean weatherBean) {
                            if (weatherBean != null) {
                                return Observable.just(weatherBean.getWeatherList().get(0));
                            }
                            return null;
                        }
                    }).doOnSubscribe(new Action0() {
                @Override
                public void call() {
                    rlLoading.setVisibility(View.VISIBLE);
                }
            })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<HeWeather>() {
                        @Override
                        public void onCompleted() {
                            if (!SPUtil.getInstance().getBoolean(FIRST_USE)) {
                                SPUtil.getInstance().putBoolan(FIRST_USE, true);
                            }
                            rlLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            rlLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(HeWeather heWeather) {
                            if (heWeather != null) {
                                weatherList.add(heWeather);
                                //开启新线程保存到数据库中
                                saveWeaInfo(heWeather);
                                setRvAdapter();
                            }
                        }
                    });
        } else {
            T.showToast(this, getString(R.string.no_network));
        }
    }

    private void setRvAdapter() {
        if (rvAdapter == null) {
            rvAdapter = new RvAdapter(this, weatherList);
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            helper = new LinearSnapHelper();
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(rvAdapter);
            helper.attachToRecyclerView(recyclerView);
        } else {
            rvAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 如果id已存在，则删除再存
     *
     * @param heWeather
     */
    private void saveWeaInfo(final HeWeather heWeather) {
        final LocalWeather localWeather = new LocalWeather();
        localWeather.setWeaId(heWeather.getBasic().getId());
        localWeather.setWeaInfo(GsonUtil.toJson(heWeather));
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                int count = localWeather.updateAll("weaid=?", heWeather.getBasic().getId());
                if (count > 0) {
                    subscriber.onNext(true);
                } else {
                    subscriber.onError(new Throwable("存储天气信息失败"));
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribe(new SimpleSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.e(tag, "存储天气信息成功");
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (DoubleClickExit.check()) {
                super.onBackPressed();
            } else {
                T.showToast(this, "再按一次退出");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_city) {
            Intent intent = new Intent(MainActivity.this, ChooseCityActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_delete_city) {
            deleteCity();
        } else if (id == R.id.nav_set) {
            Intent intent = new Intent(MainActivity.this, SetActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }
       /* if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void deleteCity() {
        if (layoutManager != null) {
            int position = layoutManager.findFirstVisibleItemPosition();
            if (position != -1) {
                final HeWeather heWeather = weatherList.remove(position);
                rvAdapter.notifyItemRemoved(position);
                recyclerView.smoothScrollToPosition(position);
                Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        int count = DataSupport.deleteAll(LocalWeather.class, "weaid=?", heWeather.getBasic().getId());
                        if (count > 0) {
                            subscriber.onNext(count);
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Throwable("删除失败"));
                        }
                    }
                }).subscribeOn(Schedulers.io())
                        .flatMap(new Func1<Integer, Observable<Boolean>>() {
                            @Override
                            public Observable<Boolean> call(Integer integer) {
                                return Observable.just(integer > 0);
                            }
                        })
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean succeed) {
                                if (succeed) {
                                    LogUtil.e(tag, "删除城市成功");
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                LogUtil.e(tag, throwable.getMessage());
                            }
                        });
            }
        }
    }

    //点击悬浮按钮
    @OnClick(R.id.fab)
    public void onFabClick() {
        //加载
        LogUtil.e(tag, "更新");
        if (ListUtil.notEmpty(weatherList)) {
            final List<Observable<WeatherBean>> weatherBeanList = new ArrayList<>();
            for (HeWeather weather : weatherList) {
                Observable<WeatherBean> obs = NetWork.getApi().getWeatherByPost(weather.getBasic().getId(), WeatherKey.key).subscribeOn(Schedulers.newThread());
                weatherBeanList.add(obs);
            }
            Observable.mergeDelayError(weatherBeanList)
                    .flatMap(new Func1<WeatherBean, Observable<HeWeather>>() {
                        @Override
                        public Observable<HeWeather> call(WeatherBean weatherBean) {
                            if (weatherBean != null) {
                                return Observable.just(weatherBean.getWeatherList().get(0));
                            }
                            return null;
                        }
                    })
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            rlLoading.setVisibility(View.VISIBLE);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<HeWeather>() {
                        @Override
                        public void onCompleted() {
                            if (ListUtil.notEmpty(updateWeatherList)) {
                                weatherList.clear();
                                weatherList.addAll(updateWeatherList);
                                setRvAdapter();
                                updateWeatherList.clear();
                            }
                            rlLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(tag, e.getMessage());
                            rlLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(HeWeather heWeather) {
                            if (heWeather != null) {
                                updateWeatherList.add(heWeather);
                                //开启新线程保存到数据库中
                                saveWeaInfo(heWeather);
                            }
                        }

                    });
        }
    }


    @Override
    protected void onDestroy() {
        stopService(new Intent(this, AutoUpdateService.class));
        super.onDestroy();

    }
}
