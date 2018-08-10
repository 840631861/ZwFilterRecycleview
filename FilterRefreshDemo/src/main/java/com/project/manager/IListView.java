package com.project.manager;

import android.view.View;

import com.project.model.ZwFilterCheckDataItem;
import com.project.model.ZwFilterData;

/**
 * 有关接口.
 * Created by Administrator on 2018/6/23.
 */

public interface IListView
{
    //顶部按钮选中事件
    interface onBarItemSelectedListener{
        void onBarItemSelected(ZwFilterCheckDataItem checkDataItem);
    }
    //筛选弹窗中确认点击事件
    interface OnFilterConfirmClickListener{
        void onFilterConfirmClick(ZwFilterData data);
    }
    //筛选弹窗中重置按钮事件
    interface OnFilterResetClickListener{
        void onFilterResetClick(ZwFilterData data);
    }
    //筛选弹窗中item状态改变或数值变动时监听
    interface OnFilterItemChangeListener{
        void onFilterItemChange(ZwFilterData data);
    }
    //添加自定义布局回调
    interface OnAddCustemViewCallback{
        void onAddCustemView(View parent, View custom);
    }
    //mark标签点击事件
    interface OnMarkItemClickListener{
        void onMarkItemClick(int position);
    }

    //pop弹出事件
    interface OnPopShowListener{
        void onPopShow();
    }
}
