package com.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.library.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/12.
 */

public class NomalAdapter extends BaseRecyclerAdapter
{
    private Context context;
    public NomalAdapter(Context context,List<Map<String,String>> data) {
        this.context = context;
        setData(data);
    }

    @Override
    public View getHolderView(ViewGroup parent, int position) {
        View root = LayoutInflater.from(context).inflate(R.layout.item_spinner, parent, false);
        return root;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        TextView tv = holder.getView(R.id.tv_name);
        Map<String,String> item = (Map<String, String>) _data.get(position);
        tv.setText(item.get("id"));
    }
}
