package com.dudutech.weibo.dao.relationship;

import android.database.Cursor;
import android.util.Log;

import com.dudutech.weibo.api.Constants;
import com.dudutech.weibo.dao.BaseDao;
import com.dudutech.weibo.model.GroupListModel;
import com.dudutech.weibo.network.WeiboParameters;
import com.google.gson.Gson;

import org.json.JSONObject;

import static com.dudutech.weibo.BuildConfig.DEBUG;

/**
 * Created by Administrator on 2015-7-17.
 */
public class GroupDao extends BaseDao {

    public GroupListModel mModel=new GroupListModel();

    private static final String TAG = GroupDao.class.getSimpleName();
    public  GroupListModel getGroups() {
        WeiboParameters params = new WeiboParameters();

        try {
            String result = doGetRequstWithAceesToken(Constants.FRIENDSHIPS_GROUPS, params);
            mModel=new Gson().fromJson(result.toString(), GroupListModel.class);
            return mModel;
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Cannot get groups");
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
        return null;
    }



}
