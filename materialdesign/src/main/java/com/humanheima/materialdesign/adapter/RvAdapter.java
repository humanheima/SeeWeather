package com.humanheima.materialdesign.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.humanheima.materialdesign.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/23.
 */

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.VH> {
    private List<String> stringList;
    private Context context;

    public RvAdapter(List<String> stringList, Context context) {
        this.stringList = stringList;
        this.context = context;
    }

    @Override
    public RvAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(RvAdapter.VH holder, int position) {
        holder.toolbar.setTitle(stringList.get(position));
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.toolbar)
        Toolbar toolbar;
        @BindView(R.id.text_view)
        TextView textView;

        VH(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
