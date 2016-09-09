package com.humanheima.hmweather.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/9/9.
 */
public class WeatherRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    public WeatherRVAdapter(Context context) {
        this.context = context;
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
