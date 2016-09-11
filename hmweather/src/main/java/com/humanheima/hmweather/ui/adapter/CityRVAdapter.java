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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dmw on 2016/9/11.
 * 城市选择界面的RecyckerView 适配器
 */
public class CityRVAdapter extends RecyclerView.Adapter<CityRVAdapter.CityRVHolder> {

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

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CityRVAdapter(Context context, List<CityInfo> cityInfoList) {
        this.context = context;
        this.cityInfoList = cityInfoList;

    }

    @Override
    public CityRVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new CityRVHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CityRVHolder holder, final int position) {
        cityInfo = cityInfoList.get(position);
        city = cityInfo.getCity();
        cityPinyin = cityInfo.getCityPinyin();
        weatherId = cityInfo.getWeatherId();
        holder.textItemCity.setText(city);
        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cityInfoList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
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
