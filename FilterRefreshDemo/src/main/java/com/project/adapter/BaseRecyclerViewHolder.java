package com.project.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.SparseArray;
import android.view.View;

public class BaseRecyclerViewHolder extends ViewHolder {
    private SparseArray<View> views;
    private View convertView;

    public BaseRecyclerViewHolder(View convertView) {
        super(convertView);
        this.views = new SparseArray<View>();
        this.convertView = convertView;
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return convertView;
    }
}
