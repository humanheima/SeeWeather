package com.humanheima.hmweather.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.humanheima.hmweather.base.BaseFragment;

import java.util.List;

/**
 * Created by dmw on 2016/9/9.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<BaseFragment> fragmentList;
    List<String> titleList;

    public ViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
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
