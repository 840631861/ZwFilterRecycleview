package com.project.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    protected List _data;
    public ListState listState = ListState.NONE;

    public enum ListState {
        NONE, REFRESHING, LOADMOREING;
    }


    public List getData() {
        return _data == null ? (_data = new ArrayList()) : _data;
    }

    @SuppressWarnings("rawtypes")
    public void setData(List data) {
        _data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (_data == null) {
            return 0;
        }
        return _data.size();
    }

    public Object getItem(int position) {
        if (_data != null && _data.size() > position) {
            return _data.get(position);
        }
        return null;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder holder = new BaseRecyclerViewHolder(getHolderView(parent,viewType));
        return holder;
    }

    public abstract View getHolderView(ViewGroup parent, int position);
}
