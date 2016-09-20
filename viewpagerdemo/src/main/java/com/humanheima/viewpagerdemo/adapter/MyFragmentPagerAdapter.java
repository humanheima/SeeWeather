package com.humanheima.viewpagerdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<View> viewList;

    public MyFragmentPagerAdapter(FragmentManager fm, List<View> viewList) {
        super(fm);
        this.viewList = viewList;
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
