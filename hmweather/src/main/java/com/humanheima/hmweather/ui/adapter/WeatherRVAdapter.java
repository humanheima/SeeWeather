package com.humanheima.hmweather.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
 * Created by Administrator on 2016/9/9.
 */
public class WeatherRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_ONE = 0;

    private final int TYPE_TWO = 1;

    private final int TYPE_THREE = 2;
    private final int TYPE_FORE = 3;
    private static Context context;
    private HeWeather heWeather;
    private List<HeWeather> heWeatherList;
    List<HeWeather.HourlyForecastBean> hourlyForecastList;
    List<HeWeather.DailyForecastBean> dailyForecastList;

    public WeatherRVAdapter(Context context, List<HeWeather> heWeatherList) {
        this.context = context;
        this.heWeatherList = heWeatherList;
        heWeather = this.heWeatherList.get(0);
        hourlyForecastList = heWeather.getHourly_forecast();
        dailyForecastList = heWeather.getDaily_forecast();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == TYPE_ONE) {
            return TYPE_ONE;
        } else if (position == TYPE_TWO) {
            return TYPE_TWO;
        } else if (position == TYPE_THREE) {
            return TYPE_THREE;
        } else {
            return TYPE_FORE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ONE:
                return new NowWeatherViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.item_temperature, parent, false));
            case TYPE_TWO:
                return new HoursWeatherViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.item_hour_info, parent, false));
            case TYPE_THREE:
                return new SuggestionViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.item_suggestion, parent, false));
            case TYPE_FORE:
                return new ForecastViewHolder(LayoutInflater.from(context).inflate(R.layout.item_forecast, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        switch (itemType) {
            case TYPE_ONE:
                ((NowWeatherViewHolder) holder).bindNowWeather(heWeather);
                break;
            case TYPE_TWO:
                ((HoursWeatherViewHolder) holder).bindHourlyForcast();
                break;
            case TYPE_THREE:
                ((SuggestionViewHolder) holder).bindSuggestion(heWeather);
                break;
            case TYPE_FORE:
                ((ForecastViewHolder) holder).bindDailyForecast();
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (!TextUtils.isEmpty(heWeather.getStatus())) ? 4 : 0;
    }

    /**
     * 当前天气
     */
    static class NowWeatherViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.temp_pm)
        TextView tempPm;
        @BindView(R.id.temp_quality)
        TextView tempQuality;
        @BindView(R.id.cardView)
        CardView cardView;

        NowWeatherViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindNowWeather(HeWeather weather) {
            try {
                tempFlu.setText(String.format("%s℃", weather.getNow().getTmp()));
                tempMax.setText(String.format("↑ %s °", weather.getDaily_forecast().get(0).getTmp().getMax()));
                tempMin.setText(String.format("↓ %s °", weather.getDaily_forecast().get(0).getTmp().getMin()));

                tempPm.setText(String.format("PM2.5: %s μg/m³", CommonUtil.safeText(weather.getAqi().getCity().getPm25())));
                tempQuality.setText(CommonUtil.safeText("空气质量： ", weather.getAqi().getCity().getQlty()));

                //ImageLoader.load(itemView.getContext(), SharedPreferenceUtil.getInstance().getInt(weather.now.cond.txt, R.mipmap.none), weatherIcon);
                ImageUtil.loadImg(context, weatherIcon, SPUtil.getInstance().getInt(weather.getNow().getCond().getTxt()));
            } catch (Exception e) {
                LogUtil.e("NowWeather", e.toString());
            }
        }
    }

    /**
     * 当日小时预告
     */
    class HoursWeatherViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout itemHourInfoLinearlayout;
        private TextView[] mClock = new TextView[hourlyForecastList.size()];
        private TextView[] mTemp = new TextView[hourlyForecastList.size()];
        private TextView[] mHumidity = new TextView[hourlyForecastList.size()];
        private TextView[] mWind = new TextView[hourlyForecastList.size()];

        public HoursWeatherViewHolder(View itemView) {
            super(itemView);
            itemHourInfoLinearlayout = (LinearLayout) itemView.findViewById(R.id.item_hour_info_linearlayout);

            for (int i = 0; i < hourlyForecastList.size(); i++) {
                View view = View.inflate(context, R.layout.item_hour_info_line, null);
                mClock[i] = (TextView) view.findViewById(R.id.one_clock);
                mTemp[i] = (TextView) view.findViewById(R.id.one_temp);
                mHumidity[i] = (TextView) view.findViewById(R.id.one_humidity);
                mWind[i] = (TextView) view.findViewById(R.id.one_wind);
                itemHourInfoLinearlayout.addView(view);
            }
        }

        public void bindHourlyForcast() {
            try {
                for (int i = 0; i < hourlyForecastList.size(); i++) {
                    //s.subString(s.length-3,s.length);
                    //第一个参数是开始截取的位置，第二个是结束位置。
                    String mDate = hourlyForecastList.get(i).getDate();
                    mClock[i].setText(mDate.substring(mDate.length() - 5, mDate.length()));
                    mTemp[i].setText(String.format("%s℃", hourlyForecastList.get(i).getTmp()));
                    mHumidity[i].setText(String.format("%s%%", hourlyForecastList.get(i).getHum()));
                    mWind[i].setText(String.format("%sKm/h", hourlyForecastList.get(i).getWind().getSpd()));
                }
            } catch (Exception e) {
                //Snackbar.make(holder.itemView, R.string.api_error, Snackbar.LENGTH_SHORT).show();
                LogUtil.e("HoursWeather", e.getMessage());
            }
        }
    }

    /**
     * 当日建议
     */
    static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cloth_brief)
        TextView clothBrief;
        @BindView(R.id.cloth_txt)
        TextView clothTxt;
        @BindView(R.id.sport_brief)
        TextView sportBrief;
        @BindView(R.id.sport_txt)
        TextView sportTxt;
        @BindView(R.id.travel_brief)
        TextView travelBrief;
        @BindView(R.id.travel_txt)
        TextView travelTxt;
        @BindView(R.id.flu_brief)
        TextView fluBrief;
        @BindView(R.id.flu_txt)
        TextView fluTxt;
        @BindView(R.id.cardView)
        CardView cardView;

        SuggestionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindSuggestion(HeWeather weather) {
            try {

                clothBrief.setText(String.format("穿衣指数---%s", weather.getSuggestion().getDrsg().getBrf()));
                clothTxt.setText(weather.getSuggestion().getDrsg().getTxt());

                sportBrief.setText(String.format("运动指数---%s", weather.getSuggestion().getSport().getBrf()));
                sportTxt.setText(weather.getSuggestion().getSport().getTxt());

                travelBrief.setText(String.format("旅游指数---%s", weather.getSuggestion().getTrav().getBrf()));
                travelTxt.setText(weather.getSuggestion().getTrav().getTxt());

                fluBrief.setText(String.format("感冒指数---%s", weather.getSuggestion().getFlu().getBrf()));
                fluTxt.setText(weather.getSuggestion().getFlu().getTxt());
            } catch (Exception e) {
                LogUtil.e("SuggestionViewHolder", e.toString());
            }
        }
    }

    /**
     * 未来天气
     */
    class ForecastViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout forecastLinear;
        private TextView[] forecastDate = new TextView[dailyForecastList.size()];
        private TextView[] forecastTemp = new TextView[dailyForecastList.size()];
        private TextView[] forecastTxt = new TextView[dailyForecastList.size()];
        private ImageView[] forecastIcon = new ImageView[dailyForecastList.size()];

        public ForecastViewHolder(View itemView) {
            super(itemView);
            forecastLinear = (LinearLayout) itemView.findViewById(R.id.forecast_linear);
            for (int i = 0; i < dailyForecastList.size(); i++) {
                View view = View.inflate(context, R.layout.item_forecast_line, null);
                forecastDate[i] = (TextView) view.findViewById(R.id.forecast_date);
                forecastTemp[i] = (TextView) view.findViewById(R.id.forecast_temp);
                forecastTxt[i] = (TextView) view.findViewById(R.id.forecast_txt);
                forecastIcon[i] = (ImageView) view.findViewById(R.id.forecast_icon);
                forecastLinear.addView(view);
            }
        }

        public void bindDailyForecast() {
            try {
                //今日 明日
                forecastDate[0].setText("今日");
                forecastDate[1].setText("明日");
                for (int i = 0; i < dailyForecastList.size(); i++) {
                    if (i > 1) {
                        try {
                            forecastDate[i].setText(CommonUtil.dayForWeek(dailyForecastList.get(i).getDate()));
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
            } catch (Exception e) {
                LogUtil.e("bindDailyForecast", e.toString());
            }
        }
    }
}
