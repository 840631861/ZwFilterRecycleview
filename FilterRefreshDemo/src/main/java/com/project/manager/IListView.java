package com.project.manager;

import android.view.View;

import com.project.model.FilterData;

/**
 * 有关接口.
 * Created by Administrator on 2018/6/23.
 */

public interface IListView
{
    //排序按钮点击事件
    interface OnSortClickListener{
        void onSortClick(int status);
    }
    //综合下拉菜单选中事件
    interface OnComSpinnerSelectedListener{
        void onComSpinnerSelected(int index);
    }
    //筛选弹窗中确认点击事件
    interface OnFilterConfirmClickListener{
        void onFilterConfirmClick(FilterData data);
    }
    //筛选弹窗中重置按钮事件
    interface OnFilterResetClickListener{
        void onFilterResetClick(FilterData data);
    }
    //筛选弹窗中item状态改变或数值变动时监听
    interface OnFilterItemChangeListener{
        void onFilterItemChange(FilterData data);
    }
    //添加自定义布局回调
    interface OnAddCustemViewCallback{
        void onAddCustemView(View parent, View custom);
    }
    //mark标签点击事件
    interface OnMarkItemClickListener{
        void onMarkItemClick(int position);
    }
}
