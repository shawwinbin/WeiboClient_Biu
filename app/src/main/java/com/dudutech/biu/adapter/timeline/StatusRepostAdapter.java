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
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dudutech.biu.R;
import com.dudutech.biu.Utils.StatusTimeUtils;
import com.dudutech.biu.Utils.Utility;
import com.dudutech.biu.global.Constants;
import com.dudutech.biu.model.MessageModel;
import com.dudutech.biu.model.RepostListModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by shaw on 2015/7/11.
 */
public class StatusRepostAdapter extends BaseTimelinAdapter<RepostListModel> {

    private StatusTimeUtils mTimeUtils;
    private View mHeadView;
    public StatusRepostAdapter(Context context, RepostListModel listModel,View headView) {
        super(context, listModel);
        mTimeUtils = StatusTimeUtils.instance(context);
        if(headView!=null){
            mHeadView=headView;
            setHeaderCount(1);
        }
    }






    @Override
    public RecyclerView.ViewHolder onCreateHeaderView(ViewGroup parent) {
        return new HeaderViewHolder(mHeadView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentView(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_weibo_comment, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view, mContext);
        return holder;


    }




    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

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


            MessageModel commentModel = mListModel.getList().get(position-mHeaderCount);

            CommentViewHolder commentViewHolder = (CommentViewHolder) holder;

            commentViewHolder.tv_content.setText(commentModel.span);
            commentViewHolder.tv_content.setMovementMethod(LinkMovementMethod.getInstance());
//            commentViewHolder.tv_time.setText(commentModel.created_at);
            String  source =   TextUtils.isEmpty(commentModel.source)?"": Utility.dealSourceString(commentModel.source);
            commentViewHolder.tv_time_source.setText(mTimeUtils.buildTimeString(commentModel.created_at) + " | " + source);
            commentViewHolder.tv_username.setText(commentModel.user.getName());
            commentViewHolder.tv_username.setText(commentModel.user.getName());
            String url = avartarHd?commentModel.user.avatar_large:commentModel.user.profile_image_url;

            if (!url.equals(commentViewHolder.iv_avatar.getTag())) {
                commentViewHolder.iv_avatar.setTag(url);
                ImageLoader.getInstance().displayImage(url, commentViewHolder.iv_avatar, Constants.getAvatarOptions(commentModel.user.getName().substring(0,1)));
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
}
