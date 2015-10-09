package com.shaweibo.biu.dao.relationship;

import android.text.TextUtils;
import android.util.Log;

import com.shaweibo.biu.dao.UrlConstants;
import com.shaweibo.biu.dao.HttpClientUtils;
import com.shaweibo.biu.model.UserModel;
import com.shaweibo.biu.dao.WeiboParameters;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

/**
 * User: qii
 * Date: 12-10-12
 */
public class FanDao {

    public  final String TAG = FanDao.class.getSimpleName();

    public FanDao( String uid) {

        this.uid = uid;
    }


    private String uid;

    public UserModel followUser(){

        return executeTask(UrlConstants.FRIENDSHIPS_CREATE);
    }

    public UserModel unFllowUser(){

        return executeTask(UrlConstants.FRIENDSHIPS_DESTROY);
    }



    private UserModel executeTask(String url) {
        if (TextUtils.isEmpty(uid) ) {
            Log.e(TAG, "uid can't be empty");
            return null;
        }

        WeiboParameters param=new WeiboParameters();

        param.put("uid", uid);

        try {
            String jsonData = HttpClientUtils.doPostRequstWithWithAceesToken(url,param);
            UserModel value = new Gson().fromJson(jsonData, UserModel.class);
            if (value != null) {
                return value;
            }
        } catch (JsonSyntaxException e) {

            Log.e(TAG,e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
