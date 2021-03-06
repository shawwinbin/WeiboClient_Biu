/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.shaweibo.biu.dao.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import com.shaweibo.biu.R;
import com.shaweibo.biu.Utils.Utility;
import com.shaweibo.biu.dao.UrlConstants;
import com.shaweibo.biu.dao.HttpClientUtils;
import com.shaweibo.biu.db.DataBaseHelper;
import com.shaweibo.biu.db.tables.UsersTable;
import com.shaweibo.biu.global.Constants;
import com.shaweibo.biu.model.UserModel;
import com.shaweibo.biu.dao.WeiboParameters;
import com.google.gson.Gson;

import static com.shaweibo.biu.BuildConfig.DEBUG;


public class UserDao
{
	private static String TAG = UserDao.class.getSimpleName();
	
	private static BitmapDrawable[] mVipDrawable;




	public UserDao(Context context) {
		mHelper = DataBaseHelper.instance(context);

		if (mVipDrawable == null) {
			mVipDrawable = new BitmapDrawable[]{
					(BitmapDrawable) context.getResources().getDrawable(R.mipmap.ic_personal_vip),
					(BitmapDrawable) context.getResources().getDrawable(R.mipmap.ic_enterprise_vip)
			};
		}
	}

	private DataBaseHelper mHelper;
	public UserModel getUser(String uid) {
		UserModel model;
		
		model = getUserFromApi(uid);

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
		model = getUserByNameFromAPi(name);
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


	public static UserModel getUserFromApi(String uid) {
		WeiboParameters params = new WeiboParameters();
		params.put("uid", uid);
		try {
			String json = HttpClientUtils.doGetRequstWithAceesToken(UrlConstants.USER_SHOW, params);
			UserModel user = new Gson().fromJson(json.replaceAll("-Weibo", ""), UserModel.class);
			return user;
		} catch (Exception e) {
			if (DEBUG) {
				Log.e(TAG, "Failed to fetch user info from net: " + e.getClass().getSimpleName());
			}
			return null;
		}
	}

	public static UserModel getUserByNameFromAPi(String name) {
		WeiboParameters params = new WeiboParameters();
		params.put("screen_name", name);

		try {
			String json = HttpClientUtils.doGetRequstWithAceesToken(UrlConstants.USER_SHOW, params);
			UserModel user = new Gson().fromJson(json.replaceAll("-Weibo", ""), UserModel.class);
			return user;
		} catch (Exception e) {
			if (DEBUG) {
				Log.e(TAG, "Failed to fetch user info from net: " + e.getClass().getSimpleName());
			}
			return null;
		}
	}

}
