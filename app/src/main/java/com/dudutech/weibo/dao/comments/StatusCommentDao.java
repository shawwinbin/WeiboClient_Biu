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

import com.dudutech.weibo.api.WeiboCommentApi;
import com.dudutech.weibo.dao.timeline.BaseTimelineDao;
import com.dudutech.weibo.db.DataBaseHelper;
import com.dudutech.weibo.db.tables.StatusCommentTable;
import com.dudutech.weibo.global.Constants;
import com.dudutech.weibo.model.BaseListModel;
import com.dudutech.weibo.model.CommentListModel;
import com.dudutech.weibo.model.CommentModel;
import com.dudutech.weibo.model.MessageListModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class StatusCommentDao extends BaseTimelineDao<CommentListModel>
{
	private long mId;
	private Context mContext;
	protected DataBaseHelper mHelper;

	public StatusCommentDao(Context context, long id) {
		mContext=context;
		mId = id;
		mHelper = DataBaseHelper.instance(context);
	}

	@Override
	public void spanAll(CommentListModel listModel) {
		listModel.spanAll(mContext);
	}

	public void cache() {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.beginTransaction();
		db.delete(StatusCommentTable.NAME, StatusCommentTable.MSGID + "=?", new String[]{String.valueOf(mId)});
		ContentValues values = new ContentValues();
		values.put(StatusCommentTable.MSGID, mId);
		values.put(StatusCommentTable.JSON, new Gson().toJson(mListModel));
		db.insert(StatusCommentTable.NAME, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
	}


	public Cursor query() {
		return mHelper.getReadableDatabase().query(StatusCommentTable.NAME, new String[]{
			StatusCommentTable.MSGID,
			StatusCommentTable.JSON
		}, StatusCommentTable.MSGID + "=?", new String[]{String.valueOf(mId)}, null, null, null);
	}


	@Override
	public CommentListModel load() {
		CommentListModel model =WeiboCommentApi.fetchCommentOfStatus(mId, Constants.HOME_TIMELINE_PAGE_SIZE, ++mCurrentPage);
		return model;
	}
	@Override
	protected Class<? extends CommentListModel> getListClass() {
	return CommentListModel.class;
}
}
