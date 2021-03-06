/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.shaweibo.biu.adapter.comments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shaweibo.biu.R;
import com.shaweibo.biu.Utils.StatusTimeUtils;
import com.shaweibo.biu.Utils.Utility;
import com.shaweibo.biu.adapter.timeline.BaseTimelinAdapter;
import com.shaweibo.biu.global.Constants;
import com.shaweibo.biu.model.CommentListModel;
import com.shaweibo.biu.model.CommentModel;
import com.shaweibo.biu.ui.timeline.StatusDetailActivity;
import com.shaweibo.biu.ui.timeline.UserHomeActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015-7-16.
 */
public class CommentMeAdapter  extends BaseTimelinAdapter<CommentListModel> implements View.OnClickListener {
    private StatusTimeUtils mTimeUtils;


    public CommentMeAdapter(Context context, CommentListModel commentListModel) {
        super(context, commentListModel);
        mTimeUtils = StatusTimeUtils.instance(context);

        setBottomCount(1);

    }


    @Override
    public RecyclerView.ViewHolder onCreateContentView(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.card_comment_me, parent, false);
        CommentMeViewHolder holder = new CommentMeViewHolder(view, mContext);
        return holder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        super.onBindViewHolder(holder,position);
        if(holder instanceof  CommentMeViewHolder) {
            CommentModel commentModel = mListModel.getList().get(position);
            CommentMeViewHolder commentViewHolder = (CommentMeViewHolder) holder;
            commentViewHolder.tv_content.setText(commentModel.span);
            commentViewHolder.tv_content.setMovementMethod(LinkMovementMethod.getInstance());
//            commentViewHolder.tv_time.setText(commentModel.created_at);
            String  source =   TextUtils.isEmpty(commentModel.source)?"": Utility.dealSourceString(commentModel.source);
            commentViewHolder.tv_time_source.setText(mTimeUtils.buildTimeString(commentModel.created_at)+" | "+source);
            commentViewHolder.tv_username.setText(commentModel.user.getName());
            String url = avartarHd?commentModel.user.avatar_large:commentModel.user.profile_image_url;

            if (!url.equals(commentViewHolder.iv_avatar.getTag())) {
                commentViewHolder.iv_avatar.setTag(url);
                ImageLoader.getInstance().displayImage(url, commentViewHolder.iv_avatar,  Constants.getAvatarOptions(commentModel.user.name.substring(0, 1)));
            }
            commentViewHolder.iv_avatar.setTag(position);
            commentViewHolder.iv_avatar.setOnClickListener(this);
            String statusImgUrl= commentModel.status.thumbnail_pic;
            if(TextUtils.isEmpty(statusImgUrl)&&commentModel.status.retweeted_status!=null){
                statusImgUrl=commentModel.status.retweeted_status.thumbnail_pic;
            }
            if(TextUtils.isEmpty(statusImgUrl)){
                statusImgUrl=commentModel.status.user.avatar_large;
            }
            if (!statusImgUrl.equals(commentViewHolder.iv_status.getTag())) {
                commentViewHolder.iv_status.setTag(statusImgUrl);
                ImageLoader.getInstance().displayImage(statusImgUrl, commentViewHolder.iv_status, Constants.timelineListOptions);
            }
            commentViewHolder.tv_status_author.setText(commentModel.status.user.getName());
            commentViewHolder.tv_status_content.setText(commentModel.status.text);
            commentViewHolder.status_source.setTag(position);
            commentViewHolder.status_source.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        int postion = (int) v.getTag();
        CommentModel msg = mListModel.get(postion);
        switch (viewId) {
            case R.id.iv_avatar:
                UserHomeActivity.start(mContext, msg.user);
                break;
            case R.id.status_source:
                StatusDetailActivity.start(mContext, msg.status);
                break;
        }
    }


    public class CommentMeViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_time_source)
        public TextView tv_time_source;
        @InjectView(R.id.tv_username)
        public TextView tv_username;
        @InjectView(R.id.tv_content)
        public TextView tv_content;
        @InjectView(R.id.iv_avatar)
        public ImageView iv_avatar;
        @InjectView(R.id.iv_source)
        public ImageView iv_status;
        @InjectView(R.id.tv_status_author)
        public TextView tv_status_author;
        @InjectView(R.id.tv_status_content)
        public TextView tv_status_content;
        @InjectView(R.id.status_source)
        View status_source;

        public CommentMeViewHolder(View itemView, Context context) {
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
