package com.project.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.project.R;
import com.project.adapter.NomalAdapter;
import com.project.commom.comm;
import com.project.manager.FilterManager;
import com.project.manager.ViewBarManager;
import com.project.model.FilterCheckDataItem;
import com.project.model.FilterData;
import com.project.model.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private RecyclerView recyclerView;

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
        Drawable barImgSortDefault = ta.getDrawable(R.styleable.ZwFilterRefreshView_barImgSortDefault);
        Drawable barImgSortAsc = ta.getDrawable(R.styleable.ZwFilterRefreshView_barImgSortAsc);
        Drawable barImgSortDesc = ta.getDrawable(R.styleable.ZwFilterRefreshView_barImgSortDesc);
        Drawable barImgFilter = ta.getDrawable(R.styleable.ZwFilterRefreshView_barImgFilter);
        String barSortText = ta.getString(R.styleable.ZwFilterRefreshView_barSortText);
        String barFilterText = ta.getString(R.styleable.ZwFilterRefreshView_barFilterText);

        if( barTxtSize > 0 )
        viewBarManager.setBarTxtSize(barTxtSize);
        if( barHeight > 0 )
        viewBarManager.setBarHeight(barHeight);
        viewBarManager.setBarTxtColor(barTxtColor);
        viewBarManager.setBarTxtColorActive(barTxtColorActive);
        if( barImgCom != null && barImgComActive != null )
        viewBarManager.setBarImgCom(barImgCom,barImgComActive);
        if( barImgSortDefault != null && barImgSortAsc != null && barImgSortDesc != null )
        viewBarManager.setBarImgSot(barImgSortDefault,barImgSortAsc,barImgSortDesc);
        if( barImgFilter != null )
        viewBarManager.setBarImgFilter(barImgFilter);

        if( !comm.isEmpty(barSortText) )
            viewBarManager.setSortTxt(barSortText);
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
    public Params getBarCurData()
    {
        Params params = new Params();
        int comPosition = getViewBarManager().getComSpinnerSelected();
        int sort = getViewBarManager().getSortStatus();
        FilterData filterData = getFilterManager().getFilterDatas();
        ArrayList<FilterCheckDataItem> markData = getViewBarManager().getMarkData();
        params.setComIndex(comPosition);
        params.setSort(sort);
        params.setFilterData(filterData);
        params.setMarkData(markData);
        return params;
    }

    public PullToRefreshLayout getRefreshLayout(){
        return refreshLayout;
    }

    public RecyclerView getRecyclerView(){
        return recyclerView;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
