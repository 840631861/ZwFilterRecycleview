package com.project.manager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.project.R;
import com.project.adapter.MarkRecycleAdapter;
import com.project.model.FilterCheckDataItem;
import com.project.view.CheckText;
import com.project.view.CustomSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/23.
 */

public class ViewBarManager
{
    //上下文
    private Context context;
    //视图
    private View view;
    private LinearLayout ll_top_bar;//顶部操作栏
    private LinearLayout ll_filter_btn;//筛选按钮
    private LinearLayout ll_sort_btn;//排序按钮
    private TextView tv_bar_sort,tv_bar_filter;//按钮文字
    private ImageView img_sort,img_filter;
    private CustomSpinner customSpinner;

    //标签
    private RecyclerView recy_marks;

    //字体、高度、颜色
    private int barTxtSize ;//顶部栏按钮字体大小
    private int barHeight ;//顶部栏高度
    private int barTxtColor;//顶部栏按钮字体颜色
    private int barTxtColorActive;//顶部栏文字选中时的颜色
    //图标
    private Drawable barImgCom;//顶部栏综合按钮图标
    private Drawable barImgComActive;//顶部栏综合按钮图标（选中时）
    private Drawable barImgSortDefault;//顶部栏排序按钮图标
    private Drawable barImgSortAsc;
    private Drawable barImgSortDesc;
    private Drawable barImgFilter;//顶部栏筛选按钮图标

    //监听
    private IListView.OnSortClickListener onSortClickListener;
    private IListView.OnComSpinnerSelectedListener onComSpinnerSelectedListener;
    private IListView.OnMarkItemClickListener onMarkItemClickListener;//标签点击事件监听

    //状态值
    private int sortStatus = SORT_STATUS_DEFAULT;//排序状态

    public static int SORT_STATUS_DEFAULT = 0;
    public static int SORT_STATUS_ASC = 1;
    public static int SORT_STATUS_DESC = -1;

    public ViewBarManager(Context context, View view) {
        this.context = context;
        this.view = view;
        initView();
        initDefaultView();
        initEvent();
    }

    private void initView()
    {
        ll_top_bar = view.findViewById(R.id.ll_top_bar);
        ll_filter_btn = view.findViewById(R.id.ll_filter_btn);
        ll_sort_btn = view.findViewById(R.id.ll_sort_btn);

        recy_marks = view.findViewById(R.id.recy_marks);

        tv_bar_sort = ll_top_bar.findViewById(R.id.tv_bar_sort);
        tv_bar_filter = ll_top_bar.findViewById(R.id.tv_bar_filter);

        img_sort = ll_top_bar.findViewById(R.id.img_sort);
        img_filter = ll_top_bar.findViewById(R.id.img_filter);

        customSpinner = ll_top_bar.findViewById(R.id.customSpinner);
    }

    //设置默认样式
    private void initDefaultView()
    {
        barTxtColorActive = context.getResources().getColor(R.color.colorAccent);

        barTxtColor = context.getResources().getColor(R.color.gray_8);
        barImgCom = context.getResources().getDrawable(R.mipmap.ic_tri_down);
        barImgComActive = context.getResources().getDrawable(R.mipmap.ic_tri_up);
        barImgSortDefault = context.getResources().getDrawable(R.mipmap.ic_sort);
        barImgSortAsc = context.getResources().getDrawable(R.mipmap.ic_sort_asc);
        barImgSortDesc = context.getResources().getDrawable(R.mipmap.ic_sort_desc);
        barImgFilter = context.getResources().getDrawable(R.mipmap.ic_filter);

        if( barTxtSize == 0 )
            barTxtSize = 15;
        if( barHeight == 0 )
            barHeight = 35;

        customSpinner.setTextStyle(barTxtSize,barTxtColor,barTxtColorActive);
        customSpinner.setTriImg(barImgCom,barImgComActive);

    }

    /**
     * 更新顶部栏样式
     */
    public void updateBarView()
    {
        //设置顶部栏字体
        tv_bar_sort.setTextSize(barTxtSize);
        tv_bar_filter.setTextSize(barTxtSize);
        //设置顶部栏高度
        LinearLayout.LayoutParams topBarParams = (LinearLayout.LayoutParams) ll_top_bar.getLayoutParams();
        topBarParams.height = dip2px(context,barHeight);
        ll_top_bar.setLayoutParams(topBarParams);
        img_filter.setImageDrawable(barImgFilter);
        //根据排序状态改变排序图片样式、字体颜色
        changeSortViewByStatus();
        //设置spinner字体、颜色等
        customSpinner.setTextStyle(barTxtSize,barTxtColor,barTxtColorActive);
        customSpinner.setTriImg(barImgCom,barImgComActive);
    }

    //设置初始事件
    private void initEvent()
    {
        //排序点击事件
        ll_sort_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( sortStatus == SORT_STATUS_DEFAULT )
                    sortStatus = SORT_STATUS_ASC;
                else if( sortStatus == SORT_STATUS_ASC )
                    sortStatus = SORT_STATUS_DESC;
                else if( sortStatus == SORT_STATUS_DESC )
                    sortStatus = SORT_STATUS_DEFAULT;

                changeSortViewByStatus();
                if( onSortClickListener != null )
                onSortClickListener.onSortClick(sortStatus);
            }
        });
        //综合下拉菜单
        customSpinner.setOnItemSelectedListener(new CustomSpinner.OnItemSelectedListenerSpinner() {
            @Override
            public void onItemSelected(int index) {
                if( onComSpinnerSelectedListener != null )
                onComSpinnerSelectedListener.onComSpinnerSelected(index);
            }
        });
    }

    public int getComSpinnerSelected()
    {
        return customSpinner.getSelectedPosition();
    }

    ////////////////////////////////////////////////////////////////排序相关//////////////////////////////////////////////////////////////
    //根据排序状态改变图片样式、字体颜色
    private void changeSortViewByStatus()
    {
        if( sortStatus == SORT_STATUS_DEFAULT )
        {
            img_sort.setImageDrawable(barImgSortDefault);
            tv_bar_sort.setTextColor(barTxtColor);
        }else if( sortStatus == SORT_STATUS_ASC )
        {
            img_sort.setImageDrawable(barImgSortAsc);
            tv_bar_sort.setTextColor(barTxtColorActive);
        }else if( sortStatus == SORT_STATUS_DESC )
        {
            img_sort.setImageDrawable(barImgSortDesc);
            tv_bar_sort.setTextColor(barTxtColorActive);
        }
    }

    //设置排序状态
    public int getSortStatus() {
        return sortStatus;
    }
    public void setSortStatus(int sortStatus) {
        this.sortStatus = sortStatus;
    }

    /////////////////////////////////////////////////////////设置顶部栏样式////////////////////////////////////////////////////////////////
    //设置顶部栏按钮字体大小
    public ViewBarManager setBarTxtSize(int txtSizePx ){this.barTxtSize = txtSizePx;return this;}

    //设置顶部栏文字
    public ViewBarManager setSortTxt(String txt ){tv_bar_sort.setText(txt);return this;}
    public ViewBarManager setFilterTxt(String txt ){tv_bar_filter.setText(txt);return this;}

    //设置顶部栏高度
    public ViewBarManager setBarHeight(int barHeight ){this.barHeight = barHeight;return this;}

    //设置顶部栏字体颜色
    public ViewBarManager setBarTxtColor(int color ){this.barTxtColor = color;return this;}
    public ViewBarManager setBarTxtColorActive(int color ){this.barTxtColorActive = color;return this;}

    //设置顶部栏综合按钮图标
    public ViewBarManager setBarImgCom(Drawable imgCom, Drawable imgComActive ){
        this.barImgCom = imgCom;
        this.barImgComActive = imgComActive;
        return this;}

    //设置排序按钮图标
    public ViewBarManager setBarImgSot(Drawable barImgSortDefault, Drawable barImgSortAsc, Drawable barImgSortDesc ){
        this.barImgSortDefault = barImgSortDefault;
        this.barImgSortAsc = barImgSortAsc;
        this.barImgSortDesc = barImgSortDesc;
        return this;
    }

    //设置顶部栏筛选图标按钮
    public ViewBarManager setBarImgFilter(Drawable barImgFilter ){this.barImgFilter = barImgFilter;return this;}

    //设置综合下拉菜单数据
    public ViewBarManager setComSpinnerData(List<String> data )
    {
        if( data != null || data.size() > 0 )
            customSpinner.attachDataSource(data);
        return this;
    }

    public ViewBarManager setComSpinnerSelectedIndex(int index )
    {
        customSpinner.setSelectedIndex(index);
        return this;
    }

    /////////////////////////////////////////////////////////////显示、隐藏////////////////////////////////////////////////////////////////////////

    //隐藏、显示顶部栏
    public ViewBarManager hideBar(Boolean isHide)
    {
        if( isHide )
            ll_top_bar.setVisibility(View.GONE);
        else
            ll_top_bar.setVisibility(View.VISIBLE);
        return this;
    }
    //隐藏、显示顶部栏中的综合按钮、排序按钮、筛选按钮
    public ViewBarManager hideBar(Boolean isHideComBtn, Boolean isHideSortBtn, Boolean isHideFilterBtn)
    {
        if( isHideComBtn ) customSpinner.setVisibility(View.GONE); else customSpinner.setVisibility(View.VISIBLE);
        if( isHideSortBtn ) ll_sort_btn.setVisibility(View.GONE); else ll_sort_btn.setVisibility(View.VISIBLE);
        if( isHideFilterBtn ) ll_filter_btn.setVisibility(View.GONE); else ll_filter_btn.setVisibility(View.VISIBLE);

        return this;
    }

    //////////////////////////////////////////////////////监听///////////////////////////////////////////////////////////////////////////////

    //设置排序按钮点击监听
    public ViewBarManager setOnSortClickListener(IListView.OnSortClickListener onSortClickListener)
    {
        this.onSortClickListener = onSortClickListener;
        return this;
    }

    //设置综合下拉菜单选中监听
    public ViewBarManager setOnComSpinnerSelectedListenner(IListView.OnComSpinnerSelectedListener onComSpinnerSelectedListener)
    {
        this.onComSpinnerSelectedListener = onComSpinnerSelectedListener;
        return this;
    }

    //设置标签点击事件监听
    public ViewBarManager setOnMarkItemClickListener( IListView.OnMarkItemClickListener onMarkItemClickListener )
    {
        this.onMarkItemClickListener = onMarkItemClickListener;
        return this;
    }

    /////////////////////////////////////////////////////////////////////////////////////////标签相关/////////////////////////////////////////

    private ArrayList<FilterCheckDataItem> mMarkData;
    public ViewBarManager setMarkData(final ArrayList<FilterCheckDataItem> markData)
    {
        mMarkData = markData;
        recy_marks.setVisibility(View.VISIBLE);

        recy_marks.setHasFixedSize(true);//设置固定大小
        recy_marks.setItemAnimator(new DefaultItemAnimator());//设置默认动画
        LinearLayoutManager mLayoutManage=new LinearLayoutManager(context);
        mLayoutManage.setOrientation(OrientationHelper.HORIZONTAL);//设置滚动方向，横向滚动
        recy_marks.setLayoutManager(mLayoutManage);
        MarkRecycleAdapter adapter=new  MarkRecycleAdapter(context,R.layout.item_mark,markData);
        recy_marks.setAdapter(adapter);

        adapter.setOnItemClickListener(new MarkRecycleAdapter.OnRecycleViewItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                CheckText checkText = view.findViewById(R.id.tv_text);
                if( checkText.isChecked() ){
                    checkText.setChecked(false);
                    markData.get(position).setChecked(false);
                }else {
                    checkText.setChecked(true);
                    markData.get(position).setChecked(true);
                }
                if( onMarkItemClickListener != null )
                    onMarkItemClickListener.onMarkItemClick(position);
            }
        });
        return this;
    }

    //获取标签数据
    public ArrayList<FilterCheckDataItem> getMarkData()
    {
        return mMarkData;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////其他//////////////////////////////////////
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = getScale(context);
        return (int) (dpValue * scale + 0.5f);
    }
    private static float getScale(Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return findScale(fontScale);
    }
    private static float findScale(float scale){
        if(scale<=1){
            scale=1;
        }else if(scale<=1.5){
            scale=1.5f;
        }else if(scale<=2){
            scale=2f;
        }else if(scale<=3){
            scale=3f;
        }
        return scale;
    }


}
