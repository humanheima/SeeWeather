package com.example.humanweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request.Method;
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
import util.LogUtil;
import util.NetUtil;
import util.ProDialogUtil;
import util.ShareWeatherTask;
import util.ShowImageUtil;

public class FeatureWeatherActivity extends AppCompatActivity {

	LinearLayout lineLayoutFea;
	private ProDialogUtil dialogUtil;
	//private ImageView im_back;
	private static  String citynm;
	private static String weaid;
	private FeatureWeatherDB featureWeatherDB;
	private ListView lv_featureWeatherInfo;
	FeatureWeatherAdapter adapter;
	private List<FeatureWeatherModel> featureWeathermodelList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feature_weather);
		dialogUtil=new ProDialogUtil(this);
		getIntentExtra();
		findViews();
		ShowImageUtil.getInstance().drawBackground(this, "");
		if (NetUtil.hasNetWork()) {
			sendRequest(weaid);
		} else {
			showStoredWeather();
		}
	}

	/**
	 * 展示数据库里存储的天气信息
	 */
	private void showStoredWeather() {
		dialogUtil.showProgressDialog();
		Log.e("TAG", citynm);
		featureWeathermodelList=featureWeatherDB.loadFeatureOneDayWeather(citynm);
		adapter = new FeatureWeatherAdapter(FeatureWeatherActivity.this,R.layout.feature_weather_listviewitem,featureWeathermodelList);
		lv_featureWeatherInfo.setAdapter(adapter);
		dialogUtil.closeProgressDialog();
	}


	private void getIntentExtra() {
		Intent intent = getIntent();
		citynm = intent.getStringExtra("citynm");
		weaid = intent.getStringExtra("weaid");
		LogUtil.e("tag","citynm"+citynm);
		LogUtil.e("tag","weaid"+weaid);
	}

	private void findViews() {
		lineLayoutFea= (LinearLayout) findViewById(R.id.lineLayoutFea);
		lv_featureWeatherInfo = (ListView) findViewById(R.id.lv_featureWeatherInfo);
		featureWeatherDB = FeatureWeatherDB.getInstance(FeatureWeatherActivity.this);
		featureWeathermodelList = new ArrayList<>();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.feature_weather, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		else if (id == R.id.actionShare) {
			if (NetUtil.hasNetWork()) {
				//分享天气
				new ShareWeatherTask(FeatureWeatherActivity.this).execute();
			}else {
				//Toast.makeText(this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
				Snackbar.make(lineLayoutFea,getString(R.string.net_error),Snackbar.LENGTH_SHORT).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	public void sendRequest(String weaid) {
		dialogUtil.showProgressDialog();
		String url = "http://api.k780.com:88/?app=weather.future&weaid="+weaid+"&&appkey=15732&sign=bf10378fb5e93259d0a94f2423fa81e5&format=json";
		//有网络，发送请求，先把本地的天气信息删除
		featureWeatherDB.deleteDBRecode(citynm);
		JsonObjectRequest request=new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {
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
							//String wind = featureObject.getString("wind");
							//String winp = featureObject.getString("winp");
							FeatureWeatherModel weathermodel = new FeatureWeatherModel();
							weathermodel.setDays(days);
							weathermodel.setWeek(week);
							weathermodel.setCitynm(citynm);
							weathermodel.setTemperature(temperature);
							weathermodel.setWeather(weather);
							//weathermodel.setWind(wind);
							//weathermodel.setWinp(winp);

							featureWeathermodelList.add(weathermodel);
							featureWeatherDB.saveFeatureWeathermodel(weathermodel);
						}
						adapter = new FeatureWeatherAdapter(FeatureWeatherActivity.this,R.layout.feature_weather_listviewitem,featureWeathermodelList);
						lv_featureWeatherInfo.setAdapter(adapter);
					} else {
						Toast.makeText(FeatureWeatherActivity.this,getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {

					//Log.e("TAG", "请求未来天气出错");
				}

				dialogUtil.closeProgressDialog();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//Toast.makeText(FeatureWeatherActivity.this,getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
				Snackbar.make(lineLayoutFea,getString(R.string.update_failed),Snackbar.LENGTH_SHORT).show();
			}
		});
		MyApp.getVolleyQueue().add(request);
	}
}
