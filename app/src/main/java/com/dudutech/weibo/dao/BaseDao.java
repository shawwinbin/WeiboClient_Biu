package com.dudutech.weibo.dao;

import android.util.Log;

import com.dudutech.weibo.network.WeiboParameters;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static com.dudutech.weibo.BuildConfig.DEBUG;

/**
 * Created by Administrator on 2015-7-17.
 */
public class BaseDao {
    private static final String TAG = BaseDao.class.getSimpleName();
    private final static  OkHttpClient client = new OkHttpClient();
    // Access Token
    private static String mAccessToken;
    protected String doGetRequstWithAceesToken(String url ,WeiboParameters params) throws IOException {
        params.put("access_token", mAccessToken);
        return doGetRequst(url,params);
    }

    protected String doGetRequst(String url ,WeiboParameters param) throws IOException {
        String send=param.encode();
         url=url+"?"+send;
        return doGetRequst(url);
    }

    protected String doGetRequst(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();


        if (response.isSuccessful()) {

            String result=response.body().string();
            if (DEBUG) {
                Log.e(TAG, result);
            }
            return result;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public static String getAccessToken() {
        return mAccessToken;
    }

    public static void setAccessToken(String token) {
        mAccessToken = token;
    }
}
