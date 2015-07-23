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

import com.dudutech.biu.api.UserTimeLineApi;
import com.dudutech.biu.db.tables.UserTimeLineTable;
import com.dudutech.biu.global.Constants;
import com.dudutech.biu.model.MessageListModel;
import com.google.gson.Gson;



/* Cache api for exact user timeline */
public class FriendTimeLineDao extends StatusTimeLineDao
{
	private String mUid;
	
	public FriendTimeLineDao(Context context, String uid) {
		super(context,"");
		mUid = uid;
	}

	@Override
	public void cache() {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.beginTransaction();
		db.delete(UserTimeLineTable.NAME, UserTimeLineTable.UID + "=?", new String[]{mUid});
		ContentValues values = new ContentValues();
		values.put(UserTimeLineTable.UID, mUid);
		values.put(UserTimeLineTable.JSON, new Gson().toJson(mListModel));
		db.insert(UserTimeLineTable.NAME, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
		
	}

	@Override
	public Cursor query() {
		return mHelper.getReadableDatabase().query(UserTimeLineTable.NAME, new String[]{
			UserTimeLineTable.UID,
			UserTimeLineTable.JSON
		}, UserTimeLineTable.UID + "=?", new String[]{mUid}, null, null, null);
	}

	@Override
	public MessageListModel load() {
		MessageListModel model= UserTimeLineApi.fetchUserTimeLine(mUid, Constants.HOME_TIMELINE_PAGE_SIZE, ++mCurrentPage);
		return model;
	}
}
