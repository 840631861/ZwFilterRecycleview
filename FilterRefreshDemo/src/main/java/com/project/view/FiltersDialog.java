package com.project.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.library.R;
import com.project.commom.comm;
import com.project.manager.FilterManager;
import com.project.model.FilterCheckData;
import com.project.model.FilterCheckDataItem;
import com.project.model.FilterData;
import com.project.utils.ListSortUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 筛选条件侧拉栏dialog
 * Created by Administrator on 2018/6/29.
 */

public class FiltersDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private View view;
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

    public FiltersDialog(Context context) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.dlg_filters, null);

        mData = new FilterData();
        List<FilterCheckData> checks = new ArrayList<>();
        mData.setCheckDatas(checks);
        checkDatas = mData.getCheckDatas();

        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);
        //初始化界面控件
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_confirm:
                if( onConfirmClickListener != null )
                    onConfirmClickListener.onConfirmClick(mData);
                break;
            case R.id.tv_reset:
                resetAllItems();
                if( onResetClickListener != null )
                    onResetClickListener.onResetClick(mData);
                break;
            case R.id.tv_time_start:
                CurTimePicker = (TextView) v;
                mTimeDialog.show(fragmentManager,"hour_minute");
                break;
            case R.id.tv_time_end:
                CurTimePicker = (TextView) v;
                mTimeDialog.show(fragmentManager,"hour_minute");
                break;
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
            switch (CurTimePicker.getId())
            {
                case R.id.tv_time_start:
                    mData.setTimeStart(millseconds);
                    break;
                case R.id.tv_time_end:
                    mData.setTimeEnd(millseconds);
                    break;
            }
            CurTimePicker.setText(text);
            onItemChangeListener.onItemChangeListener(mData);
        }
    };

    public String getDateToString(long time)
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //显示、隐藏动画
    @Override
    protected void onStart() {
        super.onStart();
        view.measure(0,0);
        int viewH = view.getMeasuredHeight();
        int viewW = view.getMeasuredWidth();

        Window window = getWindow();
        window.setWindowAnimations(R.style.rightShow);
        WindowManager.LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.RIGHT);
        //设置发生动画中的背景
        windowparams.width = viewW;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(windowparams);
    }


    ////手指控制dialog滑动隐藏
    float startX;
    float moveX = 0;
    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = ev.getX() - startX;
                view.scrollBy(-(int) moveX, 0);
                startX = ev.getX();
                if (view.getScrollX() > 0) {
                    view.scrollTo(0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (view.getScrollX() < -this.getWindow().getAttributes().width / 4 && moveX > 0) {
                    this.dismiss();

                }
                view.scrollTo(0, 0);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
