package com.example.humanweather;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.FeatureWeatherAdapter;
import db.FeatureWeatherDB;
import model.FeatureWeatherModel;
import util.Constants;
import util.LogUtil;
import util.NetUtil;
import util.ProDialogUtil;
import util.ShareWeatherTask;
import util.ShowImageUtil;

public class MdFeaWeaActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab;
    RelativeLayout relativeLayout;
    private ProDialogUtil dialogUtil;
    private static String citynm;
    private static String weaid;
    private FeatureWeatherDB featureWeatherDB;
    private ListView lv_featureWeatherInfo;
    private FeatureWeatherAdapter adapter;
    private List<FeatureWeatherModel> featureWeathermodelList;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md_fea_wea);
        dialogUtil = new ProDialogUtil(this);
        getIntentExtra();
        findViews();
        ShowImageUtil.getInstance().drawBackground(this, "");
        if (NetUtil.hasNetWork()) {
            dialogUtil.showProgressDialog();
            sendRequest(weaid);

        } else {
            showStoredWeather();
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feature_weather, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionShare) {
            if (NetUtil.hasNetWork()) {
                //分享天气
                new ShareWeatherTask(MdFeaWeaActivity.this).execute();
            } else {
                Snackbar.make(relativeLayout, getString(R.string.net_error), Snackbar.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 展示存储在本地的数据
     */
    private void showStoredWeather() {
        dialogUtil.showProgressDialog();
        Log.e("TAG", citynm);
        featureWeathermodelList = featureWeatherDB.loadFeatureOneDayWeather(citynm);
        adapter = new FeatureWeatherAdapter(MdFeaWeaActivity.this, R.layout.feature_weather_listviewitem, featureWeathermodelList);
        lv_featureWeatherInfo.setAdapter(adapter);
        dialogUtil.closeProgressDialog();
    }
    private void sendRequest(String weaid) {
        String url = "http://api.k780.com:88/?app=weather.future&weaid=" + weaid + "&&appkey=15732&sign=bf10378fb5e93259d0a94f2423fa81e5&format=json";
        //有网络，发送请求，先把本地的天气信息删除
        featureWeatherDB.deleteDBRecode(citynm);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if ("1".equals(response.optString("success"))) {
                        JSONArray jsonArray = response.getJSONArray("result");
                        featureWeathermodelList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject featureObject = jsonArray.getJSONObject(i);
                            String days = featureObject.getString("days");
                            String week = featureObject.getString("week");
                            String citynm = featureObject.getString("citynm");
                            String temperature = featureObject.getString("temperature");
                            String weather = featureObject.getString("weather");
                            FeatureWeatherModel weathermodel = new FeatureWeatherModel();
                            weathermodel.setDays(days);
                            weathermodel.setWeek(week);
                            weathermodel.setCitynm(citynm);
                            weathermodel.setTemperature(temperature);
                            weathermodel.setWeather(weather);

                            featureWeathermodelList.add(weathermodel);
                            featureWeatherDB.saveFeatureWeathermodel(weathermodel);
                        }
                        adapter = new FeatureWeatherAdapter(MdFeaWeaActivity.this, R.layout.feature_weather_listviewitem, featureWeathermodelList);
                        lv_featureWeatherInfo.setAdapter(adapter);
                        Snackbar.make(relativeLayout, getString(R.string.update_success), Snackbar.LENGTH_SHORT).show();
                    } else {
                        // Toast.makeText(MdFeaWeaActivity.this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
                        Snackbar.make(relativeLayout, getString(R.string.update_failed), Snackbar.LENGTH_SHORT).show();
                    }
                    dialogUtil.closeProgressDialog();
                } catch (Exception e) {
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogUtil.closeProgressDialog();
                Snackbar.make(relativeLayout, getString(R.string.update_failed), Snackbar.LENGTH_SHORT).show();
            }
        });

        MyApp.getVolleyQueue().add(request);
    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        citynm = intent.getStringExtra(Constants.ARG_CITYNM);
        weaid = intent.getStringExtra(Constants.ARG_WEAID);
        LogUtil.e("tag", "citynm" + citynm);
        LogUtil.e("tag", "weaid" + weaid);
    }

    public void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srlFeatureWeather);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new FeaWeatherTask().execute(weaid);

            }
        });
        lv_featureWeatherInfo = (ListView) findViewById(R.id.lv_featureWeatherInfo);
        featureWeatherDB = FeatureWeatherDB.getInstance(MdFeaWeaActivity.this);
        featureWeathermodelList = new ArrayList<>();
    }


    private class FeaWeatherTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            sendRequest(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
