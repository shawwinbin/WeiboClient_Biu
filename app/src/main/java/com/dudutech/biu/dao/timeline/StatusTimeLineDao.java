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

package com.dudutech.biu.dao.timeline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dudutech.biu.api.BilateralTimeLineApi;
import com.dudutech.biu.api.GroupsApi;
import com.dudutech.biu.api.HomeTimeLineApi;
import com.dudutech.biu.db.DataBaseHelper;
import com.dudutech.biu.db.tables.HomeTimeLineTable;
import com.dudutech.biu.model.MessageListModel;
import com.google.gson.Gson;

import com.dudutech.biu.global.Constants;



/* Time Line of me and my friends */
public class StatusTimeLineDao  extends  BaseTimelineDao <MessageListModel>
{
	public static final String GROUP_BILATERAL = "groups_bilateral";
	public static final String GROUP_ALL = "groups_all";
	protected DataBaseHelper mHelper;
	private Context mContext;
	public String mGroupId;

	public Constants.LOADING_STATUS mStatus;
	public StatusTimeLineDao(Context context,String groupId) {
		mHelper = DataBaseHelper.instance(context);
		mContext = context;
		mStatus= Constants.LOADING_STATUS.NORMAL;
		mGroupId=groupId;
	}

	@Override
	public void cache() {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.beginTransaction();
		db.delete(HomeTimeLineTable.NAME, HomeTimeLineTable.ID + "=?", new String[]{mGroupId});
		ContentValues values = new ContentValues();
		values.put(HomeTimeLineTable.GROUP_ID, mGroupId);
		values.put(HomeTimeLineTable.JSON, new Gson().toJson(mListModel));
		db.insert(HomeTimeLineTable.NAME, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	@Override
	public Cursor query() {
		return mHelper.getReadableDatabase().query(HomeTimeLineTable.NAME, null, HomeTimeLineTable.GROUP_ID+ "=?", new String[]{mGroupId}, null, null, null);
	}


	@Override
	public void spanAll(MessageListModel messageListModel) {
		messageListModel.spanAll(mContext);
	}

     @Override
	public MessageListModel load() {
		 MessageListModel model;
		 if(mGroupId.equals(GROUP_ALL)){
			 model =HomeTimeLineApi.fetchHomeTimeLine(Constants.HOME_TIMELINE_PAGE_SIZE, ++mCurrentPage);
		 }
		 else if(mGroupId.equals(GROUP_BILATERAL)){
			 model= BilateralTimeLineApi.fetchBilateralTimeLine(Constants.HOME_TIMELINE_PAGE_SIZE, ++mCurrentPage);
		 }
		 else{
			 model= GroupsApi.fetchGroupTimeLine(mGroupId, Constants.HOME_TIMELINE_PAGE_SIZE, ++mCurrentPage);
		 }

		return model;
	}

	@Override
	protected Class<? extends MessageListModel> getListClass() {
		return MessageListModel.class ;
	}

}
