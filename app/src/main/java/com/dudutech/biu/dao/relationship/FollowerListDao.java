package com.dudutech.biu.dao.relationship;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.dudutech.biu.dao.UrlConstants;
import com.dudutech.biu.dao.HttpClientUtils;
import com.dudutech.biu.global.Constants;
import com.dudutech.biu.model.BaseListModel;
import com.dudutech.biu.model.UserListModel;
import com.dudutech.biu.dao.WeiboParameters;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by Administrator on 2015-7-21.
 */
public class FollowerListDao extends AbUserListDao<UserListModel> {
    public Constants.LOADING_STATUS mStatus;
    private String uid;
    private UserListModel mListModel;
    private String screen_name;
    private int count;
    private int trim_status;
    private Context mContext;
    public final static int  PAGESIZE=50;
    public FollowerListDao(Context context, String uid){
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
            String jsonStr=HttpClientUtils.doGetRequstWithAceesToken(UrlConstants.FRIENDSHIPS_FOLLOWERS,param);
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
