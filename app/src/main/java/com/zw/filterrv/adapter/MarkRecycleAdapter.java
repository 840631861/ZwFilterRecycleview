package com.zw.filterrv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zw.filterrv.R;
import com.zw.filterrv.model.ZwFilterCheckDataItem;
import com.zw.filterrv.view.CheckText;

import java.util.ArrayList;

/**
 * 标签横向滚动适配器
 * Created by Administrator on 2018/8/8.
 */

public class MarkRecycleAdapter extends RecyclerView.Adapter<MarkRecycleAdapter.MyViewHoler> implements View.OnClickListener {
    int ResourceID;
    Context mContext;
    ArrayList<ZwFilterCheckDataItem> mData;
    private OnRecycleViewItemClickListener mOnItemClickListener;

    public MarkRecycleAdapter(Context context, int resourceID, ArrayList<ZwFilterCheckDataItem> brings) {
        mContext=context;
        mData=brings;
        ResourceID=resourceID;
    }

    @Override
    public void onBindViewHolder(MyViewHoler holder, int position) {

        //相当于listview的adapter中的getview方法
        ZwFilterCheckDataItem item=mData.get(position);
        holder.tvToolName.setText(item.getShowName());
        if( item.getChecked() )
            holder.tvToolName.setChecked(true);
        else
            holder.tvToolName.setChecked(false);
        holder.itemView.setTag(position);//将位置保存在tag中
    }

    @Override
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        //负责创建视图
        View view= LayoutInflater.from(mContext).inflate(ResourceID,null);
        view.setOnClickListener(this);
        return new MyViewHoler(view);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener!=null)
        {
            mOnItemClickListener.OnItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecycleViewItemClickListener listener)
    {
        this.mOnItemClickListener=listener;
    }
    public static interface OnRecycleViewItemClickListener{
        void OnItemClick(View view,int position);
    }
    class MyViewHoler extends RecyclerView.ViewHolder
    {
        private final CheckText tvToolName;

        public MyViewHoler(View itemView) {
            super(itemView);
            tvToolName=(CheckText)itemView.findViewById(R.id.tv_text);

        }
    }


}
