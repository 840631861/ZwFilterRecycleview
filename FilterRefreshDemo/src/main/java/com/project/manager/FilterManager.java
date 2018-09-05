package com.project.manager;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;

import com.jzxiang.pickerview.data.Type;
import com.library.R;
import com.project.model.ZwFilterCheckData;
import com.project.model.ZwFilterCheckDataItem;
import com.project.model.ZwFilterData;
import com.project.seekbar.RangeSeekBar;
import com.project.view.FiltersDialog;

import java.util.List;

/**
 * 筛选菜单管理器
 * Created by Administrator on 2018/6/28.
 */

public class FilterManager implements View.OnClickListener {
    private Context context;
    private View view;

    private LinearLayout ll_top_bar;
    private LinearLayout ll_filter_btn;//筛选按钮
    private FiltersDialog filtersDialog;//侧拉弹窗
    private IListView.OnFilterConfirmClickListener onFilterConfirmClickListener;//确认按钮点击事件
    private IListView.OnFilterItemChangeListener onFilterItemChangeListener;//item状态改变事件
    private IListView.OnFilterSeekbarChangeListener onFilterSeekbarChangeListener;//筛选弹窗中seekbar改变监听
    private IListView.OnFilterTimeChangeListener onFilterTimeChangeListener;//筛选弹窗中时间改变监听
    private IListView.OnFilterSearchChangeListener onFilterSearchChangeListener;//筛选弹窗中搜索框监听
    private IListView.OnFilterResetClickListener onFilterResetClickListener;//重置监听
    private IListView.OnAddCustemViewCallback onAddCustemViewCallback;//添加自定义布局回调
    private IListView.OnPopShowListener onPopShowListener;//pop弹出监听
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
        ll_top_bar = view.findViewById(R.id.ll_top_bar);
        ll_filter_btn = view.findViewById(R.id.ll_filter_btn);
        ll_filter_btn.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_filter_btn) {
            if (filtersDialog == null)
                filtersDialog = new FiltersDialog(context);
            filtersDialog.showAsDropDown(ll_top_bar);

        }
    }

    public FilterManager addCheckData(ZwFilterCheckData data)
    {
        if( filtersDialog == null )
            filtersDialog = new FiltersDialog(context);
        filtersDialog.addData(data);
        return this;
    }

    public FilterManager addCheckDatas(List<ZwFilterCheckData> datas)
    {
        if( filtersDialog == null )
            filtersDialog = new FiltersDialog(context);
        if( datas == null || datas.size() == 0 )
            return this;
        for( int i=0;i<datas.size();i++ )
            filtersDialog.addData(datas.get(i));
        return this;
    }

    public ZwFilterData getFilterDatas()
    {
        return filtersDialog.getAllData();
    }

    private void initEvent()
    {
        if( filtersDialog == null )
            filtersDialog = new FiltersDialog(context);

        filtersDialog.setOnConfirmClickListener(new FiltersDialog.OnConfirmClickListener() {
            @Override
            public void onConfirmClick(ZwFilterData data) {
                if( onFilterConfirmClickListener != null )
                onFilterConfirmClickListener.onFilterConfirmClick(data);
            }
        });

        filtersDialog.setOnItemChangeListener(new FiltersDialog.OnItemChangeListener() {
            @Override
            public void onItemChangeListener(ZwFilterData data,ZwFilterCheckDataItem item,String parentId) {
                if( onFilterItemChangeListener != null )
                onFilterItemChangeListener.onFilterItemChange(data,item,parentId);
            }
        });
        filtersDialog.setOnSeekbarChangeListener(new FiltersDialog.OnSeekbarChangeListener() {
            @Override
            public void onSeekbarChangeListener(ZwFilterData data, float left, float right) {
                if( onFilterSeekbarChangeListener != null )
                    onFilterSeekbarChangeListener.onFilterSeekbarChange(data,left,right);
            }
        });
        filtersDialog.setOnTimeChangeListener(new FiltersDialog.OnTimeChangeListener() {
            @Override
            public void onTimeChangeListener(ZwFilterData data, long time) {
                if( onFilterTimeChangeListener != null )
                    onFilterTimeChangeListener.onFilterTimeChange(data,time);
            }
        });
        filtersDialog.setOnSearchChangeListener(new FiltersDialog.OnSearchChangeListener() {
            @Override
            public void onSearchChangeListener(ZwFilterData data, String keywords) {
                if( onFilterSearchChangeListener != null )
                    onFilterSearchChangeListener.onFilterSearchChange(data,keywords);
            }
        });
        filtersDialog.setOnResetClickListener(new FiltersDialog.OnResetClickListener() {
            @Override
            public void onResetClick(ZwFilterData data) {
                if( onFilterResetClickListener != null )
                onFilterResetClickListener.onFilterResetClick(data);
            }
        });
        filtersDialog.setOnPopShowListener(new FiltersDialog.OnPopShowListener() {
            @Override
            public void onPopShow() {
                if( onPopShowListener != null )
                    onPopShowListener.onPopShow();
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
    //seekbar状态改变事件
    public FilterManager setOnFilterSeekbarChangeListener(IListView.OnFilterSeekbarChangeListener onFilterSeekbarChangeListener)
    {
        this.onFilterSeekbarChangeListener = onFilterSeekbarChangeListener;
        return this;
    }
    //time状态改变事件
    public FilterManager setOnFilterTimeChangeListener(IListView.OnFilterTimeChangeListener onFilterTimeChangeListener)
    {
        this.onFilterTimeChangeListener = onFilterTimeChangeListener;
        return this;
    }
    //搜索框状态改变事件
    public FilterManager setOnFilterSearchChangeListener(IListView.OnFilterSearchChangeListener onFilterSearchChangeListener)
    {
        this.onFilterSearchChangeListener = onFilterSearchChangeListener;
        return this;
    }
    //重置按钮事件
    public FilterManager setOnFilterResetClickListener(IListView.OnFilterResetClickListener onFilterResetClickListener)
    {
        this.onFilterResetClickListener = onFilterResetClickListener;
        return this;
    }

    //pop弹出监听
    public FilterManager setOnPopShowListener(IListView.OnPopShowListener onPopShowListener)
    {
        this.onPopShowListener = onPopShowListener;
        return this;
    }

    //添加时间段条件
    public FilterManager addTimeSection(FragmentManager fragmentManager, Type type)
    {
        filtersDialog.addTimeSection(fragmentManager,type);
        return this;
    }

    //添加时间段条件
    public FilterManager addTimeSection(FragmentManager fragmentManager, Type type,int themeColor)
    {
        filtersDialog.setTimeDialogTheme(themeColor);
        filtersDialog.addTimeSection(fragmentManager,type);
        return this;
    }

    //添加搜索框
    public FilterManager addSearchTxt()
    {
        filtersDialog.addSearchTxt();
        return this;
    }

    //添加seekbar
    public FilterManager addSeekBar1(String title,int min,int max,int color)
    {
        filtersDialog.setSeekBar1(title,min,max,color);
        return this;
    }
    public FilterManager addSeekBar2(String title,int min,int max,int color)
    {
        filtersDialog.setSeekBar2(title,min,max,color);
        return this;
    }
    //获取seekbar
    public RangeSeekBar getSeekbar1()
    {
        return filtersDialog.getSeekBar1();
    }
    public RangeSeekBar getSeekbar2()
    {
        return filtersDialog.getSeekBar2();
    }

    public FilterManager hideDialog()
    {
        filtersDialog.dismiss();
        return this;
    }

    //添加自定义布局
    public FilterManager addCustomView(View view,final IListView.OnAddCustemViewCallback onAddCustemViewCallback)
    {
        filtersDialog.addCustomView(view, new FiltersDialog.OnAddCustemViewCallback() {
            @Override
            public void onCallback(View parent, View custom) {
                onAddCustemViewCallback.onAddCustemView(parent,custom);
            }
        });
        return this;
    }
}
