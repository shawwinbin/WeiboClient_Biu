/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.dao.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import com.dudutech.weibo.R;
import com.dudutech.weibo.Utils.Utility;
import com.dudutech.weibo.api.UserApi;
import com.dudutech.weibo.db.DataBaseHelper;
import com.dudutech.weibo.db.tables.UsersTable;
import com.dudutech.weibo.global.Constants;
import com.dudutech.weibo.model.UserModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import static com.dudutech.weibo.BuildConfig.DEBUG;


public class UserDao
{
	private static String TAG = UserDao.class.getSimpleName();
	
	private static BitmapDrawable[] mVipDrawable;




	public UserDao(Context context) {
		mHelper = DataBaseHelper.instance(context);

		if (mVipDrawable == null) {
			mVipDrawable = new BitmapDrawable[]{
					(BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_personal_vip),
					(BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_enterprise_vip)
			};
		}
	}

	private DataBaseHelper mHelper;
	public UserModel getUser(String uid) {
		UserModel model;
		
		model = UserApi.getUser(uid);

		if (model == null) {
			Cursor cursor = mHelper.getReadableDatabase().query(UsersTable.NAME, new String[] {
				UsersTable.UID,
				UsersTable.TIMESTAMP,
				UsersTable.USERNAME,
				UsersTable.JSON
			}, UsersTable.UID + "=?", new String[]{uid}, null, null, null);

			if (cursor.getCount() >= 1) {
				cursor.moveToFirst();

				long time = cursor.getLong(cursor.getColumnIndex(UsersTable.TIMESTAMP));

				if (DEBUG) {
					Log.d(TAG, "time = " + time);
					Log.d(TAG, "available = " + Utility.isCacheAvailable(time, Constants.DB_CACHE_DAYS));
				}

				if (Utility.isCacheAvailable(time, Constants.DB_CACHE_DAYS)) {
					model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(UsersTable.JSON)), UserModel.class);
					model.timestamp = cursor.getInt(cursor.getColumnIndex(UsersTable.TIMESTAMP));
				}
			}
		} else {
			
			// Insert into database
			ContentValues values = new ContentValues();
			values.put(UsersTable.UID, uid);
			values.put(UsersTable.TIMESTAMP, model.timestamp);
			values.put(UsersTable.USERNAME, model.getName());
			values.put(UsersTable.JSON, new Gson().toJson(model));

			SQLiteDatabase db = mHelper.getWritableDatabase();
			db.beginTransaction();
			db.delete(UsersTable.NAME, UsersTable.UID + "=?", new String[]{uid});
			db.insert(UsersTable.NAME, null, values);
			db.setTransactionSuccessful();
			db.endTransaction();

		}

		return model;
	}

	public UserModel getUserByName(String name) {
		UserModel model;
		model = UserApi.getUserByName(name);
		if (model == null) {
			Cursor cursor = mHelper.getReadableDatabase().query(UsersTable.NAME, new String[] {
				UsersTable.UID,
				UsersTable.TIMESTAMP,
				UsersTable.USERNAME,
				UsersTable.JSON
			}, UsersTable.USERNAME + "=?", new String[]{name}, null, null, null);

			if (cursor.getCount() >= 1) {
				cursor.moveToFirst();

				long time = cursor.getLong(cursor.getColumnIndex(UsersTable.TIMESTAMP));

				if (DEBUG) {
					Log.d(TAG, "time = " + time);
					Log.d(TAG, "available = " + Utility.isCacheAvailable(time, Constants.DB_CACHE_DAYS));
				}

				if (Utility.isCacheAvailable(time, Constants.DB_CACHE_DAYS)) {
					model = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(UsersTable.JSON)), UserModel.class);
					model.timestamp = cursor.getInt(cursor.getColumnIndex(UsersTable.TIMESTAMP));
				}
			}
		} else {

			// Insert into database
			ContentValues values = new ContentValues();
			values.put(UsersTable.UID, model.id);
			values.put(UsersTable.TIMESTAMP, model.timestamp);
			values.put(UsersTable.USERNAME, name);
			values.put(UsersTable.JSON, new Gson().toJson(model));

			SQLiteDatabase db = mHelper.getWritableDatabase();
			db.beginTransaction();
			db.delete(UsersTable.NAME, UsersTable.USERNAME + "=?", new String[]{name});
			db.insert(UsersTable.NAME, null, values);
			db.setTransactionSuccessful();
			db.endTransaction();

		}
		
		return model;
	}

	
}
