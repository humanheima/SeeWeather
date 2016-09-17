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
import com.humanheima.hmweather.bean.WeatherBean;
import com.humanheima.hmweather.ui.adapter.ViewPagerAdapter;
import com.humanheima.hmweather.ui.fragment.WeatherFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
        fragmentList.add(new WeatherFragment());
        //fragmentList.add(new WeatherFragment());
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
