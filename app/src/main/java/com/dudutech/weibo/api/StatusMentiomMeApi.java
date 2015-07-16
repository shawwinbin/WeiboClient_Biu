/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.api;

import android.util.Log;

import com.dudutech.weibo.model.MessageListModel;
import com.dudutech.weibo.network.WeiboParameters;
import com.google.gson.Gson;
import org.json.JSONObject;
import static com.dudutech.weibo.BuildConfig.DEBUG;


public class StatusMentiomMeApi extends BaseApi
{
	private static final String TAG = StatusMentiomMeApi.class.getSimpleName();
	
	public static MessageListModel fetchMentionsTimeLine(int count, int page) {
		WeiboParameters params = new WeiboParameters();
		params.put("count", count);
		params.put("page", page);

		try {
			JSONObject json = request(Constants.MENTIONS, params, HTTP_GET);
			return new Gson().fromJson(json.toString(), MessageListModel.class);
		} catch (Exception e) {
			if (DEBUG) {
				Log.d(TAG, "Cannot fetch home timeline, " + e.getClass().getSimpleName());
			}
			return null;
		}
	}
	
	public static MessageListModel fetchMentionsTimeLineSince(long id) {
		WeiboParameters params = new WeiboParameters();
		params.put("since_id", id);

		try {
			JSONObject json = request(Constants.MENTIONS, params, HTTP_GET);
			return new Gson().fromJson(json.toString(), MessageListModel.class);
		} catch (Exception e) {
			if (DEBUG) {
				Log.d(TAG, "Cannot fetch home timeline, " + e.getClass().getSimpleName());
			}
			return null;
		}
	}
}
