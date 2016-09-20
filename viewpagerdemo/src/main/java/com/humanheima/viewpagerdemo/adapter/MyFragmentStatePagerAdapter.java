package com.humanheima.viewpagerdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.humanheima.viewpagerdemo.BlankFragment;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private List<BlankFragment> fragmentList;

    public MyFragmentStatePagerAdapter(FragmentManager fm, List<BlankFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
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
