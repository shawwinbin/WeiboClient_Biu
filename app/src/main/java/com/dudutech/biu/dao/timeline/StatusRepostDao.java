/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.biu.dao.timeline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dudutech.biu.api.RepostTimeLineApi;
import com.dudutech.biu.db.tables.RepostTimeLineTable;
import com.dudutech.biu.global.Constants;
import com.dudutech.biu.model.RepostListModel;
import com.google.gson.Gson;

/**
 * Created by shaw on 2015/7/13.
 */
public class StatusRepostDao extends  StatusTimeLineDao{

    private long mId;
    public StatusRepostDao(Context context, long id) {
        super(context,"");
        mId=id;
    }

    @Override
    public void cache() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.beginTransaction();
        db.delete(RepostTimeLineTable.NAME, RepostTimeLineTable.MSGID + "=?", new String[]{String.valueOf(mId)});
        ContentValues values = new ContentValues();
        values.put(RepostTimeLineTable.MSGID, mId);
        values.put(RepostTimeLineTable.JSON, new Gson().toJson((RepostListModel) mListModel));
        db.insert(RepostTimeLineTable.NAME, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public Cursor query() {
        return mHelper.getReadableDatabase().query(RepostTimeLineTable.NAME, new String[]{
                RepostTimeLineTable.MSGID,
                RepostTimeLineTable.JSON
        }, RepostTimeLineTable.MSGID + "=?", new String[]{String.valueOf(mId)}, null, null, null);
    }

    @Override
    public RepostListModel load() {
        return RepostTimeLineApi.fetchRepostTimeLine(mId, Constants.HOME_TIMELINE_PAGE_SIZE, ++mCurrentPage);
    }

    @Override
    protected Class<? extends RepostListModel> getListClass() {
        return RepostListModel.class ;
    }

}
