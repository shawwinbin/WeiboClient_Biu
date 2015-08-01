/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.biu.dao.comments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dudutech.biu.dao.UrlConstants;
import com.dudutech.biu.dao.HttpClientUtils;
import com.dudutech.biu.db.tables.CommentMentionsTimeLineTable;
import com.dudutech.biu.global.Constants;
import com.dudutech.biu.model.CommentListModel;
import com.dudutech.biu.dao.WeiboParameters;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by Administrator on 2015-7-16.
 */
public class CommentsMentionMeDao extends StatusCommentDao {
    private static final String TAG = CommentsMentionMeDao.class.getSimpleName();

    public CommentsMentionMeDao(Context context) {
        super(context, 0);
    }

    @Override
    public void cache() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.beginTransaction();
        db.execSQL(Constants.SQL_DROP_TABLE + CommentMentionsTimeLineTable.NAME);
        db.execSQL(CommentMentionsTimeLineTable.CREATE);
        ContentValues values = new ContentValues();
        values.put(CommentMentionsTimeLineTable.ID, 1);
        values.put(CommentMentionsTimeLineTable.JSON, new Gson().toJson( mListModel));
        db.insert(CommentMentionsTimeLineTable.NAME, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public Cursor query() {
        return mHelper.getReadableDatabase().query(CommentMentionsTimeLineTable.NAME, null, null, null, null, null, null);
    }

    @Override
    public CommentListModel load() {
        WeiboParameters params = new WeiboParameters();


        params.put("count", Constants.HOME_TIMELINE_PAGE_SIZE);
        params.put("page",  ++mCurrentPage);
        CommentListModel listModel=null;
        try {
            String jsonStr= HttpClientUtils.doGetRequstWithAceesToken(UrlConstants.COMMENTS_MENTIONS, params);
            listModel = new Gson().fromJson(jsonStr,CommentListModel.class);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Cannot fetch mentions timeline, " + e.getClass().getSimpleName());
        }


        return listModel;
    }
}
