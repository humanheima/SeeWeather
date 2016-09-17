package fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.humanweather.MdFeaWeaActivity;
import com.example.humanweather.MyApp;
import com.example.humanweather.R;

import org.json.JSONException;
import org.json.JSONObject;

import util.LogUtil;
import util.NetUtil;
import util.ShowImageUtil;
import util.SnackUtil;

/**
 * Created by Administrator on 2016/4/14.
 */
public class FragWeather extends BaseFragment {
    private static final String ARG_WEAID = "weaid", ARG_CITYNA = "citynm", DEFAULT_VALUE = "--";
    public  String weaid = "", citynm = "";//天气id
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView im_weather;// 天气图片
    public RelativeLayout rtLayout;
    private ViewGroup viewGroup;
    /**
     * 分别代表：日期，城市名，星期几，实时温度，实时湿度，温度范围，天气，风向，风速，pm2.5，天气指数
     */
    private TextView tv_days, tv_citynm, tv_week, tv_temperature_curr, tvHumidity, tv_temperature, tv_weather, tv_wind, tv_winp, tvAqi, tvQuality, TvTravel;
    private TextView tvFeature;
    private String weaSuccess = "x";
    private String pmSuccess = "y";

    public FragWeather() {
    }

    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param weaid 天气预报城市id
     * @return A new instance of fragment BlankFragment.
     */
    public static FragWeather newInstance(String weaid) {
        FragWeather fragment = new FragWeather();

        Bundle args = new Bundle();
        args.putString(ARG_WEAID, weaid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weaid = getArguments().getString(ARG_WEAID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_weather, container, false);
        findViews(view);
        setViews();
        return view;
    }

    /**
     * 应该先从pref里面得到数据，如果为空则发送请求
     */
    private void setViews() {
        if (pref.getString(weaid + "temperature_curr", null) == null) {
            //LogUtil.e("tag", "temperature_curr" + pref.getString(weaid + "temperature_curr", null));
            //请求实时天气
           // LogUtil.e("TAG", "fragweather_weaid" + weaid);

            sendCurWeatherReq(weaid);
            //请求PM2.5信息
            sendPMReq(weaid);
        } else {
            String weatherStr = pref.getString(weaid + "weather", DEFAULT_VALUE);
            rainSnow = weatherStr;
            //下雨还是下雪
            rainOrSnow(viewGroup, rainSnow);
            ShowImageUtil.getInstance().showDayWeatherImage(getActivity(), weatherStr, im_weather, rtLayout);
            tv_temperature_curr.setText(pref.getString(weaid + "temperature_curr", DEFAULT_VALUE));
            citynm = pref.getString(weaid + "citynm", DEFAULT_VALUE);
            tv_weather.setText(weatherStr);
            tv_days.setText(pref.getString(weaid + "days", DEFAULT_VALUE));
            tv_citynm.setText(citynm);
            tv_week.setText(pref.getString(weaid + "week", DEFAULT_VALUE));
            tvAqi.setText(pref.getString(weaid + "aqi", DEFAULT_VALUE));
            tvQuality.setText(pref.getString(weaid + "aqi_levnm", DEFAULT_VALUE));
            tv_temperature.setText(pref.getString(weaid + "temperature", DEFAULT_VALUE));
            tvHumidity.setText(pref.getString(weaid + "humidity", DEFAULT_VALUE));
            tv_wind.setText(pref.getString(weaid + "wind", DEFAULT_VALUE));
            tv_winp.setText(pref.getString(weaid + "winp", DEFAULT_VALUE));
            TvTravel.setText(pref.getString(weaid + "aqi_remark", DEFAULT_VALUE));
        }
    }

    /**
     * 找到控件，并给特定的控件添加监听事件
     *
     * @param view
     */
    private void findViews(final View view) {

        rtLayout = (RelativeLayout) view.findViewById(R.id.rtLayout);
        viewGroup = (ViewGroup) view.findViewById(R.id.rtLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_blue_dark, android.R.color.darker_gray, android.R.color.holo_blue_bright);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //rtLayout.removeView(minRainSnowView);
                //viewGroup.removeViewAt(0);
                swipeRefreshLayout.setRefreshing(true);
                new MyRequestTask().execute(weaid);
            }
        });
        im_weather = (ImageView) view.findViewById(R.id.imWeather);
        tv_days = (TextView) view.findViewById(R.id.tvDays);
        tv_citynm = (TextView) view.findViewById(R.id.tvCitynm);
        tv_week = (TextView) view.findViewById(R.id.tvWeek);
        tv_temperature_curr = (TextView) view.findViewById(R.id.tvTempCur);
        tv_temperature = (TextView) view.findViewById(R.id.tvTemp);
        tvHumidity = (TextView) view.findViewById(R.id.tvHumidity);
        tv_weather = (TextView) view.findViewById(R.id.tvWeather);
        tv_wind = (TextView) view.findViewById(R.id.tvWind);
        tv_winp = (TextView) view.findViewById(R.id.tvWinp);
        tvAqi = (TextView) view.findViewById(R.id.tvAqi);
        tvQuality = (TextView) view.findViewById(R.id.tvQuality);
        TvTravel = (TextView) view.findViewById(R.id.TvTravel);
        tvFeature = (TextView) view.findViewById(R.id.tvFeature);
        tvFeature.setOnClickListener(this);
    }

    /**
     * 发送请求，请求实时天气和PM2.5的信息
     *
     * @author dmw
     */
    private class MyRequestTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //请求实时天气
            sendCurWeatherReq(params[0]);
            //请求PM2.5信息
            sendPMReq(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 请求实时天气信息
     *
     * @param weaid
     */
    public void sendCurWeatherReq(final String weaid) {
        //http://api.k780.com:88/?app=weather.today&weaid=1&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json
        String url = "http://api.k780.com:88/?app=weather.today&weaid=" + weaid + "&&appkey=15732&sign=bf10378fb5e93259d0a94f2423fa81e5&format=json";
        if (NetUtil.hasNetWork()) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    // 返回的是一个json数组
                    try {
                        LogUtil.e("TAG", "response" + response.toString());
                        weaSuccess = response.getString("success");
                        if ("1".equals(weaSuccess)) {//有数据
                            JSONObject jsonObject = response.optJSONObject("result");
                            tv_days.setText(jsonObject.getString("days"));
                            citynm = jsonObject.getString("citynm");
                            //tv_citynm.setText(jsonObject.getString("citynm"));
                            tv_citynm.setText(citynm);
                            tv_week.setText(jsonObject.getString("week"));
                            tv_temperature_curr.setText(jsonObject.getString("temperature_curr"));
                            tv_temperature.setText(jsonObject.getString("temperature"));
                            tvHumidity.setText(jsonObject.getString("humidity"));
                            tv_wind.setText(jsonObject.getString("wind"));
                            tv_winp.setText(jsonObject.getString("winp"));
                            String weatherStr = jsonObject.getString("weather");
                            rainSnow = weatherStr;
                            ShowImageUtil.getInstance().showDayWeatherImage(getActivity(), weatherStr, im_weather, rtLayout);
                            if (viewGroup.getChildAt(0) != null) {
                                viewGroup.removeViewAt(0);
                            }
                            rainOrSnow(viewGroup, rainSnow);
                            tv_weather.setText(weatherStr);
                            editor.putString(weaid + "days", jsonObject.getString("days"));
                            editor.putString(weaid + "citynm", jsonObject.getString("citynm"));
                            editor.putString(weaid + "week", jsonObject.getString("week"));
                            editor.putString(weaid + "temperature_curr", jsonObject.getString("temperature_curr"));
                            LogUtil.e("tag", "what is wraong temperature_curr" + jsonObject.getString("temperature_curr"));
                            editor.putString(weaid + "temperature", jsonObject.getString("temperature"));
                            editor.putString(weaid + "humidity", jsonObject.getString("humidity"));
                            editor.putString(weaid + "wind", jsonObject.getString("wind"));
                            editor.putString(weaid + "winp", jsonObject.getString("winp"));
                            editor.putString(weaid + "weather", jsonObject.getString("weather"));
                            editor.commit();

                        }
                    } catch (JSONException e) {
                        //SnackUtil.SnackShort(tv_temperature_curr,getString(R.string.update_exception));
                        LogUtil.e("TAG", "解析天气数据异常");
                    }
                    SnackUtil.SnackShort(tv_temperature_curr,getString(R.string.update_success));
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getActivity(), getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
                    //swipeRefreshLayout.setRefreshing(false);

                }
            });
            MyApp.getVolleyQueue().add(request);
        } else {
            SnackUtil.SnackShort(tv_temperature_curr,getString(R.string.net_error));
            swipeRefreshLayout.setRefreshing(false);
            //LogUtil.e("TAG", "sendCurWeatherReq没有网络");
        }
    }

    /**
     * 请求PM2.5的信息
     *
     * @param weaid
     */
    public void sendPMReq(final String weaid) {
        //http://api.k780.com:88/?app=weather.pm25&weaid=1&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json
        String url = "http://api.k780.com:88/?app=weather.pm25&weaid=" + weaid + "&appkey=15732&sign=bf10378fb5e93259d0a94f2423fa81e5&format=json";
        if (NetUtil.hasNetWork()) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // 返回的是一个json数组
                    try {
                        pmSuccess = response.optString("success");
                        if ("1".equals(pmSuccess)) {//有数据
                            JSONObject jsonObject = response.optJSONObject("result");
                            tvAqi.setText(jsonObject.getString("aqi"));
                            tvQuality.setText(jsonObject.getString("aqi_levnm"));
                            TvTravel.setText(jsonObject.getString("aqi_remark"));
                            //将解析到的内容存到本地
                            editor.putString(weaid + "aqi", jsonObject.getString("aqi"));
                            editor.putString(weaid + "aqi_levnm", jsonObject.getString("aqi_levnm"));
                            editor.putString(weaid + "aqi_remark", jsonObject.getString("aqi_remark"));
                            editor.commit();
                        }
                    } catch (Exception e) {
                        LogUtil.e("TAG", "请求pm2.5出错");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.e("TAG", "请求PM2.5失败");

                }
            });
            MyApp.getVolleyQueue().add(request);
        } else {
            LogUtil.e("TAG", "sendPMReq 请检查网络连接");
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvFeature:
                // 查看天气走向
                Intent featurIntent = new Intent(getActivity(), MdFeaWeaActivity.class);
                featurIntent.putExtra("weaid", weaid);
                featurIntent.putExtra("citynm", citynm);
                startActivity(featurIntent);
                break;
            default:
                break;
        }
    }
}
