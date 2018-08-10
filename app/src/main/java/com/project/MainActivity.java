package com.project;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jzxiang.pickerview.data.Type;
import com.project.adapter.NomalAdapter;
import com.project.manager.FilterManager;
import com.project.manager.IListView;
import com.project.model.ZwFilterCheckData;
import com.project.model.ZwFilterCheckDataItem;
import com.project.model.ZwFilterData;
import com.project.model.ZwParams;
import com.project.view.ZwFilterRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    private ZwFilterRefreshView view;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.view);
        setListView();
    }

    private void setListView()
    {
        List<ZwFilterCheckData> checkDatas = getFilterCheckData();

        //设置侧拉栏数据
        view.getFilterManager()
                //.addCheckData(data)//添加单个侧拉栏中数据（选择按钮）
                .addCheckDatas(checkDatas)//添加多个侧拉栏中数据(选择按钮)
                .setOnFilterItemChangeListener(new IListView.OnFilterItemChangeListener() {
                    @Override
                    public void onFilterItemChange(ZwFilterData data) {
                        //点击或改变筛选中的item数值后的回调
                        Toast.makeText(context,"点击了选项",Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnFilterConfirmClickListener(new IListView.OnFilterConfirmClickListener() {
                    @Override
                    public void onFilterConfirmClick(ZwFilterData data) {
                        //点击确认按钮的监听
                        ZwParams params = view.getBarCurData();
                        Toast.makeText(context,"点击了确认",Toast.LENGTH_SHORT).show();
                        List<ZwFilterCheckData> list = data.getCheckDatas();
                        view.getFilterManager().hideDialog();
                    }
                })
                .setOnFilterResetClickListener(new IListView.OnFilterResetClickListener() {
                    @Override
                    public void onFilterResetClick(ZwFilterData data) {
                        //点击重置按钮的监听
                        Toast.makeText(context,"点击了重置",Toast.LENGTH_SHORT).show();
                        List<ZwFilterCheckData> list = data.getCheckDatas();
                    }
                });

        View custom = LayoutInflater.from(context).inflate(R.layout.layout_custom,null);
        //设置侧拉栏样式
        view.getFilterManager()
                //.addTimeSection(getSupportFragmentManager(), Type.YEAR_MONTH)//添加时间段（不带样式使用默认）
                .addTimeSection(getSupportFragmentManager(), Type.YEAR_MONTH,R.color.colorAccent)//添加时间段（带样式）
                .addSearchTxt()//添加搜索框
                .addCustomView(custom, new IListView.OnAddCustemViewCallback() //添加自定义布局
                {
                    @Override
                    public void onAddCustemView(View parent, View custom) {
                        //自定义布局回调
                    }
                })
                .addSeekBar1("距离",0,200,0)
                .addSeekBar2("距离2",0,200,0);


        //设置顶部栏样式
        view.getViewBarManager()
                //.setBarTxtSize(15)
                //.setBarImgCom(getResources().getDrawable(R.mipmap.ic_tri_down),getResources().getDrawable(R.mipmap.ic_tri_up))
                .setComSpinnerData(setMarkData())
                .setFilterTxt("筛选")
                .setOnBarItemSelectedListener(new IListView.onBarItemSelectedListener() {
                    @Override
                    public void onBarItemSelected(ZwFilterCheckDataItem checkDataItem) {
                        Toast.makeText(context,"按钮",Toast.LENGTH_SHORT).show();
                    }
                })
                .setComSpinnerSelectedIndex(0)
                .setMarkData(setMarkData())
                .setBarBtns(setMarkData());

        view.updateView();

        //设置数据
        List<Map<String,String>> list = getData();
        NomalAdapter adapter = new NomalAdapter(context,list);
        view.getRecyclerView().setLayoutManager(new LinearLayoutManager(context));
        view.getRecyclerView().setAdapter(adapter);

        //设置刷新加载监听
        view.getRefreshLayout().setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束刷新
                        view.getRefreshLayout().finishRefresh();
                    }
                }, 2000);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束加载更多
                        view.getRefreshLayout().finishLoadMore();
                        view.getRefreshLayout().setCanLoadMore(false);
                    }
                }, 2000);
            }
        });
    }


    //设置列表数据
    public List<Map<String,String>> getData()
    {
        final List<Map<String,String>> data = new ArrayList<>();
        for( int i=0;i<30;i++ )
        {
            Map<String,String> map = new HashMap<>();
            map.put("id","文本"+i);
            data.add(map);
        }

        return data;
    }

    //设置筛选菜单中单选/多选的数据
    public List<ZwFilterCheckData> getFilterCheckData()
    {
        ZwFilterCheckData data = new ZwFilterCheckData();
        ZwFilterCheckData data2 = new ZwFilterCheckData();
        List<ZwFilterCheckDataItem> ls = new ArrayList<>();List<ZwFilterCheckDataItem> ls2 = new ArrayList<>();
        ZwFilterCheckDataItem dataItem1 = new ZwFilterCheckDataItem(),dataItem2 = new ZwFilterCheckDataItem(),dataItem3 = new ZwFilterCheckDataItem(),dataItem4 = new ZwFilterCheckDataItem();
        dataItem1.setShowName("语文"); dataItem1.setChecked(false);
        dataItem2.setShowName("数学"); dataItem2.setChecked(false);
        dataItem3.setShowName("英语"); dataItem3.setChecked(false);
        dataItem4.setShowName("历史"); dataItem4.setChecked(false);
        ls.add(dataItem1);ls.add(dataItem2);
        ls2.add(dataItem3);ls2.add(dataItem4);
        data.setList(ls);
        data.setTitle("科目2");
        data.setSort(2);
        data2.setList(ls2);
        data2.setTitle("科目");
        data2.setSort(1);
        data2.setType(FilterManager.FILTER_TYPE_SIN_ELECTION);

        List<ZwFilterCheckData> list = new ArrayList<>();
        list.add(data);
        list.add(data2);
        return list;
    }

    public ArrayList<ZwFilterCheckDataItem> setMarkData()
    {
        final ArrayList<ZwFilterCheckDataItem> markData = new ArrayList<>();
        for( int i=0;i<20;i++ ){
            ZwFilterCheckDataItem item = new ZwFilterCheckDataItem();
            item.setChecked(false);
            item.setShowName("标签标签"+i);
            markData.add(item);
        }
        return markData;
    }

}
