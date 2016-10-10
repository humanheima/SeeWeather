package com.humanheima.md50above.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.humanheima.md50above.fragment.BlankFragment;

import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<BlankFragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, List<BlankFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Tab 1";
            case 1:
                return "Tab 2";
        }
        return "";
    }
}
