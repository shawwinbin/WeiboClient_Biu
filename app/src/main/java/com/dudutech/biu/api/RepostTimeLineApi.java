/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.biu.api;

import android.util.Log;

import com.dudutech.biu.model.RepostListModel;
import com.dudutech.biu.network.WeiboParameters;
import com.google.gson.Gson;

import org.json.JSONObject;
import static com.dudutech.biu.BuildConfig.DEBUG;



public class RepostTimeLineApi extends BaseApi
{
	private static final String TAG = RepostTimeLineApi.class.getSimpleName();

	public static RepostListModel fetchRepostTimeLine(long msgId, int count, int page) {
		WeiboParameters params = new WeiboParameters();
		params.put("id", msgId);
		params.put("count", count);
		params.put("page", page);

		try {
			JSONObject json = request(UrlConstants.REPOST_TIMELINE, params, HTTP_GET);
			return new Gson().fromJson(json.toString(), RepostListModel.class);
		} catch (Exception e) {
			if (DEBUG) {
				Log.d(TAG, "Cannot fetch repost timeline, " + e.getClass().getSimpleName());
			}
			return null;
		}
	}
}
