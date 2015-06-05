package com.dudutech.weibo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;
import com.dudutech.weibo.R;
import com.dudutech.weibo.Utils.StatusTimeUtils;
import com.dudutech.weibo.Utils.Utility;
import com.dudutech.weibo.model.MessageListModel;
import com.dudutech.weibo.model.MessageModel;
import com.dudutech.weibo.widget.FlowLayout;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2014.12.29.
 */
public class TimelineAdapter  extends Adapter<TimelineAdapter.ViewHolder> {
    private  Context mContext;
    private final LayoutInflater mLayoutInflater;
    private List<MessageModel> mList;
    private StatusTimeUtils mTimeUtils;

    public TimelineAdapter(Context context,List<MessageModel> list){
        this.mContext=context;
        this.mList=list;
        mLayoutInflater = LayoutInflater.from(context);
        mTimeUtils=StatusTimeUtils.instance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mLayoutInflater.inflate(R.layout.card_weibo,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MessageModel msg=mList.get(position);
//        holder.tv_time.setText(msg.);
        holder.tv_content.setText(msg.span);
        holder.tv_username.setText(msg.user.name);
        String url = msg.user.avatar_large;
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .crossFade()
                .into(holder.iv_avatar);

        if(!msg.source.isEmpty()){
            holder.tv_from.setText(Html.fromHtml(msg.source));
        }

        holder.tv_time.setText(mTimeUtils.buildTimeString(msg.created_at));
        holder.tv_comment_count.setText(Utility.getCountString(msg.comments_count));
        holder.tv_like_count.setText("  "+Utility.getCountString(msg.attitudes_count));
        holder.tv_repost_count.setText(Utility.getCountString(msg.reposts_count));


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_time)
        public TextView tv_time;
        @InjectView(R.id.tv_username)
        public TextView tv_username;
        @InjectView(R.id.tv_from)
        public TextView tv_from;
        @InjectView(R.id.tv_content)
        public TextView tv_content;
//        public View v;
       @InjectView(R.id.ll_comment)
        public LinearLayout ll_comment;
        @InjectView(R.id.ll_like)
        public LinearLayout ll_like;
        @InjectView(R.id.ll_repost)
        public LinearLayout ll_repost;
        @InjectView(R.id.fl_images)
        public FlowLayout fl_images;
        @InjectView(R.id.iv_avatar)
        public ImageView iv_avatar;

        @InjectView(R.id.tv_comment_count)
        public  TextView tv_comment_count;
        @InjectView(R.id.tv_repost_count)
        public  TextView tv_repost_count;

        @InjectView(R.id.tv_like_count)
        public  TextView tv_like_count;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
//            v=itemView;
//            init();
        }

//        private void init(){
//            tv_time= Utility.findViewById(v,R.id.tv_time);
//            tv_username= Utility.findViewById(v,R.id.tv_username);
//            ll_comment= Utility.findViewById(v,R.id.ll_comment);
//            ll_like= Utility.findViewById(v,R.id.ll_like);
//            ll_share= Utility.findViewById(v,R.id.ll_share);
//            fl_images= Utility.findViewById(v,R.id.fl_images);
//            iv_avatar= Utility.findViewById(v,R.id.iv_avatar);
//            tv_content=Utility.findViewById(v,R.id.tv_content);
//            tv_from=Utility.findViewById(v,R.id.tv_from);
//        }
    }
}
