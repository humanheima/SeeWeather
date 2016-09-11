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
import com.humanheima.hmweather.listener.OnItemClickListener;
import com.humanheima.hmweather.listener.OnLoadMoreListener;
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
    private List<CityInfo> tempCityInfoList;
    private static int SIZE = 0;
    AsynLoacCity asynTask;

    @Override
    protected int bindLayout() {
        return R.layout.activity_choose_city;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        cityInfoList = new ArrayList<>();
        tempCityInfoList = new ArrayList<>();
        recyclerViewCity.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCity.setHasFixedSize(true);
        recyclerViewCity.setItemAnimator(new DefaultItemAnimator());
        new AsynLoacCity().execute(SIZE);
    }

    private void setAdapter() {
        if (cityRVAdapter == null) {
            cityRVAdapter = new CityRVAdapter(recyclerViewCity, cityInfoList, new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    //每次上拉加载更多之前设置setLoadAll(false)
                    new AsynLoacCity().execute(SIZE);
                }
            });
            cityRVAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    T.showToast(ChooseCityActivity.this, cityRVAdapter.getWeatherId());
                }
            });
            recyclerViewCity.setAdapter(cityRVAdapter);
        }
        //更新适配器
        cityRVAdapter.reset();
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

    /**
     * 加载城市列表，每次加载100条
     */
    class AsynLoacCity extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... params) {
            tempCityInfoList.clear();
            String start = String.valueOf(params[0]);
            String end = String.valueOf(params[0] + 10);

            tempCityInfoList = DataSupport.where("id > ?", start).where("id<=?", end).find(CityInfo.class);
            if (tempCityInfoList.size() > 0) {
                LogUtil.e("tag", tempCityInfoList.size() + "");
                cityInfoList.addAll(tempCityInfoList);
                SIZE += 10;//每次查询成功，就把SIZE加100；
                return true;
            } else {
                LogUtil.e("tag", "doInBackground 查询失败");
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            setAdapter();

        }
    }

}
