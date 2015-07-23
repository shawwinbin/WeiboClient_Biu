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

import com.dudutech.biu.api.UrlConstants;
import com.dudutech.biu.dao.HttpClientUtils;
import com.dudutech.biu.dao.timeline.BaseTimelineDao;
import com.dudutech.biu.db.DataBaseHelper;
import com.dudutech.biu.db.tables.CommentsToMeTable;
import com.dudutech.biu.global.Constants;
import com.dudutech.biu.model.CommentListModel;
import com.dudutech.biu.network.WeiboParameters;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by Administrator on 2015-7-16.
 */
public class CommentsToMeDao   extends BaseTimelineDao<CommentListModel> {
    private Context mContext;
    protected DataBaseHelper mHelper;
    public CommentsToMeDao(Context context) {
        mContext=context;
        mHelper = DataBaseHelper.instance(context);
    }

    @Override
    public void spanAll(CommentListModel listModel) {
        listModel.spanAll(mContext);
    }

    @Override
    public CommentListModel load() {
        WeiboParameters params = new WeiboParameters();
        params.put("count",Constants.HOME_TIMELINE_PAGE_SIZE);
        params.put("page", ++mCurrentPage);
        try {
            String result= HttpClientUtils.doGetRequstWithAceesToken(UrlConstants.COMMENTS_TO_ME_TIMELINE,params);
            CommentListModel model=new Gson().fromJson(result, CommentListModel.class);
            return model;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected Class<? extends CommentListModel> getListClass() {
        return CommentListModel.class;
    }

    @Override
    public void cache() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.beginTransaction();
        db.execSQL(Constants.SQL_DROP_TABLE + CommentsToMeTable.NAME);
        db.execSQL(CommentsToMeTable.CREATE);
        db.delete(CommentsToMeTable.NAME,null,null);
        ContentValues values = new ContentValues();
        values.put(CommentsToMeTable.JSON, new Gson().toJson( mListModel));
        db.insert(CommentsToMeTable.NAME, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public Cursor query() {
        return mHelper.getReadableDatabase().query(CommentsToMeTable.NAME, null, null, null, null, null, null);
    }

}
