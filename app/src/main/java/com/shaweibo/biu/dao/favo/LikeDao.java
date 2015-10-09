package com.shaweibo.biu.dao.favo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.shaweibo.biu.dao.UrlConstants;
import com.shaweibo.biu.dao.HttpClientUtils;
import com.shaweibo.biu.model.FavoModel;
import com.shaweibo.biu.dao.WeiboParameters;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

/**
 * Created by shaw on 2015/7/28.
 */
public class LikeDao {

    public  final String TAG = LikeDao.class.getSimpleName();
    public long id;
    private Context mContext;

    public LikeDao(long id, Context context){
        this.id=id;
        mContext=context;
    }

    // Add to favorite
    public  void like() {
       executeTask(UrlConstants.ATTITUDE_CREATE);
    }

    // Remove from favorite
    public void unLike() {

        executeTask(UrlConstants.ATTITUDE_DESTROY);
    }

    private void executeTask(final String url) {
        AsyncTask<Void, Void, Boolean> task=new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                WeiboParameters param=new WeiboParameters();
                param.put("id", id);
                if(url.equals(UrlConstants.ATTITUDE_CREATE)){
                    param.put("attitude","smile");
                }
                try {
                    String jsonData = HttpClientUtils.doPostRequstWithWithAceesToken(url,param);
                    FavoModel value = new Gson().fromJson(jsonData, FavoModel.class);
                    if (value != null) {
                        return true;
                    }
                } catch (JsonSyntaxException e) {

                    Log.e(TAG,e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);

            }
        };
        task.execute();

    }

}
