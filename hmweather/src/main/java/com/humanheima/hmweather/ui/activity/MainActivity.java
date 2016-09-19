package com.humanheima.hmweather.ui.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.humanheima.hmweather.R;
import com.humanheima.hmweather.base.BaseActivity;
import com.humanheima.hmweather.base.BaseFragment;
import com.humanheima.hmweather.bean.HeWeather;
import com.humanheima.hmweather.bean.WeatherCode;
import com.humanheima.hmweather.ui.adapter.ViewPagerAdapter;
import com.humanheima.hmweather.ui.fragment.WeatherFragment;
import com.humanheima.hmweather.utils.LogUtil;
import com.humanheima.hmweather.utils.RxBus;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by dmw on 2016/9/9.
 */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    private List<BaseFragment> fragmentList;
    private ViewPagerAdapter viewPagerAdapter;
    private final static String tag = "MainActivity";

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

        initViewPager();

    }

    private void initViewPager() {
        fragmentList = new ArrayList<>();
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
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
    }

    /**
     * 首先存储weatherCode，
     * 然后增加天气信息，
     * 现在还没有判断weaId是否重复
     *
     * @param weatherCode
     */
    private void addCityWeather(final WeatherCode weatherCode) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                //先查询如果表中没有则存,否则直接返回true
                WeatherCode temp = DataSupport.where("code=?", weatherCode.getCode()).findFirst(WeatherCode.class);
                if (temp == null) {
                    subscriber.onNext(weatherCode.save());
                } else {
                    subscriber.onNext(true);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(tag, e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean succeed) {
                        if (succeed) {
                            String weaId = weatherCode.getCode();
                            BaseFragment fragment = WeatherFragment.newInstance(weaId);
                            fragmentList.add(fragment);
                            viewPagerAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    HeWeather heWeather;

    //点击悬浮按钮
    @OnClick(R.id.fab)
    public void onFabClick() {
       /* NetWork.getApi().getWeatherByPost("CN101020100", "fcaa02b41e9048e7aa5854b1e279e1c6")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherBean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.e("getWeather", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("getWeather", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(WeatherBean weatherBean) {
                        heWeather = weatherBean.getWeatherList().get(0);
                        LogUtil.e("getWeather", "" + heWeather.getStatus() + "," + heWeather.getBasic().getCity());
                    }
                });*/
       /* NetWork.getApi().getCityInfoList("allchina", "fcaa02b41e9048e7aa5854b1e279e1c6")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<CityInfoList>() {
                    @Override
                    public void onCompleted() {
                        Log.e("tag", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("tag", "onError" + throwable.getMessage());
                    }

                    @Override
                    public void onNext(CityInfoList cityInfoList) {
                        cityInfo = cityInfoList.getCityInfo();
                        if (cityInfo.size() > 0) {
                            DataSupport.saveAll(cityInfo);
                        }
                    }
                });*/

        //final List<CityInfo> list = DataSupport.where("id > ?", "0").find(CityInfo.class);
      /*  if (list.size() > 0) {
            ContentValues values = new ContentValues();
            values.put("citypinyin", "");
            int i = DataSupport.updateAll(CityInfo.class, values, "id>?", "0");
            if (i > 0) {
                LogUtil.e("tag", "updata success");
            }
        }*/
       /* if (list.size() > 0) {

            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... voids) {
                    long id = -1;
                    for (int i = 0; i < list.size(); i++) {
                        cityInfo = list.get(i);
                        String city = cityInfo.getCity();
                        id = cityInfo.getId();
                        char[] chars = city.toCharArray();
                        String cityPinyin = "";
                        String s = null;
                        for (int j = 0; j < chars.length; j++) {
                            s = Pinyin.toPinyin(chars[j]);
                            cityPinyin += s;
                        }
                        ContentValues values = new ContentValues();
                        values.put("citypinyin", cityPinyin);
                        DataSupport.update(CityInfo.class, values, id);
                    }
                    if (id > 0) {
                        return true;
                    } else {
                        return false;
                    }
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    if (aBoolean) {
                        LogUtil.e("save", "successful");
                    } else {
                        LogUtil.e("save", "failed");
                    }
                }
            }.execute();
        }*/

      /*  NetWork.getApi().getWeatherByPost(cityInfo.getWeatherId(), C.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherBean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.e("getWeather", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("getWeather", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(WeatherBean weatherBean) {
                        heWeather = weatherBean.getWeatherList().get(0);
                        LogUtil.e("getWeather", "" + heWeather.getStatus() + "," + heWeather.getBasic().getCity());
                    }
                });*/
    }
}
