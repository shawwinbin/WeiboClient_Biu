/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.adapter.timeline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dudutech.weibo.R;
import com.dudutech.weibo.Utils.StatusTimeUtils;
import com.dudutech.weibo.adapter.BaseMultipleItemAdapter;
import com.dudutech.weibo.global.Constants;
import com.dudutech.weibo.model.CommentListModel;
import com.dudutech.weibo.model.CommentModel;
import com.dudutech.weibo.model.MessageListModel;
import com.dudutech.weibo.widget.FlowLayout;
import com.dudutech.weibo.widget.LetterImageView;
import com.dudutech.weibo.widget.TagImageVIew;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by shaw on 2015/7/11.
 */
public class StatusComentAdapter extends BaseTimelinAdapter<CommentListModel> {

    private StatusTimeUtils mTimeUtils;
    public StatusComentAdapter(Context context, CommentListModel listModel) {
        super(context, listModel);
        mTimeUtils = StatusTimeUtils.instance(context);
    }

//    private Context mContext;
//    private final LayoutInflater mLayoutInflater;
//
//    private CommentListModel mListModel;

    public static enum ITEM_TYPE {
        ITEM_TYPE_HEADER,
        ITEM_TYPE_BOTTOM,
        ITEM_TYPE_NORMAL,
    }


//    public StatusComentAdapter (Context context ,CommentListModel listModel ){
//        mContext=context;
//        mListModel=listModel;
//        mLayoutInflater = LayoutInflater.from(context);
//
//    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderView(ViewGroup parent) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentView(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_weibo_comment, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view, mContext);
        return holder;


    }

//    @Override
//    public RecyclerView.ViewHolder onCreateBottomView(ViewGroup parent) {
//        View view = mLayoutInflater.inflate(R.layout.bottom_view_loading, parent, false);
//        BottomViewHolder bottomViewHolder = new BottomViewHolder(view, mContext);
//        return bottomViewHolder;
//    }

    @Override
    public int getContentItemCount() {
        return mListModel.getList().size();
    }

    @Override
    public int getContentItemViewType(int position) {
        return 3;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        super.onBindViewHolder(holder,position);
        if(holder instanceof  CommentViewHolder) {


            CommentModel commentModel = mListModel.getList().get(position);

            CommentViewHolder commentViewHolder = (CommentViewHolder) holder;

            commentViewHolder.tv_content.setText(commentModel.span);
            commentViewHolder.tv_content.setMovementMethod(LinkMovementMethod.getInstance());
//            commentViewHolder.tv_time.setText(commentModel.created_at);
            commentViewHolder.tv_time.setText(mTimeUtils.buildTimeString(commentModel.created_at));
            commentViewHolder.tv_username.setText(commentModel.user.getName());
            commentViewHolder.tv_username.setText(commentModel.user.getName());
            String url = commentModel.user.avatar_large;
            if (!url.equals(commentViewHolder.iv_avatar.getTag())) {
                commentViewHolder.iv_avatar.setTag(url);
                ImageLoader.getInstance().displayImage(url, commentViewHolder.iv_avatar, Constants.avatarOptions);
            }
        }

    }
//    public class BottomViewHolder extends RecyclerView.ViewHolder {
//
//        public BottomViewHolder(View view, Context context) {
//            super(view);
//        }
//
//    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_time)
        public TextView tv_time;
        @InjectView(R.id.tv_username)
        public TextView tv_username;
        @InjectView(R.id.tv_content)
        public TextView tv_content;
        @InjectView(R.id.iv_avatar)
        public ImageView iv_avatar;



        public CommentViewHolder(View itemView, Context context) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (mListenner != null) {
//
//                        mListenner.onTtemClick(v, getAdapterPosition());
//                    }
                }
            });


        }


    }
}
