package com.humanheima.hmweather.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.humanheima.hmweather.ui.fragment.WeatherFragment;

import java.util.List;

/**
 * Created by dmw on 2016/9/9.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<WeatherFragment> fragmentList;

    public ViewPagerAdapter(FragmentManager fm, List<WeatherFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
