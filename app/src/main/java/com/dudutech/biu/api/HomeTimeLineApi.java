

package com.dudutech.biu.api;

import android.util.Log;

import com.dudutech.biu.model.MessageListModel;
import com.dudutech.biu.network.WeiboParameters;
import com.google.gson.Gson;
import static com.dudutech.biu.BuildConfig.DEBUG;
import org.json.JSONObject;


/* Fetches a Home Timeline */
public class HomeTimeLineApi extends BaseApi
{
	private static final String TAG = HomeTimeLineApi.class.getSimpleName();
	
	public static MessageListModel fetchHomeTimeLine(int count, int page) {
		WeiboParameters params = new WeiboParameters();
		params.put("count", count);
		params.put("page", page);
		
		try {
			JSONObject json = request(UrlConstants.HOME_TIMELINE, params, HTTP_GET);
			return new Gson().fromJson(json.toString(), MessageListModel.class);
		} catch (Exception e) {
			if (DEBUG) {
				Log.d(TAG, "Cannot fetch home timeline, " + e.getClass().getSimpleName());
				Log.d(TAG, Log.getStackTraceString(e));
			}
			return null;
		}
	}

	public static MessageListModel fetchGroupTimeLine(String groupId, int count, int page) {
		WeiboParameters params = new WeiboParameters();
		params.put("list_id", groupId);
		params.put("count", count);
		params.put("page", page);

		try {
			JSONObject json = request(UrlConstants.FRIENDSHIPS_GROUPS_TIMELINE, params, HTTP_GET);
			return new Gson().fromJson(json.toString(), MessageListModel.class);
		} catch (Exception e) {
			if (DEBUG) {
				Log.e(TAG, "Cannot get group timeline");
				Log.e(TAG, Log.getStackTraceString(e));
			}
		}

		return null;
	}
	public static MessageListModel fetchBilateralTimeLine(int count, int page) {
		WeiboParameters params = new WeiboParameters();
		params.put("count", count);
		params.put("page", page);

		try {
			JSONObject json = request(UrlConstants.BILATERAL_TIMELINE, params, HTTP_GET);
			return new Gson().fromJson(json.toString(), MessageListModel.class);
		} catch (Exception e) {
			if (DEBUG) {
				Log.d(TAG, "Cannot fetch bilateral timeline, " + e.getClass().getSimpleName());
				Log.d(TAG, Log.getStackTraceString(e));
			}
			return null;
		}
	}
}
