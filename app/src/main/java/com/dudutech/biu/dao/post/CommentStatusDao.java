package com.dudutech.biu.dao.post;

import com.dudutech.biu.dao.HttpClientUtils;
import com.dudutech.biu.dao.UrlConstants;
import com.dudutech.biu.model.CommentModel;
import com.dudutech.biu.dao.WeiboParameters;
import com.google.gson.Gson;

import java.io.IOException;

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

        CommentModel value = null;
        try {
            String  jsonData = HttpClientUtils.doPostRequstWithWithAceesToken(url, params);
            value =  new Gson().fromJson(jsonData.toString(), CommentModel.class);
        } catch (IOException e) {
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
