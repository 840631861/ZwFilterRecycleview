package com.project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.R;
import com.project.model.ZwFilterCheckDataItem;
import com.project.utils.DensityUtil;
import com.project.view.BarCheckText;

import java.util.ArrayList;

/**
 * 顶部栏按钮横向滚动适配器
 * Created by Administrator on 2018/8/8.
 */

public class BarBtnsRecycleAdapter extends RecyclerView.Adapter<BarBtnsRecycleAdapter.MyViewHoler> implements View.OnClickListener {
    int ResourceID;
    Context mContext;
    ArrayList<ZwFilterCheckDataItem> mData;
    private OnRecycleViewItemClickListener mOnItemClickListener;
    int barTxtSize,barTxtColor,barTxtColorActive,barHeight;

    public BarBtnsRecycleAdapter(Context context, int resourceID, ArrayList<ZwFilterCheckDataItem> brings, int barTxtSize, int barTxtColor, int barTxtColorActive, int barHeight) {
        mContext=context;
        mData=brings;
        ResourceID=resourceID;
        this.barTxtSize = barTxtSize;
        this.barTxtColor = barTxtColor;
        this.barTxtColorActive = barTxtColorActive;
        this.barHeight = barHeight;
    }

    @Override
    public void onBindViewHolder(MyViewHoler holder, int position) {

        //相当于listview的adapter中的getview方法
        ZwFilterCheckDataItem item=mData.get(position);

        holder.tvToolName.setCheckedColor(barTxtColorActive,barTxtColor);
        holder.tvToolName.setTextSize(barTxtSize);
        holder.tvToolName.setHeight(DensityUtil.dip2px(mContext,barHeight));
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
        void OnItemClick(View view, int position);
    }
    class MyViewHoler extends RecyclerView.ViewHolder
    {
        private final BarCheckText tvToolName;

        public MyViewHoler(View itemView) {
            super(itemView);
            tvToolName=(BarCheckText)itemView.findViewById(R.id.tv_text);
        }
    }


}
