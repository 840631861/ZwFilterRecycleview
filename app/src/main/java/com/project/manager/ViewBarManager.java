package com.project.manager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.R;
import com.project.adapter.BarBtnsRecycleAdapter;
import com.project.adapter.MarkRecycleAdapter;
import com.project.model.ZwFilterCheckDataItem;
import com.project.view.BarCheckText;
import com.project.view.CheckText;
import com.project.view.CustomSpinner;

import java.util.ArrayList;
import java.util.List;

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
    private TextView tv_bar_filter;//按钮文字
    private ImageView img_filter;
    private CustomSpinner customSpinner;

    //标签
    private RecyclerView recy_marks;
    //按钮组
    private RecyclerView recy_bar_btns;

    //按钮组与下拉菜单只能选中一个，
    private ZwFilterCheckDataItem barCheckedItem;//当前选中的按钮


    //字体、高度、颜色
    private int barTxtSize ;//顶部栏按钮字体大小
    private int barHeight ;//顶部栏高度
    private int barTxtColor;//顶部栏按钮字体颜色
    private int barTxtColorActive;//顶部栏文字选中时的颜色
    //图标
    private Drawable barImgCom;//顶部栏综合按钮图标
    private Drawable barImgComActive;//顶部栏综合按钮图标（选中时）
    private Drawable barImgFilter;//顶部栏筛选按钮图标

    //监听
    private IListView.onBarItemSelectedListener onBarItemSelectedListener;
    private IListView.OnMarkItemClickListener onMarkItemClickListener;//标签点击事件监听


    public ViewBarManager(Context context, View view) {
        this.context = context;
        this.view = view;
        initView();
        initDefaultView();
    }

    private void initView()
    {
        ll_top_bar = view.findViewById(R.id.ll_top_bar);
        ll_filter_btn = view.findViewById(R.id.ll_filter_btn);

        recy_marks = view.findViewById(R.id.recy_marks);
        recy_bar_btns = view.findViewById(R.id.recy_bar_btns);

        tv_bar_filter = ll_top_bar.findViewById(R.id.tv_bar_filter);

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
        tv_bar_filter.setTextSize(barTxtSize);
        //设置顶部栏高度
        LinearLayout.LayoutParams topBarParams = (LinearLayout.LayoutParams) ll_top_bar.getLayoutParams();
        topBarParams.height = dip2px(context,barHeight);
        ll_top_bar.setLayoutParams(topBarParams);
        img_filter.setImageDrawable(barImgFilter);
        //设置spinner字体、颜色等
        customSpinner.setTextStyle(barTxtSize,barTxtColor,barTxtColorActive);
        customSpinner.setTriImg(barImgCom,barImgComActive);
    }

    //按钮组全部设为未选中
    private void clearBarBtnsCheck()
    {
        if( mBarBynsData != null )
        {
            for (ZwFilterCheckDataItem item:mBarBynsData) {
                item.setChecked(false);
            }
            setBarBtns(mBarBynsData);
        }
    }
    //下拉菜单全部设为未选中
    private void clearSpinnerCheck()
    {
        customSpinner.setSelectedIndex(0);
        customSpinner.changeTxtColor(false);
    }

    public ZwFilterCheckDataItem getBarCheckedItemData()
    {
        return barCheckedItem;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////下拉菜单///////////////////////////
    private List<ZwFilterCheckDataItem> mSpinnerData;
    //设置综合下拉菜单数据
    public ViewBarManager setComSpinnerData(List<ZwFilterCheckDataItem> data )
    {
        mSpinnerData = data;
        if( data != null || data.size() > 0 )
            customSpinner.attachDataSource(data);

        customSpinner.setOnItemSelectedListener(new CustomSpinner.OnItemSelectedListenerSpinner() {
            @Override
            public void onItemSelected(int index)
            {
                clearBarBtnsCheck();
                barCheckedItem = mSpinnerData.get(index);
                customSpinner.changeTxtColor(true);

                if( onBarItemSelectedListener != null )
                    onBarItemSelectedListener.onBarItemSelected(barCheckedItem);
            }
        });

        return this;
    }

    public ViewBarManager setComSpinnerSelectedIndex(int index )
    {
        customSpinner.setSelectedIndex(index);
        customSpinner.changeTxtColor(true);
        barCheckedItem = mSpinnerData.get(index);
        return this;
    }

    public int getComSpinnerSelected()
    {
        return customSpinner.getSelectedPosition();
    }
    ////////////////////////////////////////////////////////////////按钮组//////////////////////////////////////////////////////////////
    private ArrayList<ZwFilterCheckDataItem> mBarBynsData;

    public ViewBarManager setBarBtns(final ArrayList<ZwFilterCheckDataItem> btnData)
    {
        mBarBynsData = btnData;
        recy_bar_btns.setHasFixedSize(true);//设置固定大小
        recy_bar_btns.setItemAnimator(new DefaultItemAnimator());//设置默认动画
        LinearLayoutManager mLayoutManage=new LinearLayoutManager(context);
        mLayoutManage.setOrientation(OrientationHelper.HORIZONTAL);//设置滚动方向，横向滚动
        recy_bar_btns.setLayoutManager(mLayoutManage);
        BarBtnsRecycleAdapter adapter=new  BarBtnsRecycleAdapter(context,R.layout.item_bar_btn,btnData,barTxtSize,barTxtColor,barTxtColorActive,barHeight);
        recy_bar_btns.setAdapter(adapter);

        adapter.setOnItemClickListener(new BarBtnsRecycleAdapter.OnRecycleViewItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                BarCheckText checkText = view.findViewById(R.id.tv_text);
                if( !checkText.isChecked() ){

                    clearSpinnerCheck();
                    clearBarBtnsCheck();

                    checkText.setChecked(true);
                    btnData.get(position).setChecked(true);
                    barCheckedItem = btnData.get(position);

                    if( onBarItemSelectedListener != null )
                        onBarItemSelectedListener.onBarItemSelected(barCheckedItem);
                }
            }
        });
        return this;
    }


    /////////////////////////////////////////////////////////////////////////////////////////标签相关/////////////////////////////////////////

    private ArrayList<ZwFilterCheckDataItem> mMarkData;
    public ViewBarManager setMarkData(final ArrayList<ZwFilterCheckDataItem> markData)
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
    public ArrayList<ZwFilterCheckDataItem> getMarkData()
    {
        return mMarkData;
    }

    /////////////////////////////////////////////////////////设置顶部栏样式////////////////////////////////////////////////////////////////
    //设置顶部栏按钮字体大小
    public ViewBarManager setBarTxtSize(int txtSizePx ){this.barTxtSize = txtSizePx;return this;}

    //设置顶部栏文字
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

    //设置顶部栏筛选图标按钮
    public ViewBarManager setBarImgFilter(Drawable barImgFilter ){this.barImgFilter = barImgFilter;return this;}


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
    public ViewBarManager hideBar(Boolean isHideComBtn, Boolean isHideFilterBtn)
    {
        if( isHideComBtn ) customSpinner.setVisibility(View.GONE); else customSpinner.setVisibility(View.VISIBLE);
        if( isHideFilterBtn ) ll_filter_btn.setVisibility(View.GONE); else ll_filter_btn.setVisibility(View.VISIBLE);

        return this;
    }

    //////////////////////////////////////////////////////监听///////////////////////////////////////////////////////////////////////////////


    //设置综合下拉菜单选中、按钮点击监听
    public ViewBarManager setOnBarItemSelectedListener(IListView.onBarItemSelectedListener onBarItemSelectedListener)
    {
        this.onBarItemSelectedListener = onBarItemSelectedListener;
        return this;
    }

    //设置标签点击事件监听
    public ViewBarManager setOnMarkItemClickListener( IListView.OnMarkItemClickListener onMarkItemClickListener )
    {
        this.onMarkItemClickListener = onMarkItemClickListener;
        return this;
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
