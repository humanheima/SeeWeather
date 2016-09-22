package com.humanheima.viewpagerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;
    Button btnAdd, btnDelete;
    List<Fragment> fragmentList;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        fragmentList = new ArrayList<>();
        //fragmentList.add(BlankFragment.newInstance("", ""));
        //fragmentList.add(BlankFragment.newInstance("", ""));
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        Class clz = this.getClass();
        MainActivity mainActivity = new MainActivity();
        startActivity(new Intent(MainActivity.this, mainActivity.getClass()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                fragmentList.add(BlankFragment.newInstance("", ""));
                adapter.notifyDataSetChanged();
                break;
            case R.id.btnDelete:
                startActivity(new Intent(MainActivity.this, PagerAdapterActivity.class));
                break;
        }
    }
}
