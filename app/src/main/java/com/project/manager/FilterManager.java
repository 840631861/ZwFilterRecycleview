package com.project.manager;

import android.content.Context;
import android.view.View;
import android.widget.Filter;
import android.widget.LinearLayout;

import com.project.R;
import com.project.model.FilterCheckData;
import com.project.model.FilterData;
import com.project.view.FiltersDialog;

import java.util.List;

/**
 * 筛选菜单管理器
 * Created by Administrator on 2018/6/28.
 */

public class FilterManager implements View.OnClickListener {
    private Context context;
    private View view;

    private LinearLayout ll_filter_btn;//筛选按钮
    private FiltersDialog filtersDialog;//侧拉弹窗
    private IListView.OnFilterConfirmClickListener onFilterConfirmClickListener;//确认按钮点击事件
    private IListView.OnFilterItemChangeListener onFilterItemChangeListener;//item状态改变事件
    private IListView.OnFilterResetClickListener onFilterResetClickListener;//重置监听
    public static int FILTER_TYPE_SIN_ELECTION = 1;//单选
    public static int FILTER_TYPE_MUL_ELECTION = 2;//多选

    public FilterManager(Context context, View view) {
        this.context = context;
        this.view = view;

        initView();
        initEvent();
    }

    private void initView()
    {
        ll_filter_btn = view.findViewById(R.id.ll_filter_btn);
        ll_filter_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ll_filter_btn:
                if( filtersDialog == null )
                    filtersDialog = new FiltersDialog(context);
                filtersDialog.show();
                break;
        }
    }

    public FilterManager addCheckData(FilterCheckData data)
    {
        if( filtersDialog == null )
            filtersDialog = new FiltersDialog(context);
        filtersDialog.addData(data);
        return this;
    }

    public FilterManager addCheckDatas(List<FilterCheckData> datas)
    {
        if( filtersDialog == null )
            filtersDialog = new FiltersDialog(context);
        if( datas == null || datas.size() == 0 )
            return this;
        for( int i=0;i<datas.size();i++ )
            filtersDialog.addData(datas.get(i));
        return this;
    }

    public FilterData getFilterDatas()
    {
        return filtersDialog.getAllData();
    }

    public FiltersDialog getFiltersDialog()
    {
        return filtersDialog;
    }

    public void initEvent()
    {
        if( filtersDialog == null )
            filtersDialog = new FiltersDialog(context);

        filtersDialog.setOnConfirmClickListener(new FiltersDialog.OnConfirmClickListener() {
            @Override
            public void onConfirmClick(FilterData data) {
                if( onFilterConfirmClickListener != null )
                onFilterConfirmClickListener.onFilterConfirmClick(data);
            }
        });

        filtersDialog.setOnItemChangeListener(new FiltersDialog.OnItemChangeListener() {
            @Override
            public void onItemChangeListener(FilterData data) {
                if( onFilterItemChangeListener != null )
                onFilterItemChangeListener.onFilterItemChange(data);
            }
        });
        filtersDialog.setOnResetClickListener(new FiltersDialog.OnResetClickListener() {
            @Override
            public void onResetClick(FilterData data) {
                if( onFilterResetClickListener != null )
                onFilterResetClickListener.onFilterResetClick(data);
            }
        });
    }

    /////////////////////////////////////////////////////事件///////////////////////////////////////////
    //确认点击事件
    public FilterManager setOnFilterConfirmClickListener(IListView.OnFilterConfirmClickListener onFilterConfirmClickListener)
    {
        this.onFilterConfirmClickListener = onFilterConfirmClickListener;
        return this;
    }
    //item状态改变事件
    public FilterManager setOnFilterItemChangeListener(IListView.OnFilterItemChangeListener onFilterItemChangeListener)
    {
        this.onFilterItemChangeListener = onFilterItemChangeListener;
        return this;
    }
    //重置按钮事件
    public FilterManager setOnFilterResetClickListener(IListView.OnFilterResetClickListener onFilterResetClickListener)
    {
        this.onFilterResetClickListener = onFilterResetClickListener;
        return this;
    }
}
