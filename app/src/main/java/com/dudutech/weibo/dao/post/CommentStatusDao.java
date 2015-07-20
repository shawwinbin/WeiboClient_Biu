package com.dudutech.weibo.dao.post;

import com.dudutech.weibo.api.BaseApi;
import com.dudutech.weibo.api.UrlConstants;
import com.dudutech.weibo.model.CommentModel;
import com.dudutech.weibo.network.HttpUtility;
import com.dudutech.weibo.network.WeiboParameters;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shaw on 2015/7/20.
 */
public class CommentStatusDao {

    public CommentModel sendComment()  {
        String url = UrlConstants.COMMENT_CREATE;
        WeiboParameters params =new WeiboParameters();

        params.put("id", id);
        params.put("comment", comment);
        params.put("comment_ori", comment_ori);

        JSONObject jsonData = new JSONObject();
        try {
            jsonData = BaseApi.request(url, params, HttpUtility.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        CommentModel value = null;
        try {
            value = gson.fromJson(jsonData.toString(), CommentModel.class);
        } catch (JsonSyntaxException e) {
           e.printStackTrace();
        }

        return value;
    }

    public CommentStatusDao(String id, String comment) {

        this.id = id;
        this.comment = comment;
        this.comment_ori = "0";
    }

    public void enableComment_ori(boolean enable) {
        if (enable) {
            this.comment_ori = "1";
        } else {
            this.comment_ori = "0";
        }
    }

    private String id;
    private String comment;
    private String comment_ori;
}
