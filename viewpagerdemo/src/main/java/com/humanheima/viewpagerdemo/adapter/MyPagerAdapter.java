package com.humanheima.viewpagerdemo.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmw on 2016/9/20.
 */
public class MyPagerAdapter extends PagerAdapter {

    private List<View> views = new ArrayList<>();

    public MyPagerAdapter(List<View> views) {
        this.views = views;
    }

    /**
     * Used by ViewPager.  "Object" represents the page; tell the ViewPager where the
     * page should be displayed, from left-to-right.  If the page no longer exists,
     * return POSITION_NONE.
     */
    @Override
    public int getItemPosition(Object object) {
        int index = views.indexOf(object);
        if (index == -1) {
            //表示views中已经没有这个object
            return POSITION_NONE;
        } else {
            return index;
        }
    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager.  Called when ViewPager needs a page to display; it is our job
    // to add the page to the container, which is normally the ViewPager itself.  Since
    // all our pages are persistent, we simply retrieve it from our "views" ArrayList.
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = views.get(position);
        container.addView(v);
        return v;
    }


    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager.  Called when ViewPager no longer needs a page to display; it
    // is our job to remove the page from the container, which is normally the
    // ViewPager itself.  Since all our pages are persistent, we do nothing to the
    // contents of our "views" ArrayList.
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }


    //-----------------------------------------------------------------------------
    // Add "view" to right end of "views".
    // Returns the position of the new view.
    // The app should call this to add pages; not used by ViewPager.
    public int addView(View v) {
        return addView(v, views.size());
    }

    //-----------------------------------------------------------------------------
    // Add "view" at "position" to "views".
    // Returns position of new view.
    // The app should call this to add pages; not used by ViewPager.
    public int addView(View v, int position) {
        views.add(position, v);
        notifyDataSetChanged();
        return position;
    }

    //-----------------------------------------------------------------------------
    // Removes "view" from "views".
    // Retuns position of removed view.
    // The app should call this to remove pages; not used by ViewPager.
    public int removeView(ViewPager pager, View v) {
        int index = views.indexOf(v);
        return removeView(pager, index);
    }

    //-----------------------------------------------------------------------------
    // Removes the "view" at "position" from "views".
    // Retuns position of removed view.
    // The app should call this to remove pages; not used by ViewPager.
    public int removeView(ViewPager pager, int position) {
        // ViewPager doesn't have a delete method; the closest is to set the adapter
        // again.  When doing so, it deletes all its views.  Then we can delete the view
        // from from the adapter and finally set the adapter to the pager again.  Note
        // that we set the adapter to null before removing the view from "views" - that's
        // because while ViewPager deletes all its views, it will call destroyItem which
        // will in turn cause a null pointer ref.
        pager.setAdapter(null);
        views.remove(position);
        pager.setAdapter(this);
        return position;
    }

    //-----------------------------------------------------------------------------
    // Returns the "view" at "position".
    // The app should call this to retrieve a view; not used by ViewPager.
    public View getView(int position) {
        if (views.size() > 0) {
            return views.get(position);
        } else {
            return null;
        }

    }

}
