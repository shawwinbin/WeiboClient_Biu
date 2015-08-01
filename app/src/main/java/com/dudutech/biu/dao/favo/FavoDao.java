package com.dudutech.biu.dao.favo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dudutech.biu.R;
import com.dudutech.biu.dao.UrlConstants;
import com.dudutech.biu.dao.HttpClientUtils;
import com.dudutech.biu.model.FavoModel;
import com.dudutech.biu.dao.WeiboParameters;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

/**
 * Created by shaw on 2015/7/28.
 */
public class FavoDao {

    public  final String TAG = FavoDao.class.getSimpleName();
    public long id;
    private Context mContext;

    public FavoDao(long id,Context context){
        this.id=id;
        mContext=context;
    }

    // Add to favorite
    public  void favo() {
       executeTask(UrlConstants.FAVORITES_CREATE);
    }

    // Remove from favorite
    public void unfav(long id) {

        executeTask(UrlConstants.FAVORITES_DESTROY);
    }

    private void executeTask(final String url) {
        AsyncTask<Void, Void, Boolean> task=new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                WeiboParameters param=new WeiboParameters();
                param.put("id", id);
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
                if (result) {
                    Toast.makeText(mContext, R.string.success, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, R.string.fail, Toast.LENGTH_LONG).show();
                }
            }
        };
        task.execute();

    }

}
