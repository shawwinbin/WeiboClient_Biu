

package com.dudutech.biu.api;

import android.util.Log;

import com.dudutech.biu.model.CommentListModel;
import com.dudutech.biu.network.WeiboParameters;
import com.google.gson.Gson;

import static com.dudutech.biu.BuildConfig.DEBUG;
import org.json.JSONObject;


public class WeiboCommentApi extends BaseApi
{
	private static String TAG = WeiboCommentApi.class.getSimpleName();

	public static CommentListModel fetchCommentOfStatus(long msgId, int count, int page) {
		WeiboParameters params = new WeiboParameters();
		params.put("id", msgId);
		params.put("count", count);
		params.put("page", page);

		try {
			JSONObject json = request(UrlConstants.COMMENTS_SHOW, params, HTTP_GET);

			return new Gson().fromJson(json.toString(), CommentListModel.class);
		} catch (Exception e) {
			if (DEBUG) {
				Log.d(TAG, "Cannot fetch comments timeline, " + e.getClass().getSimpleName());
			}
			return null;
		}
	}
}
