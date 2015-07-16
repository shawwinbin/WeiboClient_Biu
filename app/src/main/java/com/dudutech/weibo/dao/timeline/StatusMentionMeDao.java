/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.dao.timeline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dudutech.weibo.api.RepostTimeLineApi;
import com.dudutech.weibo.api.StatusMentiomMeApi;
import com.dudutech.weibo.db.tables.MentionsTimeLineTable;
import com.dudutech.weibo.db.tables.RepostTimeLineTable;
import com.dudutech.weibo.global.Constants;
import com.dudutech.weibo.model.MessageListModel;
import com.dudutech.weibo.model.RepostListModel;
import com.google.gson.Gson;

/**
 * Created by shaw on 2015/7/13.
 */
public class StatusMentionMeDao extends  StatusTimeLineDao{

    public  StatusMentionMeDao(Context context){
        super(context);
    }
    @Override
    public void cache() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.beginTransaction();
        db.execSQL(Constants.SQL_DROP_TABLE + MentionsTimeLineTable.NAME);
        db.execSQL(MentionsTimeLineTable.CREATE);
        ContentValues values = new ContentValues();
        values.put(MentionsTimeLineTable.ID, 1);
        values.put(MentionsTimeLineTable.JSON, new Gson().toJson(mListModel));
        db.insert(MentionsTimeLineTable.NAME, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public Cursor query() {
        return mHelper.getReadableDatabase().query(MentionsTimeLineTable.NAME, null, null, null, null, null, null);
    }

    @Override
    public MessageListModel load() {
        return  StatusMentiomMeApi.fetchMentionsTimeLine(Constants.HOME_TIMELINE_PAGE_SIZE, ++mCurrentPage);
    }



}
