package com.dudutech.weibo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView.Adapter;

import com.dudutech.weibo.R;
import com.dudutech.weibo.Utils.Utility;
import com.dudutech.weibo.model.MessageListModel;
import com.dudutech.weibo.model.MessageModel;
import com.dudutech.weibo.widget.FlowLayout;

import java.util.List;

/**
 * Created by Administrator on 2014.12.29.
 */
public class TimelineAdapter  extends Adapter<TimelineAdapter.ViewHolder> {
    private  Context mContext;

    private List<MessageModel> mList;

    public TimelineAdapter(Context context,List<MessageModel> list){
        this.mContext=context;
        this.mList=list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(parent.getContext(), R.layout.item_weibo,null);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MessageModel msg=mList.get(position);

//        holder.tv_time.setText(msg.);




    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_time;
        public TextView tv_username;
        public TextView tv_from;
        public TextView tv_content;
        public View v;
        public LinearLayout ll_comment;
        public LinearLayout ll_like;
        public LinearLayout ll_share;
        public FlowLayout fl_images;
        public ImageView iv_avatar;
        public ViewHolder(View itemView) {
            super(itemView);
            v=itemView;
            init();
        }

        private void init(){
            tv_time= Utility.findViewById(v,R.id.tv_time);
            tv_username= Utility.findViewById(v,R.id.tv_username);
            ll_comment= Utility.findViewById(v,R.id.ll_comment);
            ll_like= Utility.findViewById(v,R.id.ll_like);
            ll_share= Utility.findViewById(v,R.id.ll_share);
            fl_images= Utility.findViewById(v,R.id.fl_images);
            iv_avatar= Utility.findViewById(v,R.id.iv_avatar);
            tv_content=Utility.findViewById(v,R.id.tv_content);
            tv_from=Utility.findViewById(v,R.id.tv_from);
        }
    }
}
