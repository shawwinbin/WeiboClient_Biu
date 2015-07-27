package com.dudutech.biu.dao.favo;

import android.text.TextUtils;
import android.util.Log;

import com.dudutech.biu.api.UrlConstants;
import com.dudutech.biu.dao.HttpClientUtils;
import com.dudutech.biu.model.FavoModel;
import com.dudutech.biu.model.UserModel;
import com.dudutech.biu.network.WeiboParameters;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

/**
 * Created by shaw on 2015/7/28.
 */
public class FavoDao {

    public  final String TAG = FavoDao.class.getSimpleName();
    public long id;

    public FavoDao(long id){
        this.id=id;
    }

    // Add to favorite
    public  FavoModel favo() {
        return executeTask(UrlConstants.FAVORITES_CREATE);
    }

    // Remove from favorite
    public FavoModel unfav(long id) {
        return executeTask(UrlConstants.FAVORITES_DESTROY);
    }

    private FavoModel executeTask(String url) {
        WeiboParameters param=new WeiboParameters();
        param.put("id", id);
        try {
            String jsonData = HttpClientUtils.doPostRequstWithWithAceesToken(url,param);
            FavoModel value = new Gson().fromJson(jsonData, FavoModel.class);
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
