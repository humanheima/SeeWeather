package com.humanheima.viewpagerdemo.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.humanheima.viewpagerdemo.R;
import com.humanheima.viewpagerdemo.ui.fragment.BlankFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabLayoutActivity extends AppCompatActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private List<Fragment> fragments;
    private TabLayout.Tab tabQQ, tabSina, tabWechat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);
        ButterKnife.bind(this);
        //tab，添加setCustomView
        tabQQ = tabLayout.newTab().setCustomView(R.layout.tab_item_qq);
        tabSina = tabLayout.newTab().setCustomView(R.layout.tab_item_sina);
        tabWechat = tabLayout.newTab().setCustomView(R.layout.tab_item_weixin);
        //tabLayout addTab
        tabLayout.addTab(tabQQ);
        tabLayout.addTab(tabSina);
        tabLayout.addTab(tabWechat);

        fragments = new ArrayList<>();
        //给fragments 添加三个fragment
        fragments.add(BlankFragment.newInstance("QQfragment"));
        fragments.add(BlankFragment.newInstance("微博fragment"));
        fragments.add(BlankFragment.newInstance("微信fragment"));

        //给viewPager设置适配器
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

        });
        //tabLayout 添加tab切换的监听事件
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //得到当前选中的tab的位置，切换相应的fragment
                int nowPosition = tab.getPosition();
                viewPager.setCurrentItem(nowPosition);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //给viewPager添加切换监听，
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //选中相应的tab
                switch (position) {
                    case 0:
                        tabQQ.select();
                        break;
                    case 1:
                        tabSina.select();
                        break;
                    case 2:
                        tabWechat.select();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //然后让TabLayout和ViewPager关联，只需要一句话，简直也是没谁了.
        //tabLayout.setupWithViewPager(viewPager);
    }
}

