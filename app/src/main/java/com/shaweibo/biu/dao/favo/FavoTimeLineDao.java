package com.shaweibo.biu.dao.favo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shaweibo.biu.dao.UrlConstants;
import com.shaweibo.biu.dao.HttpClientUtils;
import com.shaweibo.biu.dao.timeline.StatusTimeLineDao;
import com.shaweibo.biu.db.tables.FavListTable;
import com.shaweibo.biu.global.Constants;
import com.shaweibo.biu.model.FavoListModel;
import com.shaweibo.biu.model.MessageListModel;
import com.shaweibo.biu.dao.WeiboParameters;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by shaw on 2015/7/23.
 */
public class FavoTimeLineDao extends StatusTimeLineDao {
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
