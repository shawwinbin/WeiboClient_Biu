package com.dudutech.weibo.dao.timeline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dudutech.weibo.api.UrlConstants;
import com.dudutech.weibo.dao.HttpClientUtils;
import com.dudutech.weibo.db.tables.FavListTable;
import com.dudutech.weibo.global.Constants;
import com.dudutech.weibo.model.FavoListModel;
import com.dudutech.weibo.model.MessageListModel;
import com.dudutech.weibo.network.WeiboParameters;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by shaw on 2015/7/23.
 */
public class FavoTimeLineDao extends  StatusTimeLineDao {
    public FavoTimeLineDao(Context context) {
        super(context, "");
    }


    @Override
    public void cache() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.beginTransaction();
        db.execSQL(Constants.SQL_DROP_TABLE + FavListTable.NAME);
        db.execSQL(FavListTable.CREATE);
        ContentValues values = new ContentValues();
        values.put(FavListTable.ID, 1);
        values.put(FavListTable.JSON, new Gson().toJson(mListModel));
        db.insert(FavListTable.NAME, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public Cursor query() {
        return mHelper.getReadableDatabase().query(FavListTable.NAME, null, null, null, null, null, null);
    }

    @Override
    public MessageListModel load() {

        WeiboParameters params = new WeiboParameters();
        params.put("count", Constants.HOME_TIMELINE_PAGE_SIZE);
        params.put("page", ++mCurrentPage);
        MessageListModel listModel=null;
        try {
            String jsonStr= HttpClientUtils.doGetRequstWithAceesToken(UrlConstants.FAVORITES_LIST, params);
            listModel = new Gson().fromJson(jsonStr,FavoListModel.class).toMsgList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  listModel;
    }
}
