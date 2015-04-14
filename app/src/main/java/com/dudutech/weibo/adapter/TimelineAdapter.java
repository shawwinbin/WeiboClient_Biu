package com.dudutech.weibo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dudutech.weibo.model.MessageListModel;
import com.dudutech.weibo.model.MessageModel;

/**
 * Created by Administrator on 2014.12.29.
 */
public class TimelineAdapter  extends HeaderViewAdapter<TimelineAdapter.ViewHolder> {
    private  Context mContext;

    private MessageListModel mList;

    public TimelineAdapter(){

    }


    @Override
    int getCount() {
        return 0;
    }

    @Override
    int getViewType(int position) {
        return 0;
    }

    @Override
    long getItemViewId(int position) {
        return 0;
    }

    @Override
    void doRecycleView(ViewHolder h) {

    }

    @Override
    ViewHolder doCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    ViewHolder doCreateHeaderHolder(View header) {
        return null;
    }

    @Override
    void doBindViewHolder(ViewHolder h, int position) {

    }

    public static class ViewHolder extends HeaderViewAdapter.ViewHolder {
        public boolean sub = false;

        public TextView date;
        public TextView retweets;
        public TextView comments;
        public TextView name;
        public TextView from;
        public TextView content;
        public TextView attitudes;
        public TextView orig_content;
        public ImageView avatar;
        public ImageView popup;
        public HorizontalScrollView scroll;
        public LinearLayout pics;
        public View card;
        public View origin_parent;
        public View comment_and_retweet;

        public View v;
        public MessageModel msg = null;
        public Context context;
        public TimelineAdapter adapter;

        public ViewHolder(View v) {
            super(v);
            isHeader = true;
        }

        public ViewHolder(TimelineAdapter adapter, View v) {
            super(v);
            this.v = v;
            this.context = v.getContext();
            this.adapter = adapter;

            v.setTag(this);

//            init();
        }
    }
}
