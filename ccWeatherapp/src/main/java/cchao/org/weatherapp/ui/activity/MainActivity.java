package cchao.org.weatherapp.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cchao.org.weatherapp.Constant;
import cchao.org.weatherapp.R;
import cchao.org.weatherapp.controller.SaveDataController;
import cchao.org.weatherapp.ui.adapter.DailyRecyclerAdapter;
import cchao.org.weatherapp.ui.base.BaseActivity;
import cchao.org.weatherapp.utils.HttpUtil;
import cchao.org.weatherapp.utils.WeatherIconUtil;
import cchao.org.weatherapp.vo.ApiResultVO;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by chenchao on 15/11/13.
 */
public class MainActivity extends BaseActivity {

    private long mExitTime = 0;

    private Toolbar mToolbar;
    //现在温度
    private TextView mNowTmp;
    //现在天气状况
    private TextView mNowCond;
    //现在天气状况图片
    private ImageView mNowCondImage;
    //当前日期
    private TextView mTime;
    //当天最高温度和最低温度
    private TextView mTmp;
    //风向
    private TextView mWindDir;
    //风力
    private TextView mWindSc;
    //穿衣提示
    private TextView mSuggestionTxt;
    private ProgressDialog mProgressDialog;

    private RecyclerView mRecyclerDaily;
    private LinearLayoutManager mLinearLayoutManager;
    private DailyRecyclerAdapter mDailyRecyclerAdapter;
    private ArrayList<String> mDataTime, mDataTmp, mDataCondText, mDataCondImage;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void bindView() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mNowTmp = (TextView) findViewById(R.id.textview_now_tmp);
        mNowCond = (TextView) findViewById(R.id.textview_now_cond);
        mNowCondImage = (ImageView) findViewById(R.id.imageview_now_cond);
        mTime = (TextView) findViewById(R.id.textview_time);
        mTmp = (TextView) findViewById(R.id.textview_tmp);
        mWindDir = (TextView) findViewById(R.id.textview_dir);
        mWindSc = (TextView) findViewById(R.id.textview_sc);
        mSuggestionTxt = (TextView) findViewById(R.id.textview_suggestion_txt);
        mRecyclerDaily = (RecyclerView) findViewById(R.id.recycler_daily);
    }

    @Override
    protected void initData() {
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        if (cityIsEmpty()) {
            goSetting();
        }
        mProgressDialog = ProgressDialog.show(this, "请稍等", "刷新中...");
        updateWeather();
        initRecycler();
    }

    @Override
    protected void bindEvent() {
    }

    /**
     * 从服务器获取天气信息
     */
    private void getWeather(){
        mProgressDialog.show();
        HttpUtil.retrofitPost("CN" + mWeatherMsg.get(Constant.CITY_ID), "fcaa02b41e9048e7aa5854b1e279e1c6")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<ApiResultVO, Boolean>() {
                    @Override
                    public Boolean call(ApiResultVO apiResultVO) {
                        return SaveDataController.getSaveDataController().saveResponse(apiResultVO);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                        Toast.makeText(MainActivity.this, R.string.main_network_error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            updateWeather();
                            updateRecyclerData();
                            mDailyRecyclerAdapter.notifyDataSetChanged();
                        } else {
                            if(mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                            Toast.makeText(MainActivity.this, R.string.main_network_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecycler() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerDaily.setLayoutManager(mLinearLayoutManager);

        mDailyRecyclerAdapter = new DailyRecyclerAdapter();
        updateRecyclerData();
        mRecyclerDaily.setAdapter(mDailyRecyclerAdapter);
    }

    /**
     * 更新recyclerview数据
     */
    private void updateRecyclerData() {
        mDataTime = new ArrayList<String>();
        mDataTmp = new ArrayList<String>();
        mDataCondText = new ArrayList<String>();
        mDataCondImage = new ArrayList<String>();
        if (!cityIsEmpty()) {
            for (int i = 2; i < 8; i++) {
                String temp = String.valueOf(i);
                mDataTime.add((mWeatherMsg.get(Constant.DAILY_TIME + temp)).substring(5));
                mDataTmp.add(mWeatherMsg.get(Constant.DAILY_TMP_MIN + temp) + "°~" + mWeatherMsg.get(Constant.DAILY_TMP_MAX + temp) + "°");
                mDataCondText.add(mWeatherMsg.get(Constant.DAILY_COND_d + temp));
                mDataCondImage.add(mWeatherMsg.get(Constant.DAILY_CODE_d + temp));
            }
        }
        mDailyRecyclerAdapter.setmData(mDataTime, mDataCondImage, mDataCondText, mDataTmp);
    }

    /**
     * 更新界面
     */
    private void updateWeather() {
        if (!cityIsEmpty()) {
            getSupportActionBar().setTitle(mWeatherMsg.get(Constant.CITY_NAME));
            mNowTmp.setText(mWeatherMsg.get(Constant.NOW_TMP) + "°");
            mNowCond.setText(mWeatherMsg.get(Constant.NOW_COND));
            mNowCondImage.setImageDrawable(WeatherIconUtil.getWeatherIcon(mWeatherMsg.get(Constant.NOW_CODE)));
            mTime.setText(getWhatDay());
            mTmp.setText(mWeatherMsg.get(Constant.DAILY_TMP_MIN + 1) + "°~" + mWeatherMsg.get(Constant.DAILY_TMP_MAX + 1) + "°");
            mWindDir.setText(mWeatherMsg.get(Constant.NOW_WIND_DIR));
            if (isDigital(mWeatherMsg.get(Constant.NOW_WIND_SC).substring(0, 1))) {
                mWindSc.setText(mWeatherMsg.get(Constant.NOW_WIND_SC) + "级");
            } else {
                mWindSc.setText(mWeatherMsg.get(Constant.NOW_WIND_SC));
            }
            mSuggestionTxt.setText(mWeatherMsg.get(Constant.SUGGESTION_DRSG));
        } else {
            getSupportActionBar().setTitle("Weather");
        }
        if(mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 跳转到设置界面
     */
    private void goSetting() {
        startActivityForResult(new Intent(MainActivity.this, SettingActivity.class), UPDATE_ACTIVITY_RESULT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_main_location) {
            goSetting();
            return true;
        } else if (id == R.id.menu_main_refresh) {
            if (cityIsEmpty()) {
                Toast.makeText(MainActivity.this, R.string.main_snackbar_ID_isEmpty, Toast.LENGTH_SHORT).show();
                goSetting();
            } else {
                getWeather();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, R.string.main_exit_toast, Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == UPDATE_ACTIVITY_RESULT) {
            getWeather();
        }
    }
}
