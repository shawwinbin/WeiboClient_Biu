/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.biu.adapter.timeline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dudutech.biu.R;
import com.dudutech.biu.adapter.common.BaseMultipleItemAdapter;
import com.dudutech.biu.model.BaseListModel;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.dudutech.biu.global.Constants.*;
import static com.dudutech.biu.global.Constants.LOADING_STATUS.*;


/**
 * Created by shaw on 2015/7/12.
 */
public abstract class BaseTimelinAdapter< T extends BaseListModel> extends BaseMultipleItemAdapter {

    protected Context mContext;
    protected final LayoutInflater mLayoutInflater;
    protected T  mListModel ;

    protected LOADING_STATUS mBottomStatus;



    public  BaseTimelinAdapter(Context context ,T t){
        mContext=context;
        mListModel=t;
        mLayoutInflater = LayoutInflater.from(context);
        mBottomStatus= NORMAL;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if( holder instanceof BottomViewHolder){
            BottomViewHolder bottomViewHolder = (BottomViewHolder) holder;
            onBindBottomViewHolder(bottomViewHolder);
        }


    }
    @Override
    public RecyclerView.ViewHolder onCreateHeaderView(ViewGroup parent) {
        return null;
    }


    protected  void onBindBottomViewHolder(BottomViewHolder holder){
        switch (mBottomStatus){
            case NORMAL :
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.tv_loading_bottom.setVisibility(View.GONE);


                break;
            case FINISH :
                holder.progressBar.setVisibility(View.GONE);
                holder.tv_loading_bottom.setVisibility(View.VISIBLE);
                holder.tv_loading_bottom.setText(R.string.loading_finish);

                break;
            case FAIL:
                holder.progressBar.setVisibility(View.GONE);
                holder.tv_loading_bottom.setVisibility(View.VISIBLE);
                holder.tv_loading_bottom.setText(R.string.loading_fail);

                break;
        }

    }

    /**
     *  设置底部laoding 状态
     * @param bottomStatus
     */

    public void setBottomStatus(LOADING_STATUS bottomStatus){
        mBottomStatus=bottomStatus;
    }

    public LOADING_STATUS getBottomStatus(){

        return mBottomStatus;
    }

    @Override
    public RecyclerView.ViewHolder onCreateBottomView(ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.bottom_view_loading, parent, false);
        BottomViewHolder bottomViewHolder = new BottomViewHolder(view, mContext);
        return bottomViewHolder;
    }

    public static class BottomViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.progressbar)
        ProgressBar progressBar;
        @InjectView(R.id.tv_loading_bottom)
        TextView tv_loading_bottom;

        public BottomViewHolder(View view, Context context) {
            super(view);
            ButterKnife.inject(this, itemView);
        }
    }
    @Override
    public int getContentItemCount() {
        return mListModel.getList().size();
    }
    @Override
    public int getContentItemViewType(int position) {
        return ITEM_TYPE.values().length+1;
    }


}
