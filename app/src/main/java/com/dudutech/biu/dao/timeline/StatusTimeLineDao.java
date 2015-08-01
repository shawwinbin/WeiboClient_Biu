
package com.dudutech.biu.dao.timeline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dudutech.biu.dao.UrlConstants;
import com.dudutech.biu.dao.HttpClientUtils;
import com.dudutech.biu.db.DataBaseHelper;
import com.dudutech.biu.db.tables.HomeTimeLineTable;
import com.dudutech.biu.model.MessageListModel;
import com.dudutech.biu.dao.WeiboParameters;
import com.google.gson.Gson;

import com.dudutech.biu.global.Constants;

import static com.dudutech.biu.BuildConfig.DEBUG;


/* Time Line of me and my friends */
public class StatusTimeLineDao  extends  BaseTimelineDao <MessageListModel>
{

	private static final String TAG = StatusTimeLineDao.class.getSimpleName();
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
		db.delete(HomeTimeLineTable.NAME, HomeTimeLineTable.GROUP_ID + " =? ", new String[]{mGroupId});
		ContentValues values = new ContentValues();
		values.put(HomeTimeLineTable.GROUP_ID, mGroupId);
		values.put(HomeTimeLineTable.JSON, new Gson().toJson(mListModel));
		db.insert(HomeTimeLineTable.NAME, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	@Override
	public Cursor query() {
		return mHelper.getReadableDatabase().query(HomeTimeLineTable.NAME, null, HomeTimeLineTable.GROUP_ID+ " = ?", new String[]{mGroupId}, null, null, null);
	}


	@Override
	public void spanAll(MessageListModel messageListModel) {
		messageListModel.spanAll(mContext);
	}

     @Override
	public MessageListModel load() {
		 MessageListModel model;
		 if(mGroupId.equals(GROUP_ALL)){
			 model =getHomeTimeLine(Constants.HOME_TIMELINE_PAGE_SIZE, ++mCurrentPage);
		 }
		 else if(mGroupId.equals(GROUP_BILATERAL)){
			 model= getBilateralTimeLine(Constants.HOME_TIMELINE_PAGE_SIZE, ++mCurrentPage);
		 }
		 else{
			 model= getGroupTimelines(mGroupId, Constants.HOME_TIMELINE_PAGE_SIZE, ++mCurrentPage);
		 }

		return model;
	}

	@Override
	protected Class<? extends MessageListModel> getListClass() {
		return MessageListModel.class ;
	}


	public static MessageListModel getHomeTimeLine(int count, int page) {
		WeiboParameters params = new WeiboParameters();
		params.put("count", count);
		params.put("page", page);

		try {
			String json = HttpClientUtils.doGetRequstWithAceesToken(UrlConstants.HOME_TIMELINE, params);
			return new Gson().fromJson(json, MessageListModel.class);
		} catch (Exception e) {
			if (DEBUG) {
				Log.d(TAG, "Can't get  home timeline, " + e.getClass().getSimpleName());
				Log.d(TAG, Log.getStackTraceString(e));
			}
			return null;
		}
	}

	public static MessageListModel getGroupTimelines(String groupId, int count, int page) {
		WeiboParameters params = new WeiboParameters();
		params.put("list_id", groupId);
		params.put("count", count);
		params.put("page", page);

		try {
			String json = HttpClientUtils.doGetRequstWithAceesToken(UrlConstants.FRIENDSHIPS_GROUPS_TIMELINE, params);
			return new Gson().fromJson(json, MessageListModel.class);
		} catch (Exception e) {
			if (DEBUG) {
				Log.e(TAG, "Cannot get group timeline");
				Log.e(TAG, Log.getStackTraceString(e));
			}
		}

		return null;
	}
	public static MessageListModel getBilateralTimeLine(int count, int page) {
		WeiboParameters params = new WeiboParameters();
		params.put("count", count);
		params.put("page", page);

		try {
			String json = HttpClientUtils.doGetRequstWithAceesToken(UrlConstants.BILATERAL_TIMELINE, params);
			return new Gson().fromJson(json, MessageListModel.class);
		} catch (Exception e) {
			if (DEBUG) {
				Log.d(TAG, "Cannot get bilateral timeline, " + e.getClass().getSimpleName());
				Log.d(TAG, Log.getStackTraceString(e));
			}
			return null;
		}
	}

}
