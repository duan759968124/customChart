package com.example.chartcolumn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chartcolumn.AchartViewDemo;
import com.example.chartcolumn.R;

import java.util.ArrayList;
import java.util.List;

public class ManageCenterAdapter extends RecyclerView.Adapter<ManageCenterAdapter.MyViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<Object> mDatas;
    private List<String> mNames;
    private List<String> mNums;
    private float MaxY = 50F;//y轴最大值

    public ManageCenterAdapter(Context mContext)
    {
        this.mContext = mContext;
    }

    public void setmDatas(List<Object> mDatas)
    {
        this.mDatas = mDatas;
    }

    public void setmNames(List<String> mNames)
    {
        this.mNames = mNames;
    }

    public void setmNums(List<String> mNums)
    {
        this.mNums = mNums;
    }

    public void setMaxY(float maxY)
    {
        MaxY = maxY;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_manageaxis_test, parent, false);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.achartview.setMax_Y((int)MaxY);
        holder.achartview.setTop(50);
        if (position==0){
            ViewGroup.LayoutParams layoutParams = holder.achartview.getLayoutParams();
            layoutParams.width = 198;
            holder.achartview.setLayoutParams(layoutParams);
        }else{
            ViewGroup.LayoutParams layoutParams = holder.achartview.getLayoutParams();
            layoutParams.width = 100;
            holder.achartview.setLayoutParams(layoutParams);
        }
        holder.achartview.setmGreenColor(mContext.getResources().getColor(R.color.c_4cc1fd));
        holder.achartview.setmLineColor(mContext.getResources().getColor(R.color.c_F2F2F2));
        if (position==0){
            holder.achartview.setShow_Y(true);
        }else{
            holder.achartview.setShow_Y(false);
        }
        List<Object> mDatas1 = new ArrayList<>();
        List<String> mNames1 = new ArrayList<>();
        List<String> mNums1 = new ArrayList<>();
        mDatas1.add(mDatas.get(position));
        mNames1.add(mNames.get(position));
        mNums1.add(mNums.get(position));
        holder.achartview.setmNames(mNames1);
        holder.achartview.setmData(mDatas1);
        holder.achartview.setmNums(mNums1);
        holder.itemView.setTag(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    @Override
    public void onClick(View view)
    {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(view,view.getTag().toString());
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {


        AchartViewDemo achartview;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            achartview = itemView.findViewById(R.id.achartview);
        }

    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //定义一个接口
    public  interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }
}
