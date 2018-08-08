package com.project.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.library.R;
import com.project.commom.comm;
import com.project.manager.FilterManager;
import com.project.model.FilterCheckData;
import com.project.model.FilterCheckDataItem;
import com.project.model.FilterData;
import com.project.seekbar.OnRangeChangedListener;
import com.project.seekbar.RangeSeekBar;
import com.project.utils.ListSortUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 筛选条件侧拉栏dialog
 * Created by Administrator on 2018/6/29.
 */

public class FiltersDialog extends PopupWindow implements View.OnClickListener {
    private Context context;
    private View view;
    private View trans_view,ll_content;
    private FilterData mData;
    private List<FilterCheckData> checkDatas;

    private LayoutInflater inflater;
    private LinearLayout ll_filter_content;//包含checktext
    private TextView tv_confirm,tv_reset;
    private OnConfirmClickListener onConfirmClickListener;//确认按钮点击事件
    private OnResetClickListener onResetClickListener;//重置按钮事件监听
    private OnItemChangeListener onItemChangeListener;//item改变事件监听


    //时间段
    private LinearLayout ll_time_content;
    private TextView tv_time_start,tv_time_end;
    private TimePickerDialog mTimeDialog;
    private TextView CurTimePicker;
    private FragmentManager fragmentManager;
    private Type TimeType = Type.ALL;
    private int TimeColorId = R.color.colorAccent;
    //搜索关键字
    private LinearLayout ll_search;
    private EditText et_search;
    //自定义
    private LinearLayout ll_custom;
    //SeekBar
    private LinearLayout ll_seekbar1,ll_seekbar2;
    private TextView tv_seekbar_title1,tv_seekbar_title2;
    private RangeSeekBar seekBar1,seekBar2;


    public FiltersDialog(Context context) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.dlg_filters, null);
        trans_view = (View) view.findViewById(R.id.trans_view);
        ll_content = view.findViewById(R.id.ll_content);

        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.topShow);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                int height = view.getTop();
                int bottom = view.findViewById(R.id.ll_bottom).getBottom();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height || y>bottom){
                        dismiss();
                    }
                }
                return true;
            }
        });


        mData = new FilterData();
        List<FilterCheckData> checks = new ArrayList<>();
        mData.setCheckDatas(checks);
        checkDatas = mData.getCheckDatas();

        //初始化界面控件
        initView();
    }

    @Override
    public void dismiss() {

        Animation showAnim =  AnimationUtils.loadAnimation(context, R.anim.bg_alpha_hide);
        showAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Animation topAnim = AnimationUtils.loadAnimation(context, R.anim.pop_top_hide);
                topAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        FiltersDialog.super.dismiss();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                ll_content.startAnimation(topAnim);
                trans_view.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationEnd(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        trans_view.startAnimation(showAnim);

    }

    @Override
    public void showAsDropDown(View parent) {
        trans_view.setVisibility(View.VISIBLE);
        trans_view.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.bg_alpha_show));
        ll_content.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.pop_top_show));
        super.showAsDropDown(parent);
    }

    /**
     * 初始化界面控件
     */
    private void initView()
    {
        ll_filter_content = view.findViewById(R.id.ll_filter_content);
        tv_confirm = view.findViewById(R.id.tv_confirm);
        tv_reset = view.findViewById(R.id.tv_reset);

        ll_time_content = view.findViewById(R.id.ll_time_content);
        tv_time_start = view.findViewById(R.id.tv_time_start);
        tv_time_end = view.findViewById(R.id.tv_time_end);
        tv_time_start.setOnClickListener(this);
        tv_time_end.setOnClickListener(this);

        ll_search = view.findViewById(R.id.ll_search);
        et_search = view.findViewById(R.id.et_search);
        et_search.addTextChangedListener(keywordsChangeListener);

        ll_custom = view.findViewById(R.id.ll_custom);

        tv_confirm.setOnClickListener(this);
        tv_reset.setOnClickListener(this);

        ll_seekbar1 = view.findViewById(R.id.ll_seekbar1);
        ll_seekbar2 = view.findViewById(R.id.ll_seekbar2);
        tv_seekbar_title1 = view.findViewById(R.id.tv_seekbar_title1);
        tv_seekbar_title2 = view.findViewById(R.id.tv_seekbar_title2);
        seekBar1 = view.findViewById(R.id.seekBar1);
        seekBar2 = view.findViewById(R.id.seekBar2);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_confirm) {
            if (onConfirmClickListener != null)
                onConfirmClickListener.onConfirmClick(mData);

        } else if (i == R.id.tv_reset) {
            resetAllItems();
            if (onResetClickListener != null)
                onResetClickListener.onResetClick(mData);

        } else if (i == R.id.tv_time_start) {
            CurTimePicker = (TextView) v;
            mTimeDialog.show(fragmentManager, "hour_minute");

        } else if (i == R.id.tv_time_end) {
            CurTimePicker = (TextView) v;
            mTimeDialog.show(fragmentManager, "hour_minute");

        }
    }

    ////////////////////////////////////////////////搜索关键字///////////////////////////////////////

    public FiltersDialog addSearchTxt()
    {
        ll_search.setVisibility(View.VISIBLE);
        return this;
    }

    //关键字、标题输入框文本改变监听
    TextWatcher keywordsChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String keywords = et_search.getText().toString().trim();
            mData.setSearchTxt(keywords);
            onItemChangeListener.onItemChangeListener(mData);
        }
    };

    ///////////////////////////////////////////////////时间段相关///////////////////////////////////////
    //设置时间选择器的主题颜色
    public FiltersDialog setTimeDialogTheme(int colorId)
    {
        this.TimeColorId = colorId;
        return this;
    }

    //添加时间段条件
    public FiltersDialog addTimeSection(FragmentManager fragmentManager,Type type)
    {
        this.fragmentManager = fragmentManager;
        this.TimeType = type;

        ll_time_content.setVisibility(View.VISIBLE);
        mTimeDialog = new TimePickerDialog.Builder()
                .setType(type)
                .setCallBack(dateSetListener)
                .setCyclic(false)
                .setTitleStringId("")
                .setThemeColor(context.getResources().getColor(TimeColorId))
                .build();
        return this;
    }

    //时间选择器回调
    OnDateSetListener dateSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
            String text = getDateToString(millseconds);
            int i = CurTimePicker.getId();
            if (i == R.id.tv_time_start) {
                mData.setTimeStart(millseconds);

            } else if (i == R.id.tv_time_end) {
                mData.setTimeEnd(millseconds);

            }
            CurTimePicker.setText(text);
            onItemChangeListener.onItemChangeListener(mData);
        }
    };

    private String getDateToString(long time)
    {
        String pattern = "";
        switch (TimeType)
        {
            case ALL:
                pattern = "yyyy-MM-dd HH:mm:ss";
                break;
            case YEAR_MONTH_DAY:
                pattern = "yyyy-MM-dd";
                break;
            case HOURS_MINS:
                pattern = "HH:mm";
                break;
            case MONTH_DAY_HOUR_MIN:
                pattern = "MM-dd HH:mm";
                break;
            case YEAR_MONTH:
                pattern = "yyyy-MM";
                break;
            case YEAR:
                pattern = "yyyy";
                break;
        }

        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        Date d = new Date(time);
        return sf.format(d);
    }

    ////////////////////////////////////////////////////////seekbar///////////////////////////////////////
    public FiltersDialog setSeekBar1(String title,int min,int max,int color)
    {
        ll_seekbar1.setVisibility(View.VISIBLE);
        tv_seekbar_title1.setText(title);
        if( color > 0 )
        {
            seekBar1.setProgressColor(color);
        }
        seekBar1.setRange(min,max);
        seekBar1.setIndicatorTextDecimalFormat("0");
        seekBar1.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                mData.setSeekbarNum1Start(leftValue);
                mData.setSeekbarNum1End(rightValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                onItemChangeListener.onItemChangeListener(mData);
            }
        });
        return this;
    }
    public FiltersDialog setSeekBar2(String title,int min,int max,int color)
    {
        ll_seekbar2.setVisibility(View.VISIBLE);
        tv_seekbar_title2.setText(title);
        if( color > 0 )
        {
            seekBar2.setProgressColor(color);
        }
        seekBar2.setRange(min,max);
        seekBar2.setIndicatorTextDecimalFormat("0");
        seekBar2.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                mData.setSeekbarNum2Start(leftValue);
                mData.setSeekbarNum2End(rightValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                onItemChangeListener.onItemChangeListener(mData);
            }
        });
        return this;
    }


    /////////////////////////////////////////////////////事件///////////////////////////////////////////
    //确认监听事件
    public interface OnConfirmClickListener
    {
        void onConfirmClick(FilterData data);
    }

    public void setOnConfirmClickListener(OnConfirmClickListener onConfirmClickListener){
        this.onConfirmClickListener = onConfirmClickListener;
    }

    //重置监听事件
    public interface OnResetClickListener
    {
        void onResetClick(FilterData data);
    }

    public void setOnResetClickListener(OnResetClickListener onResetClickListener){
        this.onResetClickListener = onResetClickListener;
    }

    //item改变事件（选中状态改变、时间变化）
    public interface OnItemChangeListener{
        void onItemChangeListener(FilterData data);
    }
    public void setOnItemChangeListener(OnItemChangeListener onItemChangeListener){
        this.onItemChangeListener = onItemChangeListener;
    }

    //添加自定义布局
    public FiltersDialog addCustomView(View view,OnAddCustemViewCallback callback)
    {
        ll_custom.addView(view);
        callback.onCallback(ll_custom,view);
        return this;
    }
    //添加自定义布局回调
    public interface OnAddCustemViewCallback{
        void onCallback(View parent, View custom);
    }

    ////////////////////////////////////////////////////////数据相关/////////////////////////////////////////
    //添加数据
    public void addData(FilterCheckData data)
    {
        if( checkDatas == null )
        {
            checkDatas = new ArrayList<>();
            mData.setCheckDatas(checkDatas);
        }
        checkDatas.add(data);
        sortData();
        renderData();
    }

    public FilterData getAllData()
    {
        return mData;
    }

    //对数据排序
    private void sortData()
    {
        if( checkDatas == null || checkDatas.size() == 0)
            return;
        ListSortUtil.sort(checkDatas,true, FilterCheckData.sortName);
    }

    //渲染数据
    private void renderData()
    {
        if( checkDatas== null || checkDatas.size() == 0 )
            return;
        ll_filter_content.removeAllViews();
        for( int i=0;i<checkDatas.size();i++ )
        {
            final FilterCheckData data = checkDatas.get(i);
            String title = data.getTitle();
            final List<FilterCheckDataItem> checkDatas = data.getList();

            View view = inflater.inflate(R.layout.item_filter,null);
            TextView tvTitle = view.findViewById(R.id.tv_title);
            MyGridView gridView = view.findViewById(R.id.gridview);

            tvTitle.setText(comm.isEmpty(title)?"":title);
            FilterSearchAdapter adapter = new FilterSearchAdapter(context,checkDatas);
            gridView.setAdapter(adapter);

            ll_filter_content.addView(view);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CheckText checkText = view.findViewById(R.id.txt);
                    Boolean isCheck = checkText.isChecked();
                    FilterCheckDataItem item = checkDatas.get(position);
                    int type = data.getType();
                    //单选-则清楚所有选中
                    if( type == FilterManager.FILTER_TYPE_SIN_ELECTION )
                    {
                        unCheckAllItems(parent,checkDatas);
                    }
                    //多选 不作处理
                    else if( type == FilterManager.FILTER_TYPE_MUL_ELECTION )
                    {

                    }

                    checkText.setChecked(!isCheck);
                    Boolean isChecked = checkText.isChecked();
                    item.setChecked(isChecked);
                    if( onItemChangeListener != null )
                        onItemChangeListener.onItemChangeListener(mData);
                }
            });
        }
    }

    //清除所有check item的选中
    private void unCheckAllItems(AdapterView<?> parent,List<FilterCheckDataItem> checkDatas)
    {
        if( checkDatas == null || checkDatas.size() == 0 )
            return;
        for( int i=0;i<checkDatas.size();i++ )
        {
            View view = parent.getChildAt(i);
            CheckText checkText = view.findViewById(R.id.txt);
            checkText.setChecked(false);
            checkDatas.get(i).setChecked(false);
        }
    }

    //重置
    private void resetAllItems()
    {
        int viewNum = ll_filter_content.getChildCount();
        for( int i=0;i<viewNum;i++ )
        {
            View view = ll_filter_content.getChildAt(i);
            MyGridView gridView = view.findViewById(R.id.gridview);
            for( int j=0;j<gridView.getChildCount();j++)
            {
                View item = gridView.getChildAt(j);
                CheckText checkText = item.findViewById(R.id.txt);
                checkText.setChecked(false);
                checkDatas.get(i).getList().get(j).setChecked(false);
            }
        }
        tv_time_start.setText("");
        tv_time_end.setText("");
        et_search.setText("");
    }

    //适配器
    public class FilterSearchAdapter extends BaseAdapter
    {
        private List<FilterCheckDataItem> ls;
        private LayoutInflater inflater;

        public FilterSearchAdapter(Context context, List<FilterCheckDataItem> ls) {
            this.ls = ls;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return ls.size();
        }

        @Override
        public Object getItem(int position) {
            return ls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            Holder mHolder = null;
            final FilterCheckDataItem obj = ls.get(position);
            if( convertView == null )
            {
                mHolder = new Holder();
                convertView = inflater.inflate(R.layout.item_filter_child,null);
                mHolder.tvTitle = convertView.findViewById(R.id.txt);

                convertView.setTag(mHolder);
            }else{
                mHolder = (Holder) convertView.getTag();
            }
            mHolder.tvTitle.setText(obj.getShowName());
            if( obj.getChecked() )
                mHolder.tvTitle.setChecked(true);

            return convertView;
        }

        private class Holder
        {
            CheckText tvTitle;
        }
    }


}
