package com.humanheima.snaphelperdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.humanheima.snaphelperdemo.adapter.RecyclerViewAdapter;
import com.humanheima.snaphelperdemo.widget.MySnapHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    List<String> list;
    LinearLayoutManager layoutManager;
    RecyclerViewAdapter adapter;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    //LinearSnapHelper helper;
    MySnapHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("string--" + i);
        }
        adapter = new RecyclerViewAdapter(this, list);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        helper = new MySnapHelper();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        helper.attachToRecyclerView(recyclerView);
    }

    @OnClick(R.id.btnDelete)
    public void onClick() {
        list.remove(0);
        adapter.notifyDataSetChanged();
    }
}
