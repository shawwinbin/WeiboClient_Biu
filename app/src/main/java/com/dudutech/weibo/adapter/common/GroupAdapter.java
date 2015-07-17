package com.dudutech.weibo.adapter.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dudutech.weibo.R;
import com.dudutech.weibo.model.GroupListModel;
import com.dudutech.weibo.model.GroupModel;

/**
 * Created by Administrator on 2015-7-17.
 */
public class GroupAdapter extends BaseAdapter{

    private Context mContext ;
    private GroupListModel mListModel;

    public GroupAdapter(Context context , GroupListModel listModel){
        mContext=context;
        mListModel=listModel;
    }

    @Override
    public int getCount() {
        return mListModel.getList().size();
    }

    @Override
    public Object getItem(int position) {
        return mListModel.getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return mListModel.getList().get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_group,null);
        }
        TextView tv_group= (TextView) convertView.findViewById(R.id.tv_group);
        tv_group.setText(mListModel.get(position).name);

        return convertView;
    }
}
