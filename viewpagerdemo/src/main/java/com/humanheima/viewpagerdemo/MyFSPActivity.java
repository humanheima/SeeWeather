package com.humanheima.viewpagerdemo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.humanheima.viewpagerdemo.adapter.MyFragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyFSPActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    MyFragmentStatePagerAdapter adapter;
    private List<BlankFragment> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fsp);
        ButterKnife.bind(this);
        viewList = new ArrayList<>();
        adapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager(), viewList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(14);
    }

    @OnClick({R.id.btnAdd, R.id.btnDelete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                viewList.add(BlankFragment.newInstance("", ""));
                adapter.notifyDataSetChanged();
                break;
            case R.id.btnDelete:
                if (viewList.size() > 0) {
                    BlankFragment fragment = viewList.get(viewPager.getCurrentItem());
                    if (fragment != null) {
                        adapter.destroyItem(viewPager, viewPager.getCurrentItem(), fragment);
                        viewList.remove(fragment);
                        adapter.notifyDataSetChanged();
                    }
                }

                break;
        }
    }
}
