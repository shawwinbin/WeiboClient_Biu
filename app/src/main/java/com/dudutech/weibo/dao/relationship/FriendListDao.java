package com.dudutech.weibo.dao.relationship;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.dudutech.weibo.api.UrlConstants;
import com.dudutech.weibo.dao.HttpClientUtils;
import com.dudutech.weibo.dao.timeline.BaseTimelineDao;
import com.dudutech.weibo.global.Constants;
import com.dudutech.weibo.model.BaseListModel;
import com.dudutech.weibo.model.UserListModel;
import com.dudutech.weibo.network.WeiboParameters;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by Administrator on 2015-7-21.
 */
public class FriendListDao extends AbUserListDao<UserListModel> {
    public Constants.LOADING_STATUS mStatus;
    private String uid;
    private UserListModel mListModel;
    private String screen_name;
    private int count;
    private int trim_status;
    private Context mContext;
    public final static int  PAGESIZE=50;
    public FriendListDao(Context context,String uid){
        this.uid=uid;
        mContext=context;
        count=PAGESIZE;
        this.trim_status=1;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTrim_status(int trim_status) {
        this.trim_status = trim_status;
    }


    @Override
    public BaseListModel load() {
        WeiboParameters param=new WeiboParameters();
        if(TextUtils.isEmpty(uid)&&!TextUtils.isEmpty(screen_name)){
            param.put("screen_name", screen_name);
        }
        else {
            param.put("uid", uid);
        }
        param.put("cursor", cursor);
        param.put("trim_status", trim_status);
        param.put("count", count);
        UserListModel userListModel=null;
        try {
            String jsonStr=HttpClientUtils.doGetRequstWithAceesToken(UrlConstants.FRIENDSHIPS_FRIENDS,param);
            userListModel = new Gson().fromJson(jsonStr,UserListModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  userListModel;

    }
    @Override
    protected Class<? extends UserListModel> getListClass() {
        return UserListModel.class;
    }

    @Override
    public void cache() {

    }

    @Override
    public Cursor query() {
        return null;
    }
}
