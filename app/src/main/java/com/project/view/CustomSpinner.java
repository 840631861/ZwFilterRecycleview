package com.project.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.project.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/23.
 */

public class CustomSpinner extends LinearLayout {

    private View view;
    private TextView tv_name;
    private ImageView ib;

    //界面控件
    private ImageView spinner;
    //构造qq号用到的集合
    private List<String> list = new ArrayList<String>();
    //布局加载器
    //自定义适配器
    private MyAdapter mAdapter;
    //PopupWindow
    private PopupWindow pop;
    //是否显示PopupWindow，默认不显示
    private boolean isPopShow = true;
    private ListView listView;
    private LayoutInflater mInflater;
    private OnItemSelectedListenerSpinner onItemSelectedListener;
    private int heiht;
    private int postion = 0;

    private int barTxtSize;//顶部栏按钮字体大小
    private int barTxtColor;//顶部栏按钮字体颜色
    private int barTxtColorActive;//顶部栏文字选中时的颜色
    private Drawable barImgCom;//顶部栏综合按钮图标
    private Drawable barImgComActive;//顶部栏综合按钮图标（选中时）

    public CustomSpinner(Context context) {
        super(context);
        initView(context);
    }

    public CustomSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CustomSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }
    //设置文本大小、颜色
    public void setTextStyle( int txtSize,int txtColor,int txtColorActive )
    {
        this.barTxtSize = txtSize;
        this.barTxtColor = txtColor;
        this.barTxtColorActive = txtColorActive;

        tv_name.setTextSize(barTxtSize);
        tv_name.setTextColor(barTxtColor);
    }

    public void setTriImg( Drawable img,Drawable imgActive )
    {
        this.barImgCom = img;
        this.barImgComActive = imgActive;
    }

    //获取选中的索引
    public int getSelectedPosition()
    {
        return postion;
    }

    private void initView(final Context context) {
        mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.layout_customspinner, null);

        mAdapter = new MyAdapter();
        tv_name = (TextView) view.findViewById(R.id.et_name);
        ib = (ImageView) view.findViewById(R.id.spinner);
        tv_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != list){
                    if(pop == null){
                        listView = new ListView(context);
                        listView.setCacheColorHint(0x00000000);
                        listView.setDividerHeight(0);
                        listView.setBackgroundColor(Color.rgb(255,255,255));
                        listView.setAdapter(mAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                postion = i;
                                mAdapter.notifyDataSetChanged();
                                tv_name.setText(list.get(i));
                                if( barImgCom != null )
                                    ib.setImageDrawable(barImgCom);
                                else
                                    ib.setImageResource(R.mipmap.ic_tri_down);
                                pop.dismiss();
                                isPopShow = true;
                                CustomSpinner.this.view.setTag(getId());
                                if( onItemSelectedListener != null )
                                    onItemSelectedListener.onItemSelected(i);
                            }
                        });

                        if (heiht == 0){
                            int hei = setListViewHeightBasedOnChildren(listView);
                            //这里设置下拉框的高度
                            if (hei >= 550){
                                pop = new PopupWindow(listView, CustomSpinner.this.view.getWidth(), 550, true);
                            }else{
                                pop = new PopupWindow(listView, CustomSpinner.this.view.getWidth(), hei, true);
                            }
                        }else{
                            pop = new PopupWindow(listView, CustomSpinner.this.view.getWidth(),heiht, true);
                        }
                        pop.setBackgroundDrawable(new ColorDrawable(0x00000000));
                        pop.setFocusable(true);
                        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                isPopShow = true;
                                if( barImgCom != null )
                                    ib.setImageDrawable(barImgCom);
                                else
                                    ib.setImageResource(R.mipmap.ic_tri_down);
                            }
                        });
                        if( barImgComActive != null )
                            ib.setImageDrawable(barImgComActive);
                        else
                            ib.setImageResource(R.mipmap.ic_tri_up);
                        pop.showAsDropDown(view,0, 0);
                        isPopShow = false;
                    }else{
                        if(isPopShow){
                            //向上的箭头
                            if( barImgComActive != null )
                                ib.setImageDrawable(barImgComActive);
                            else
                                ib.setImageResource(R.mipmap.ic_tri_up);
                            pop.showAsDropDown(view, 0, 0);
                            isPopShow = false;
                        }else{
                            //向下的箭头
                            if( barImgCom != null )
                                ib.setImageDrawable(barImgCom);
                            else
                                ib.setImageResource(R.mipmap.ic_tri_down);
                            pop.dismiss();
                            isPopShow = true;
                        }
                    }
                }
                onClickCustom();
            }
        });
        if (list == null || list.size() == 0){
            tv_name.setText("");
        }else{
            tv_name.setText(list.get(0));
        }
        addView(view);
    }

    public static int setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        int ff = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        return ff;
    }

    public void onClickCustom(){

    }

    public void attachDataSource(List<String> list){
        this.list = list;
        tv_name.setText(list.get(0));
    }
    public void setOnItemSelectedListener(OnItemSelectedListenerSpinner onItemSelectedListener){
        this.onItemSelectedListener = onItemSelectedListener;
    }
    public void setSpinnerHeiht(int heiht){
        this.heiht = heiht;
    }
    public void setSelectedIndex(int index){
        tv_name.setText(list.get(index));
        if( onItemSelectedListener != null )
            onItemSelectedListener.onItemSelected(index);
    }

    public interface OnItemSelectedListenerSpinner{
        void onItemSelected(int index);
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = mInflater.inflate(R.layout.item_spinner, null);
            final TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_name.setTextColor(barTxtColor);
            tv_name.setTextSize(barTxtSize);
            if (position == CustomSpinner.this.postion){
                //选中条目的背景色
                //view.setBackgroundColor(Color.rgb(26,208,189));
                tv_name.setTextColor(barTxtColorActive);
            }
            tv_name.setText(list.get(position));
            //设置按钮的监听事件
            view.setTag(tv_name);
            return view;
        }

    }

    @Override
    public void destroyDrawingCache() {
        if (pop != null && pop.isShowing()){
            pop.dismiss();
        }
        super.destroyDrawingCache();
    }
}