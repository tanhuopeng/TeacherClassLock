package com.jci.teacherclasslock.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jci.teacherclasslock.R;
import com.jci.teacherclasslock.widget.CustomDialog;

import java.util.ArrayList;

/**
 * 创建人: 谭火朋
 * 创建时间: 2017/11/9 0009 23:56
 */

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ViewHolder> {

    private ArrayList<String> mData;
    private ClassListAdapter.OnItemClickListener onItemClickListener;


    public ClassListAdapter(ArrayList<String> data) {
        mData = data;
    }

    public void setOnItemClickListener(ClassListAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void updateData(ArrayList<String> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ClassListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_class_list_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ClassListAdapter.ViewHolder holder, int position) {
// 绑定数据
        holder.mTv.setText(mData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, pos);
                }
                //表示此事件已经消费，不会触发单击事件
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.mClassListItem);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }



}
