package com.humanheima.hmweather.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.humanheima.hmweather.R;
import com.humanheima.hmweather.bean.CityInfo;
import com.humanheima.hmweather.listener.OnItemClickListener;
import com.humanheima.hmweather.listener.OnLoadMoreListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dmw on 2016/9/11.
 * 城市选择界面的RecyckerView 适配器
 */
public class CityRVAdapter extends LoadMoreAdapter {
    private Context context;
    private List<CityInfo> cityInfoList;
    private OnItemClickListener itemClickListener;
    private String weatherId;

    public String getWeatherId() {
        return weatherId;
    }

    private String city;
    private String cityPinyin;
    private CityInfo cityInfo;


    public CityRVAdapter(RecyclerView recyclerView, List<CityInfo> cityInfoList, OnLoadMoreListener onloadMoreListener) {
        super(recyclerView, onloadMoreListener);
        this.cityInfoList = cityInfoList;
        context = recyclerView.getContext();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOAD_MORE_ITEM) {
            return super.onCreateViewHolder(parent, viewType);
        } else {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_city, parent, false);
            return new CityRVHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof CityRVHolder && position < cityInfoList.size()) {
            cityInfo = cityInfoList.get(position);
            city = cityInfo.getCity();
            cityPinyin = cityInfo.getCityPinyin();
            weatherId = cityInfo.getWeatherId();
            ((CityRVHolder) holder).textItemCity.setText(city);
        }

    }

    @Override
    int getDataSize() {
        if (cityInfoList == null) {
            return 0;
        } else {
            return cityInfoList.size();
        }
    }

    static class CityRVHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_item_city)
        TextView textItemCity;
        @BindView(R.id.cardView)
        CardView cardView;

        CityRVHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
