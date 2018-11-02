package com.zw.filterrv.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.library.R;
import com.zw.filterrv.commom.comm;
import com.zw.filterrv.manager.FilterManager;
import com.zw.filterrv.manager.ViewBarManager;
import com.zw.filterrv.model.ZwFilterCheckDataItem;
import com.zw.filterrv.model.ZwFilterData;
import com.zw.filterrv.model.ZwParams;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/22.
 */

public class ZwFilterRefreshView extends LinearLayout
{
    LayoutInflater inflater;
    private Context context;

    private ViewBarManager viewBarManager;
    private FilterManager filterManager;
    private View view;
    private PullToRefreshLayout refreshLayout;
    private MaxRecyclerView recyclerView;

    public ZwFilterRefreshView(Context context) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public ZwFilterRefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflater = LayoutInflater.from(context);
        initView(context);
        initAttr(attrs);
    }

    private void initView(Context context)
    {
        view = LayoutInflater.from(context).inflate(R.layout.zw_filter_refreshview, this);
        refreshLayout = (PullToRefreshLayout)findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void initAttr(AttributeSet attrs)
    {
        viewBarManager = new ViewBarManager(context,view);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.ZwFilterRefreshView);

        int barTxtSize = (int)ta.getDimension(R.styleable.ZwFilterRefreshView_barTxtSize,15);
        int barHeight = (int)ta.getDimension(R.styleable.ZwFilterRefreshView_barHeight,35);
        int barTxtColor = ta.getColor(R.styleable.ZwFilterRefreshView_barTxtColor,context.getResources().getColor(R.color.gray_8));
        int barTxtColorActive = ta.getColor(R.styleable.ZwFilterRefreshView_barTxtColorActive,context.getResources().getColor(R.color.colorAccent));
        Drawable barImgCom = ta.getDrawable(R.styleable.ZwFilterRefreshView_barImgCom);
        Drawable barImgComActive = ta.getDrawable(R.styleable.ZwFilterRefreshView_barImgComActive);
        Drawable barImgFilter = ta.getDrawable(R.styleable.ZwFilterRefreshView_barImgFilter);
        String barFilterText = ta.getString(R.styleable.ZwFilterRefreshView_barFilterText);

        if( barTxtSize > 0 )
        viewBarManager.setBarTxtSize(barTxtSize);
        if( barHeight > 0 )
        viewBarManager.setBarHeight(barHeight);
        viewBarManager.setBarTxtColor(barTxtColor);
        viewBarManager.setBarTxtColorActive(barTxtColorActive);
        if( barImgCom != null && barImgComActive != null )
        viewBarManager.setBarImgCom(barImgCom,barImgComActive);
        if( barImgFilter != null )
        viewBarManager.setBarImgFilter(barImgFilter);

        if( !comm.isEmpty(barFilterText) )
            viewBarManager.setFilterTxt(barFilterText);
    }

    /**
     * 获取顶部栏ListViewBarManager对象
     * @return
     */
    public ViewBarManager getViewBarManager()
    {
        if( viewBarManager == null )
            viewBarManager = new ViewBarManager(context,view);
        return viewBarManager;
    }

    //获取筛选条件FilterManager对象
    public FilterManager getFilterManager()
    {
        if( filterManager == null )
            filterManager = new FilterManager(context,view);
        return filterManager;
    }

    /**
     * 绘制列表界面
     */
    public ZwFilterRefreshView showView()
    {
        getViewBarManager().updateBarView();
        return this;
    }

    /**
     * 更新界面
     */
    public ZwFilterRefreshView updateView()
    {
        showView();
        return this;
    }

    //获取标题栏中当前选中的数据
    public ZwParams getBarCurData()
    {
        ZwParams params = new ZwParams();
        ZwFilterCheckDataItem baCheckItem = getViewBarManager().getBarCheckedItemData();
        ZwFilterData filterData = getFilterManager().getFilterDatas();
        ArrayList<ZwFilterCheckDataItem> markData = getViewBarManager().getMarkData();
        params.setBarCheckItem(baCheckItem);
        params.setFilterData(filterData);
        params.setMarkData(markData);
        return params;
    }

    public PullToRefreshLayout getRefreshLayout(){
        return refreshLayout;
    }

    public MaxRecyclerView getRecyclerView(){
        return recyclerView;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
