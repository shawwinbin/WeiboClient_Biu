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
import android.view.MotionEvent;
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
import com.shaweibo.biu.ui.timeline.UserHomeActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by shaw on 2015/7/11.
 */
public class StatusComentAdapter extends BaseTimelinAdapter<CommentListModel> implements View.OnClickListener{

    private StatusTimeUtils mTimeUtils;
    private View mHeadView;

   public HeaderViewTouchListener mHeaderViewTouchListener;
    public StatusComentAdapter(Context context, CommentListModel listModel,View headView) {
        super(context, listModel);
        mTimeUtils = StatusTimeUtils.instance(context);

        if(headView!=null){
            mHeadView=headView;
            setHeaderCount(1);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateContentView(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_weibo_comment, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view, mContext);
        return holder;


    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        super.onBindViewHolder(holder,position);
        if(holder instanceof  CommentViewHolder) {


            int realPostion=position-mHeaderCount;

            CommentModel commentModel = mListModel.getList().get(realPostion);

            CommentViewHolder commentViewHolder = (CommentViewHolder) holder;

            commentViewHolder.tv_content.setText(commentModel.span);
            commentViewHolder.tv_content.setMovementMethod(LinkMovementMethod.getInstance());
            String  source =   TextUtils.isEmpty(commentModel.source)?"": Utility.dealSourceString(commentModel.source);
            commentViewHolder.tv_time_source.setText(mTimeUtils.buildTimeString(commentModel.created_at) + " | " + source);
            commentViewHolder.tv_username.setText(commentModel.user.getName());

            String url = avartarHd?commentModel.user.avatar_large:commentModel.user.profile_image_url;
            if (!url.equals(commentViewHolder.iv_avatar.getTag())) {
                commentViewHolder.iv_avatar.setTag(url);
                ImageLoader.getInstance().displayImage(url, commentViewHolder.iv_avatar, Constants.getAvatarOptions(commentModel.user.getName().substring(0,1)));
        }
            commentViewHolder.iv_avatar.setTag(realPostion);
            commentViewHolder.iv_avatar.setOnClickListener(this);

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderView(ViewGroup parent) {

        return  new HeaderViewHolder(mHeadView);
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

        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_time_source)
        public TextView tv_time_source;
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

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
           view.setOnTouchListener(new View.OnTouchListener() {
               @Override
               public boolean onTouch(View v, MotionEvent event) {

                  return mHeaderViewTouchListener.onTouch(event);


               }
           });
        }
    }
    public void setHeaderViewTouchListener(HeaderViewTouchListener listener){
        mHeaderViewTouchListener=listener;
    }

    public interface HeaderViewTouchListener {
        public boolean onTouch( MotionEvent event);
    }

}
