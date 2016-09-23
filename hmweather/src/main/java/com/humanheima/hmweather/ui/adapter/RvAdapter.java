package com.humanheima.hmweather.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humanheima.hmweather.R;
import com.humanheima.hmweather.bean.HeWeather;
import com.humanheima.hmweather.utils.CommonUtil;
import com.humanheima.hmweather.utils.ImageUtil;
import com.humanheima.hmweather.utils.LogUtil;
import com.humanheima.hmweather.utils.SPUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dumingwei on 2016/9/22.
 * 使用场景：MainActivity 从网络或者数据库查询完数据后，组成一个List传递过来
 */

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.VH> {

    private Context context;
    private List<HeWeather> weatherList;
    private View rootView;
    //当日小时预报
    private List<HeWeather.HourlyForecastBean> hourlyForecastList;
    private TextView[] mClock;
    private TextView[] mTemp;
    private TextView[] mHumidity;
    private TextView[] mWind;

    //未来每天天气预报
    private List<HeWeather.DailyForecastBean> dailyForecastList;
    private TextView[] forecastDate;
    private TextView[] forecastTemp;
    private TextView[] forecastTxt;
    private ImageView[] forecastIcon;

    public RvAdapter(Context context, List<HeWeather> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        rootView = LayoutInflater.from(context).inflate(R.layout.item_weather, parent, false);
        return new VH(rootView);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        LogUtil.e("tag", "onBindViewHolder");
        HeWeather weather = weatherList.get(position);
        //当前天气信息
        holder.textCity.setText(weather.getBasic().getCity());
        holder.tempFlu.setText(String.format("%s℃", weather.getNow().getTmp()));
        holder.tempMax.setText(String.format("↑ %s °", weather.getDaily_forecast().get(0).getTmp().getMax()));
        holder.tempMin.setText(String.format("↓ %s °", weather.getDaily_forecast().get(0).getTmp().getMin()));
        holder.tempPm.setText(String.format("PM2.5: %s μg/m³", CommonUtil.safeText(weather.getAqi().getCity().getPm25())));
        holder.tempQuality.setText(CommonUtil.safeText("空气质量： ", weather.getAqi().getCity().getQlty()));
        ImageUtil.loadImg(context, holder.weatherIcon, SPUtil.getInstance().getInt(weather.getNow().getCond().getTxt()));
        //小时预报
        hourlyForecastList = weather.getHourly_forecast();
        mClock = new TextView[hourlyForecastList.size()];
        mTemp = new TextView[hourlyForecastList.size()];
        mHumidity = new TextView[hourlyForecastList.size()];
        mWind = new TextView[hourlyForecastList.size()];

        holder.llHourForecast.removeAllViews();
        for (int i = 0; i < hourlyForecastList.size(); i++) {
            View hourView = View.inflate(context, R.layout.item_hour_info_line, null);
            mClock[i] = (TextView) hourView.findViewById(R.id.one_clock);
            mTemp[i] = (TextView) hourView.findViewById(R.id.one_temp);
            mHumidity[i] = (TextView) hourView.findViewById(R.id.one_humidity);
            mWind[i] = (TextView) hourView.findViewById(R.id.one_wind);
            holder.llHourForecast.addView(hourView);
        }
        for (int i = 0; i < hourlyForecastList.size(); i++) {
            //s.subString(s.length-3,s.length);
            //第一个参数是开始截取的位置，第二个是结束位置。
            String mDate = hourlyForecastList.get(i).getDate();
            mClock[i].setText(mDate.substring(mDate.length() - 5, mDate.length()));
            mTemp[i].setText(String.format("%s℃", hourlyForecastList.get(i).getTmp()));
            mHumidity[i].setText(String.format("%s%%", hourlyForecastList.get(i).getHum()));
            mWind[i].setText(String.format("%sKm/h", hourlyForecastList.get(i).getWind().getSpd()));

        }
        //当日建议：穿衣，运动，啥的
        holder.textClothBrief.setText(String.format("穿衣指数---%s", weather.getSuggestion().getDrsg().getBrf()));
        holder.textClothDetail.setText(weather.getSuggestion().getDrsg().getTxt());
        holder.textSportBrief.setText(String.format("运动指数---%s", weather.getSuggestion().getSport().getBrf()));
        holder.textSportDetail.setText(weather.getSuggestion().getSport().getTxt());
        holder.textTravelBrief.setText(String.format("旅游指数---%s", weather.getSuggestion().getTrav().getBrf()));
        holder.textTravelDetail.setText(weather.getSuggestion().getTrav().getTxt());
        holder.textFluBrief.setText(String.format("感冒指数---%s", weather.getSuggestion().getFlu().getBrf()));
        holder.textFluDetail.setText(weather.getSuggestion().getFlu().getTxt());
        //未来天气
        dailyForecastList = weather.getDaily_forecast();
        forecastDate = new TextView[dailyForecastList.size()];
        forecastTemp = new TextView[dailyForecastList.size()];
        forecastTxt = new TextView[dailyForecastList.size()];
        forecastIcon = new ImageView[dailyForecastList.size()];
        holder.llDayForecast.removeAllViews();
        for (int i = 0; i < dailyForecastList.size(); i++) {
            View dayView = View.inflate(context, R.layout.item_forecast_line, null);
            forecastDate[i] = (TextView) dayView.findViewById(R.id.forecast_date);
            forecastTemp[i] = (TextView) dayView.findViewById(R.id.forecast_temp);
            forecastTxt[i] = (TextView) dayView.findViewById(R.id.forecast_txt);
            forecastIcon[i] = (ImageView) dayView.findViewById(R.id.forecast_icon);
            holder.llDayForecast.addView(dayView);
        }
        //今日 明日
        forecastDate[0].setText("今日");
        forecastDate[1].setText("明日");
        for (int i = 0; i < dailyForecastList.size(); i++) {
            if (i > 1) {
                try {
                    //forecastDate[i].setText(CommonUtil.dayForWeek(dailyForecastList.get(i).getDate()));
                    forecastDate[i].setText(dailyForecastList.get(i).getDate());
                } catch (Exception e) {
                    LogUtil.e("bindDailyForecast", e.toString());
                }
            }
            ImageUtil.loadImg(context, forecastIcon[i], SPUtil.getInstance().getInt(dailyForecastList.get(i).getCond().getTxt_d()));
            forecastTemp[i].setText(String.format("%s℃ - %s℃", dailyForecastList.get(i).getTmp().getMin(), dailyForecastList.get(i).getTmp().getMax()));
            forecastTxt[i].setText(
                    String.format("%s。 %s %s %s km/h。 降水几率 %s%%。",
                            dailyForecastList.get(i).getCond().getTxt_d(),
                            dailyForecastList.get(i).getWind().getSc(),
                            dailyForecastList.get(i).getWind().getDir(),
                            dailyForecastList.get(i).getWind().getSpd(),
                            dailyForecastList.get(i).getPop()));
        }
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.weather_icon)
        ImageView weatherIcon;
        @BindView(R.id.temp_flu)
        TextView tempFlu;
        @BindView(R.id.temp_max)
        TextView tempMax;
        @BindView(R.id.temp_min)
        TextView tempMin;
        @BindView(R.id.temp_layout)
        LinearLayout tempLayout;
        @BindView(R.id.linear_temp)
        LinearLayout linearTemp;
        @BindView(R.id.text_city)
        TextView textCity;
        @BindView(R.id.temp_pm)
        TextView tempPm;
        @BindView(R.id.temp_quality)
        TextView tempQuality;
        @BindView(R.id.ll_hour_forecast)
        LinearLayout llHourForecast;
        @BindView(R.id.text_cloth_brief)
        TextView textClothBrief;
        @BindView(R.id.text_cloth_detail)
        TextView textClothDetail;
        @BindView(R.id.text_sport_brief)
        TextView textSportBrief;
        @BindView(R.id.text_sport_detail)
        TextView textSportDetail;
        @BindView(R.id.text_travel_brief)
        TextView textTravelBrief;
        @BindView(R.id.text_travel_detail)
        TextView textTravelDetail;
        @BindView(R.id.text_flu_brief)
        TextView textFluBrief;
        @BindView(R.id.text_flu_detail)
        TextView textFluDetail;
        @BindView(R.id.ll_day_forecast)
        LinearLayout llDayForecast;

        VH(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
