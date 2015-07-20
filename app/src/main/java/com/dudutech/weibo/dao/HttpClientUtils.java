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
public class HttpClientUtils {
    private static final String TAG = HttpClientUtils.class.getSimpleName();
    private final static  OkHttpClient client = new OkHttpClient();
    // Access Token
    private static String mAccessToken;
    public static String doGetRequstWithAceesToken(String url, WeiboParameters params) throws IOException {
        params.put("access_token", mAccessToken);
        return doGetRequst(url,params);
    }

    public  static String doGetRequst(String url ,WeiboParameters param) throws IOException {
        String send=param.encode();
         url=url+"?"+send;
        return  doGetRequst(url);
    }

    public static String doGetRequst(String url) throws IOException {

        if (DEBUG) {
            Log.i(TAG, url);
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();


        if (response.isSuccessful()) {

            String result=response.body().string();
            if (DEBUG) {
                Log.i(TAG, result);
            }
            return result;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }


    public static String doPostRequst(String url,WeiboParameters params) throws IOException {

        if (DEBUG) {
            Log.i(TAG, url);
        }
        String send=params.encode();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();


        if (response.isSuccessful()) {

            String result=response.body().string();
            if (DEBUG) {
                Log.i(TAG, result);
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
