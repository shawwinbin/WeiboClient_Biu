package com.shaweibo.biu.dao.relationship;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.shaweibo.biu.dao.UrlConstants;
import com.shaweibo.biu.dao.HttpClientUtils;
import com.shaweibo.biu.dao.timeline.ITimelineBaseDao;
import com.shaweibo.biu.db.DataBaseHelper;
import com.shaweibo.biu.db.tables.GroupTable;
import com.shaweibo.biu.global.Constants;
import com.shaweibo.biu.model.BaseListModel;
import com.shaweibo.biu.model.GroupListModel;
import com.shaweibo.biu.dao.WeiboParameters;
import com.google.gson.Gson;

import static com.shaweibo.biu.BuildConfig.DEBUG;

/**
 * Created by Administrator on 2015-7-17.
 */
public class GroupDao  implements ITimelineBaseDao{

    public GroupListModel mListModel=new GroupListModel();
    protected DataBaseHelper mHelper;
    private static final String TAG = GroupDao.class.getSimpleName();
    private Context mContext;

    public GroupDao(Context context){
        mContext=context;
        mHelper = DataBaseHelper.instance(context);

    }


    public  GroupListModel getGroups() {
        WeiboParameters params = new WeiboParameters();

        try {
            String result = HttpClientUtils.doGetRequstWithAceesToken(UrlConstants.FRIENDSHIPS_GROUPS, params);
            GroupListModel  groups=new Gson().fromJson(result.toString(), GroupListModel.class);
            return groups;
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Cannot get groups");
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
        return null;
    }


    @Override
    public void loadFromCache() {
        Cursor cursor = query();
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            GroupListModel listModel = (GroupListModel) new Gson().fromJson(cursor.getString(1),getListClass());
            mListModel.getList().clear();
            mListModel.addAll(false,listModel);
            mListModel.addDefaultGroupsToTop();
        } else {
            try {
                mListModel = (GroupListModel) getListClass().newInstance();
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void cache() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.beginTransaction();
        db.execSQL(Constants.SQL_DROP_TABLE + GroupTable.NAME);
        db.execSQL(GroupTable.CREATE);
        ContentValues values = new ContentValues();
        values.put(GroupTable.ID, 1);
        values.put(GroupTable.JSON, new Gson().toJson(mListModel));
        db.insert(GroupTable.NAME, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void load(boolean isRefresh) {

       GroupListModel  listModel=getGroups();
        if(listModel==null)
            return;

        mListModel.getList().clear();

        mListModel.addAll(false,listModel);

    }

    @Override
    public Cursor query() {
        return mHelper.getReadableDatabase().query(GroupTable.NAME, null, null, null, null, null, null);
    }

    @Override
    public BaseListModel getList() {
        return null;
    }

    @Override
    public com.shaweibo.biu.global.Constants.LOADING_STATUS getStatus() {
        return null;
    }


    protected Class<? extends GroupListModel> getListClass() {
        return GroupListModel.class;
    }
}
