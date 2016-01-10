package com.humanheima.hmweather.ui.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.humanheima.hmweather.R;
import com.humanheima.hmweather.base.BaseActivity;
import com.humanheima.hmweather.base.BaseFragment;
import com.humanheima.hmweather.bean.CityInfo;
import com.humanheima.hmweather.bean.CityInfoList;
import com.humanheima.hmweather.bean.WeatherBean;
import com.humanheima.hmweather.network.NetWork;
import com.humanheima.hmweather.ui.adapter.ViewPagerAdapter;
import com.humanheima.hmweather.ui.fragment.MultiCityManageFragment;
import com.humanheima.hmweather.ui.fragment.WeatherFragment;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by dmw on 2016/9/9.
 */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private List<BaseFragment> fragmentList;
    private List<String> titleList;
    private ViewPagerAdapter viewPagerAdapter;
    private List<CityInfo> cityInfo;

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
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
        titleList = new ArrayList<>();
        fragmentList.add(new WeatherFragment());
        fragmentList.add(new MultiCityManageFragment());
        titleList.add("城市名");
        titleList.add("多城市管理");

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void bindEvent() {

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


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    WeatherBean.HeWeather heWeather;

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
        NetWork.getApi().getCityInfoList("allchina", "fcaa02b41e9048e7aa5854b1e279e1c6")
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
                });
    }
}
