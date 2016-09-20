package com.humanheima.viewpagerdemo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.humanheima.viewpagerdemo.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PagerAdapterActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    private MyPagerAdapter pagerAdapter;
    private List<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        ButterKnife.bind(this);
        viewList = new ArrayList<>();
        pagerAdapter = new MyPagerAdapter(viewList);
        viewPager.setAdapter(pagerAdapter);
        View view0 = LayoutInflater.from(this).inflate(R.layout.fragment_blank, null);
        pagerAdapter.addView(view0, 0);
        Log.e("viewpager", viewPager.getCurrentItem() + "," + viewPager.getChildCount());
    }

    @OnClick({R.id.btnAdd, R.id.btnDelete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                View addView = LayoutInflater.from(this).inflate(R.layout.fragment_blank, null);
                addView(addView);
                break;
            case R.id.btnDelete:
                int index = viewPager.getCurrentItem();
                int childCount = viewPager.getChildCount();
                Log.e("viewpager", index + "," + childCount);
                removeView(pagerAdapter.getView(index));

                break;
        }
    }

    // Here's what the app should do to add a view to the ViewPager.
    public void addView(View newPage) {
        int pageIndex = pagerAdapter.addView(newPage);
        // You might want to make "newPage" the currently displayed page:
        viewPager.setCurrentItem(pageIndex, true);
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to remove a view from the ViewPager.
    public void removeView(View defunctPage) {
        if (defunctPage != null) {
            int pageIndex = pagerAdapter.removeView(viewPager, defunctPage);
            // You might want to choose what page to display, if the current page was "defunctPage".
            if (pageIndex == pagerAdapter.getCount())
                pageIndex--;
            viewPager.setCurrentItem(pageIndex);
        }
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to get the currently displayed page.
    public View getCurrentPage() {
        return pagerAdapter.getView(viewPager.getCurrentItem());
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to set the currently displayed page.  "pageToShow" must
    // currently be in the adapter, or this will crash.
    public void setCurrentPage(View pageToShow) {
        viewPager.setCurrentItem(pagerAdapter.getItemPosition(pageToShow), true);
    }

}
