/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.shaweibo.biu.dao.comments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.shaweibo.biu.dao.UrlConstants;
import com.shaweibo.biu.dao.HttpClientUtils;
import com.shaweibo.biu.dao.timeline.BaseTimelineDao;
import com.shaweibo.biu.db.DataBaseHelper;
import com.shaweibo.biu.db.tables.StatusCommentTable;
import com.shaweibo.biu.global.Constants;
import com.shaweibo.biu.model.CommentListModel;
import com.shaweibo.biu.dao.WeiboParameters;
import com.google.gson.Gson;

import static com.shaweibo.biu.BuildConfig.DEBUG;

public class StatusCommentDao extends BaseTimelineDao<CommentListModel>
{
	private static String TAG = StatusCommentDao.class.getSimpleName();
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
		WeiboParameters params = new WeiboParameters();
		params.put("id", mId);
		params.put("count",  Constants.HOME_TIMELINE_PAGE_SIZE);
		params.put("page",  ++mCurrentPage);

		try {
			String json = HttpClientUtils.doGetRequstWithAceesToken(UrlConstants.COMMENTS_SHOW, params);

			return new Gson().fromJson(json, CommentListModel.class);
		} catch (Exception e) {
			if (DEBUG) {
				Log.d(TAG, "Cannot fetch comments timeline, " + e.getClass().getSimpleName());
			}
			return null;
		}

	}
	@Override
	protected Class<? extends CommentListModel> getListClass() {
	return CommentListModel.class;
}
}
