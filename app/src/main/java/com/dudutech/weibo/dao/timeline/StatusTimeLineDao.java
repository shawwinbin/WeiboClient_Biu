/* 
 * Copyright (C) 2014 Peter Cai
 *
 * This file is part of BlackLight
 *
 * BlackLight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BlackLight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BlackLight.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dudutech.weibo.dao.timeline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dudutech.weibo.api.BilateralTimeLineApi;
import com.dudutech.weibo.api.HomeTimeLineApi;
import com.dudutech.weibo.db.DataBaseHelper;
import com.dudutech.weibo.db.tables.HomeTimeLineTable;
import com.dudutech.weibo.model.BaseListModel;
import com.dudutech.weibo.model.MessageListModel;
import com.google.gson.Gson;

import com.dudutech.weibo.global.Constants;



/* Time Line of me and my friends */
public class StatusTimeLineDao  extends  BaseTimelineDao <MessageListModel>
{
	private static final String BILATERAL = "bilateral";
	protected DataBaseHelper mHelper;
	private Context mContext;

	public Constants.LOADING_STATUS mStatus;
	public StatusTimeLineDao(Context context) {
		mHelper = DataBaseHelper.instance(context);
		mContext = context;
		mStatus= Constants.LOADING_STATUS.NORMAL;
	}

	@Override
	public void cache() {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.beginTransaction();
		db.execSQL(Constants.SQL_DROP_TABLE + HomeTimeLineTable.NAME);
		db.execSQL(HomeTimeLineTable.CREATE);
		ContentValues values = new ContentValues();
		values.put(HomeTimeLineTable.ID, 1);
		values.put(HomeTimeLineTable.JSON, new Gson().toJson(mListModel));
		db.insert(HomeTimeLineTable.NAME, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	@Override
	public Cursor query() {
		return mHelper.getReadableDatabase().query(HomeTimeLineTable.NAME, null, null, null, null, null, null);
	}


	@Override
	public void spanAll(MessageListModel messageListModel) {
		messageListModel.spanAll(mContext);
	}
	protected MessageListModel load(String groupId) {
		if (groupId == null) {
			return load();
		} else if (groupId == BILATERAL) {
			return BilateralTimeLineApi.fetchBilateralTimeLine(Constants.HOME_TIMELINE_PAGE_SIZE, ++mCurrentPage);
		}
        else {
//			return GroupsApi.fetchGroupTimeLine(groupId, Constants.HOME_TIMELINE_PAGE_SIZE, ++mCurrentPage);
            return null;
		}
	}
     @Override
	public MessageListModel load() {
		MessageListModel model=HomeTimeLineApi.fetchHomeTimeLine(Constants.HOME_TIMELINE_PAGE_SIZE, ++mCurrentPage);
		return model;
	}

	@Override
	protected Class<? extends MessageListModel> getListClass() {
		return MessageListModel.class ;
	}

}
