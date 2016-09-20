package com.example.humanweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fragment.FragWeather;
import util.Constants;
import util.NetUtil;
import util.ShareWeatherTask;
import util.ShowImageUtil;
import util.SnackUtil;

public class WeatherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static int REQUEST_CODE = 7;
    private String weaid;
    //是否是第一次使用
    private boolean isFirstUse;

    private SharedPreferences weaidPref, isFirstPref;
    private SharedPreferences.Editor isFirstEditor, weaidEditor;
    private ViewPager viewPager;
    private FragWeather fragWeather;
    private ArrayList<FragWeather> fragList;
    private Map<String, String> maps = new HashMap<String, String>();
    private MyViewPagerAdapter adapter;
    private long exitTime = 0;
    private long exitDuration = 2000;
    private RelativeLayout actLinLayout;
    private File file;
    //
    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        isFirstPref = getSharedPreferences(Constants.IS_FIRST_USE, MODE_PRIVATE);
        weaidPref = getSharedPreferences(Constants.WEAID_PREF, MODE_PRIVATE);
        //让editor处于可编辑状态
        isFirstEditor = isFirstPref.edit();
        weaidEditor = weaidPref.edit();
        findViews();
        initData();
        setAdapter();
        if (isFirstUse()) {
            //第一次进入城市列表界面，选择你感兴趣的城市
            //startActivity(new Intent(WeatherActivity.this,CityActivity.class));
            Intent intent = new Intent(WeatherActivity.this, MdCityActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
            //finish();
        }
        /**
         * 不是第一次使用软件，则每次进入这个页面的时候应该判断newWeaid是否等于weaid，如果不等于的话，
         * 则实例化一个新的Fragment添加到ViewPager中，并且显示新添加的Fragment
         * 应该把weaid存到sharePreference中每次启动时根据有几个不同的weaid,创建几个Fragment
         */
        ShowImageUtil.getInstance().drawBackground(this, "");
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * 给viewPager添加适配器
     */
    private void setAdapter() {
        adapter = new MyViewPagerAdapter(getSupportFragmentManager(), fragList);
        viewPager.setAdapter(adapter);
        //viewPager.setCurrentItem(0);
    }

    private void initData() {
        fragList = new ArrayList<FragWeather>();
        maps = (Map<String, String>) weaidPref.getAll();
        Collection<String> collection = maps.values();
        Log.e("TAG", "collection.size==" + collection.size());
        for (String prefWeaid : collection) {
            //Log.e("TAG", "prefWeaid"+prefWeaid);
            fragWeather = FragWeather.newInstance(prefWeaid);
            fragList.add(fragWeather);
        }
    }

    /**
     * 判断是不是第一次使用软件
     *
     * @return 第一次使用返回真，否则返回假
     */
    private boolean isFirstUse() {
        //pref=getSharedPreferences("isFirstUse", MODE_PRIVATE);
        isFirstUse = isFirstPref.getBoolean("isFirstUse", true);
        //第一次使用
        if (isFirstUse) {
            //实例化Editor对象
            //editor = pref.edit();
            //存入数据
            isFirstEditor.putBoolean("isFirstUse", false);
            //提交修改
            isFirstEditor.commit();
        }
        return isFirstUse;
    }

    /**
     * 找到控件
     */
    private void findViews() {
        //actLinLayout = (LinearLayout) findViewById(R.id.actLinLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                FragWeather curFrag = fragList.get(viewPager.getCurrentItem());
                String curWeaid = curFrag.weaid;
                String curCitynm = curFrag.citynm;
                // 查看天气走向
                Intent featurIntent = new Intent(WeatherActivity.this, MdFeaWeaActivity.class);
                featurIntent.putExtra(Constants.ARG_WEAID, curWeaid);
                featurIntent.putExtra(Constants.ARG_CITYNM, curCitynm);
                startActivity(featurIntent);
                //new ShareWeatherTask(WeatherActivity.this).execute();
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        actLinLayout = (RelativeLayout) findViewById(R.id.actLinLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (System.currentTimeMillis() - exitTime > exitDuration) {
            SnackUtil.SnackShort(actLinLayout, getString(R.string.press_again));
            exitTime = System.currentTimeMillis();
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
        } else {
            this.finish();
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            if (NetUtil.hasNetWork()) {
                new ShareWeatherTask(WeatherActivity.this).execute();
            } else {
                SnackUtil.SnackShort(actLinLayout, R.string.net_error);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 删除当前的Fragment,并删除shareprefrence中的内容
     */
    private void deleteCurFrag() {
        if (fragList.size() > 0) {
            FragWeather curFrag = fragList.get(viewPager.getCurrentItem());
            if (curFrag != null) {
                String delWeaid = curFrag.weaid;
                adapter.destroyItem(viewPager, viewPager.getCurrentItem(), curFrag);
                fragList.remove(viewPager.getCurrentItem());
                adapter.notifyDataSetChanged();
                weaidEditor.remove(delWeaid);
                weaidEditor.commit();
                SharedPreferences.Editor editor = curFrag.editor;
                editor.remove(delWeaid);
                editor.remove(delWeaid + "days");
                editor.remove(delWeaid + "citynm");
                editor.remove(delWeaid + "week");
                editor.remove(delWeaid + "temperature_curr");
                editor.remove(delWeaid + "temperature");
                editor.remove(delWeaid + "humidity");
                editor.remove(delWeaid + "wind");
                editor.remove(delWeaid + "winp");
                editor.remove(delWeaid + "weather");
                editor.remove(delWeaid + "aqi");
                editor.remove(delWeaid + "aqi_levnm");
                editor.remove(delWeaid + "aqi_remark");
                editor.commit();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            //Intent intent = new Intent(WeatherActivity.this, CityActivity.class);
            Intent intent = new Intent(WeatherActivity.this, MdCityActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            //删除当前的Fragment
            deleteCurFrag();
            SnackUtil.SnackShort(actLinLayout, R.string.delete_success);

        } /*else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {

        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 从cityActivity返回的结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // weaid = data.getStringExtra("weaid");
                weaid = data.getStringExtra(Constants.ARG_WEAID);
                //如果从CityActivity传递过来的weaid!=null
                if (weaid != null) {
                    if (!weaidPref.contains(weaid)) {
                        weaidEditor.putString(weaid, weaid);
                        weaidEditor.commit();
                        //fragWeather = new FragWeather(weaid);
                        fragWeather = FragWeather.newInstance(weaid);
                        fragList.add(fragWeather);
                        adapter.notifyDataSetChanged();
                        viewPager.setCurrentItem(fragList.size() - 1, true);
                    }
                }
            } else {
                Log.e("TAG", "onActivityResult, result not ok");
            }
        }
    }

    /**
     * ViewPager的适配器
     */
    private class MyViewPagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<FragWeather> fragments;

        public FragWeather currentFragment;

        public MyViewPagerAdapter(FragmentManager fm, ArrayList<FragWeather> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        //this is called when notifyDataSetChanged() is called
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        /**
         * 得到当前的Fragment对FragmentStatePagerAdapter没用，要使用FragmentPagerAdapter
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            currentFragment = (FragWeather) object;
            super.setPrimaryItem(container, position, currentFragment);
        }


    }
}
