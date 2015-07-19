/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.dao.comments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dudutech.weibo.api.CommentMentionMeApi;
import com.dudutech.weibo.db.tables.CommentMentionsTimeLineTable;
import com.dudutech.weibo.global.Constants;
import com.dudutech.weibo.model.CommentListModel;
import com.dudutech.weibo.network.WeiboParameters;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2015-7-16.
 */
public class CommentsMentionMeDao extends StatusCommentDao {


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

        CommentListModel model =  CommentMentionMeApi.fetchCommentMentionsTimeLine(Constants.HOME_TIMELINE_PAGE_SIZE, ++mCurrentPage);
        return model;
    }
}
