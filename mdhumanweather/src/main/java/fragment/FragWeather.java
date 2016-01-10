package fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
import com.example.humanweather.FeatureWeatherActivity;
import com.example.humanweather.MyApp;
import com.example.humanweather.R;

import org.json.JSONObject;

import util.NetUtil;
import util.ShowImageUtil;
import util.SnackUtil;

/**
 * Created by Administrator on 2016/4/14.
 */
public class FragWeather extends BaseFragment {
    private static final String ARG_WEAID = "weaid",ARG_CITYNA = "citynm";
    public  String weaid,citynm;//天气id
    private View view;
    // 下拉刷新控件
    //private ScrollView scrollView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView im_weather;// 天气图片
    public RelativeLayout rtLayout;//Fragment最外层的布局
    /**
     * 分别代表：日期，城市名，星期几，实时温度，实时湿度，温度范围，天气，风向，风速，pm2.5，天气指数
     */
    private TextView tv_days, tv_citynm, tv_week, tv_temperature_curr, tvHumidity, tv_temperature, tv_weather, tv_wind, tv_winp, tvAqi, tvQuality, TvTravel;
    //	private Button btnFeature;
    private TextView tvFeature;
    /**
     * 用来标识刷新是否成功
     */
    //public static String SUCCESS = "1";
    //public static String refreshSucceed = "刷新成功";
    // public static String refreshFailed = "刷新失败";
    private String weaSuccess = "x";
    private String pmSuccess = "y";

    public FragWeather(){

    }
    //构造函数
   /* public FragWeather(String weaid) {
        this.weaid = weaid;
    }*/

    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     * @param weaid
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
        setViews(view);
        return view;
    }

    /**
     * 应该先从pref里面得到数据，如果为空则发送请求
     */
    private void setViews(View view) {

        if (pref.getString(weaid + "temperature_curr", null) == null) {
            //请求实时天气
            sendCurWeatherReq(weaid);
            //请求PM2.5信息
            sendPMReq(weaid);
        } else {
            String weatherStr = pref.getString(weaid + "weather", null);
            rainSnow = pref.getString(weaid + "weather", null);
            citynm=pref.getString(weaid+"citynm",null);
            //下雨还是下雪
            rainOrSnow(rtLayout, rainSnow);
            tv_temperature_curr.setText(pref.getString(weaid + "temperature_curr", null));
            ShowImageUtil.getInstance().showDayWeatherImage(getActivity(), weatherStr, im_weather, rtLayout);
            tv_weather.setText(weatherStr);
            tv_days.setText(pref.getString(weaid + "days", null));
            //tv_citynm.setText(pref.getString(weaid + "citynm", null));
            tv_citynm.setText(citynm);
            tv_week.setText(pref.getString(weaid + "week", null));
            tvAqi.setText(pref.getString(weaid + "aqi", null));
            tvQuality.setText(pref.getString(weaid + "aqi_levnm", null));
            tv_temperature.setText(pref.getString(weaid + "temperature", null));
            tvHumidity.setText(pref.getString(weaid + "humidity", null));
            tv_wind.setText(pref.getString(weaid + "wind", null));
            tv_winp.setText(pref.getString(weaid + "winp", null));
            TvTravel.setText(pref.getString(weaid + "aqi_remark", null));
        }
    }

    /**
     * 找到控件，并给特定的控件添加监听事件
     *
     * @param view
     */
    private void findViews(View view) {

        rtLayout = (RelativeLayout) view.findViewById(R.id.rtLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_blue_dark, android.R.color.darker_gray, android.R.color.holo_blue_bright);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                rtLayout.removeView(minRainSnowView);
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
            //pullToRefreshScrollView.onRefreshComplete();//下拉刷新完成
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
                        weaSuccess = response.getString("success");
                        if ("1".equals(weaSuccess)) {//有数据

                            JSONObject jsonObject = response.optJSONObject("result");
                            tv_days.setText(jsonObject.getString("days"));
                            citynm=jsonObject.getString("citynm");
                            //tv_citynm.setText(jsonObject.getString("citynm"));
                            tv_citynm.setText(citynm);
                            tv_week.setText(jsonObject.getString("week"));
                            tv_temperature_curr.setText(jsonObject.getString("temperature_curr"));
                            tv_temperature.setText(jsonObject.getString("temperature"));
                            tvHumidity.setText(jsonObject.getString("humidity"));
                            tv_wind.setText(jsonObject.getString("wind"));
                            tv_winp.setText(jsonObject.getString("winp"));
                            tv_weather.setText(jsonObject.getString("weather"));
                            editor.putString(weaid + "days", jsonObject.getString("days"));
                            editor.putString(weaid + "citynm", jsonObject.getString("citynm"));
                            editor.putString(weaid + "week", jsonObject.getString("week"));
                            editor.putString(weaid + "temperature_curr", jsonObject.getString("temperature_curr"));
                            editor.putString(weaid + "temperature", jsonObject.getString("temperature"));
                            editor.putString(weaid + "humidity", jsonObject.getString("humidity"));
                            editor.putString(weaid + "wind", jsonObject.getString("wind"));
                            editor.putString(weaid + "winp", jsonObject.getString("winp"));
                            editor.putString(weaid + "weather", jsonObject.getString("weather"));
                            editor.commit();
                            //Log.e("TAG", "json"+tv_weather.getText().toString());
                            // 显示天气图片
                            String weatherStr = jsonObject.getString("weather");
                            rainSnow = jsonObject.getString("weather");
                            if (weatherStr == null) {
                                weatherStr = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(weaid + "weather", "晴");
                            }
                            ShowImageUtil.getInstance().showDayWeatherImage(getActivity(), weatherStr, im_weather, rtLayout);

                            rainOrSnow(rtLayout, rainSnow);
                        }
                    } catch (Exception e) {

                    }
                    //Toast.makeText(getActivity(), getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                    SnackUtil.SnackShort(rtLayout,getString(R.string.update_success));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getActivity(), getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
                    SnackUtil.SnackShort(rtLayout,getString(R.string.update_failed));
                    //Log.e("TAG", "请求实时天气失败");

                }
            });
            MyApp.getVolleyQueue().add(request);
        } else {
            SnackUtil.SnackShort(rtLayout,getString(R.string.net_error));
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

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.e("TAG", "请求PM2.5失败");
                    SnackUtil.SnackShort(rtLayout,getString(R.string.update_failed));

                }
            });
            MyApp.getVolleyQueue().add(request);
        } else {
            SnackUtil.SnackShort(rtLayout,getString(R.string.net_error));
        }

    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvFeature:
                // 查看天气走向
                Intent featurIntent = new Intent(getActivity(), FeatureWeatherActivity.class);
                featurIntent.putExtra("weaid", weaid);
                featurIntent.putExtra("citynm", citynm);
                startActivity(featurIntent);
                break;
            default:
                break;
        }
    }
}
