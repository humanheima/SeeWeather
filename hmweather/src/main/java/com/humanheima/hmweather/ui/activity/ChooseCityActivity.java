package com.humanheima.hmweather.ui.activity;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.humanheima.hmweather.R;
import com.humanheima.hmweather.base.BaseActivity;
import com.humanheima.hmweather.bean.CityInfo;
import com.humanheima.hmweather.ui.adapter.CityRVAdapter;
import com.humanheima.hmweather.utils.LogUtil;
import com.humanheima.hmweather.utils.T;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ChooseCityActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.RecyclerViewCity)
    RecyclerView recyclerViewCity;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private CityRVAdapter cityRVAdapter;
    private List<CityInfo> cityInfoList;

    @Override
    protected int bindLayout() {
        return R.layout.activity_choose_city;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        cityInfoList = new ArrayList<>();
        recyclerViewCity.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCity.setHasFixedSize(true);
        recyclerViewCity.setItemAnimator(new DefaultItemAnimator());
        cityRVAdapter = new CityRVAdapter(this, cityInfoList);
        cityRVAdapter.setItemClickListener(new CityRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                T.showToast(ChooseCityActivity.this, cityRVAdapter.getWeatherId());
            }
        });
        recyclerViewCity.setAdapter(cityRVAdapter);

        searchCityFromDb();
    }

    private void searchCityFromDb() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                cityInfoList.addAll(DataSupport.where("id > ?", "0").find(CityInfo.class));
                if (cityInfoList.size() > 0) {
                    LogUtil.e("searchCityFromDb", "cityInfoList" + cityInfoList.size());
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    cityRVAdapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }

    @Override
    protected void bindEvent() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
