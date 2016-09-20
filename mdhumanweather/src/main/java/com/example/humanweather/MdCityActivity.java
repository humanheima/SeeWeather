package com.example.humanweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapter.CityListViewAdapter;
import db.WeatherCityDB;
import fragment.LocateDialogFragment;
import model.WeatherCityModel;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import util.Constants;
import util.NetUtil;
import util.PinYn4jUtil;
import util.PinyinComparator;
import util.SnackUtil;
import view.SideBar;

/**
 * Created by dumingwei on 2016/4/14.
 * 城市列表界面
 * 定位功能
 * 城市的A_Z排序 ,快速定位
 * 手动输入搜索：可以输入城市的汉字，拼音 首字母,进行搜索
 * 点击相应的城市进入实时界面，查询天气情况
 */
public class MdCityActivity extends AppCompatActivity {

    //定位相关的变量
    private LocationClient locationClient;
    private LocationClientOption option;
    private MyLocListener listener;
    private String citynm, weaid;
    WeatherCityDB cityDB;
    private ListView lv_city;
    private CityListViewAdapter adapter;

    RelativeLayout rlCity;
    /**
     * 屏幕右边的A_Z列表
     */
    private SideBar sideBar;
    /**
     * 输入文本框
     */
    //private ClearEditText clearEditText;
    private List<WeatherCityModel> sourceDatalist;
    /**
     * 进度提示框
     */
    private ProgressDialog progressDialog;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private Toolbar toolbar;
    private PinyinComparator pinyinComparator;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md_city);
        initDbData();
        closeProgressDialog();
        findViews();
        setSupportActionBar(toolbar);
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.cityToolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        lv_city = (ListView) findViewById(R.id.lv_city);
        rlCity = (RelativeLayout) findViewById(R.id.rlcity);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //定位城市
                if (NetUtil.hasNetWork()) {
                    startLocate();
                } else {
                    SnackUtil.SnackShort(rlCity, getString(R.string.net_error));
                }
            }
        });
        pinyinComparator = new PinyinComparator();
      /*  // list里面填充数据
        sourceDatalist = cityDB.loadCity();
        pinyinComparator = new PinyinComparator();
        Collections.sort(sourceDatalist, pinyinComparator);
        adapter = new CityListViewAdapter(this, sourceDatalist);
        lv_city.setAdapter(adapter);*/
        // 找到ListView控件并为其添加监听
        lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(Constants.ARG_WEAID, ((WeatherCityModel) adapter.getItem(position)).getWeaid());
                setResult(RESULT_OK, intent);
                MdCityActivity.this.finish();
            }
        });

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    lv_city.setSelection(position);
                }
            }
        });

        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                // list里面填充数据
                sourceDatalist = cityDB.loadCity();
                Collections.sort(sourceDatalist, pinyinComparator);
                subscriber.onNext(true);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        adapter = new CityListViewAdapter(MdCityActivity.this, sourceDatalist);
                        lv_city.setAdapter(adapter);
                    }
                });
    }

    /**
     * 根据输入框传过来的值过滤想要查找的城市
     *
     * @param textstr
     */
    protected void filterData(String textstr) {

        List<WeatherCityModel> filterlist = new ArrayList<WeatherCityModel>();
        if (TextUtils.isEmpty(textstr)) {
            filterlist = sourceDatalist;
        } else {

            filterlist.clear();

            for (WeatherCityModel weatherCityModel : sourceDatalist) {

                String cityno = weatherCityModel.getCityno();
                if (cityno.startsWith(textstr)) {
                    filterlist.add(weatherCityModel);
                }
            }
        }
        // 根据A_Z排序
        Collections.sort(filterlist, pinyinComparator);
        adapter.updateListView(filterlist);
    }

    /**
     * 将城市列表数据库文件复制到本地
     *
     * @return
     */
    private void initDbData() {
        showProgressDialog();
        // 得到数据库实例
        cityDB = new WeatherCityDB(this);
    }

    /**
     * 开启定位城市，
     */
    private void startLocate() {
        if (locationClient == null) {
            locationClient = new LocationClient(this);
        }
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(0);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        locationClient.setLocOption(option);
        listener = new MyLocListener();
        locationClient.registerLocationListener(listener);
        //启动监听
        locationClient.start();

    }

    private class MyLocListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            citynm = bdLocation.getDistrict() != null ? bdLocation.getDistrict() : bdLocation.getCity();
            if (citynm != null && citynm.length() > 0) {
                citynm = citynm.substring(0, citynm.length() - 1);
                //tvLocCity.setText(citynm);
                weaid = cityDB.queryWeaid(citynm);
            }
            if (!"".equals(weaid)) {
                //dialog=new AlertDialogUtil(CityActivity.this,citynm,weaid);
                //dialog.show();
                LocateDialogFragment dialogFragment = LocateDialogFragment.newInstance(citynm, weaid);
                dialogFragment.show(getSupportFragmentManager(), weaid);
            } else {
                SnackUtil.SnackShort(rlCity, getString(R.string.loc_failed));
            }
            locationClient.unRegisterLocationListener(listener);
            locationClient.stop();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.city_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryCitynm) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                String citynm = PinYn4jUtil.getPinYin(queryCitynm).toLowerCase();
                filterData(citynm);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null) {
            if (locationClient.isStarted()) {
                locationClient.unRegisterLocationListener(listener);
                locationClient.stop();
            }
        }
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getString(R.string.wait));
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


}
