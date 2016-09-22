package com.humanheima.hmweather.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.humanheima.hmweather.bean.HeWeather;

import java.util.List;

/**
 * Created by dumingwei on 2016/9/22.
 * 使用场景：MainActivity 从网络或者数据库查询完数据后，组成一个List传递过来
 */

public class RvAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<HeWeather>weatherList;

    public RvAdapter(Context context, List<HeWeather> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
