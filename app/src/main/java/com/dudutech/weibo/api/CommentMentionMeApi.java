/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.api;

import android.util.Log;

import com.dudutech.weibo.model.CommentListModel;
import com.dudutech.weibo.network.WeiboParameters;
import com.google.gson.Gson;

import org.json.JSONObject;
import static com.dudutech.weibo.BuildConfig.DEBUG;


public class CommentMentionMeApi extends BaseApi
{
	private static String TAG = CommentMentionMeApi.class.getSimpleName();

	public static CommentListModel fetchCommentMentionsTimeLine(int count, int page) {
		WeiboParameters params = new WeiboParameters();
		params.put("count", count);
		params.put("page", page);

		try {
			JSONObject json = request(UrlConstants.COMMENTS_MENTIONS, params, HTTP_GET);
			return new Gson().fromJson(json.toString(), CommentListModel.class);
		} catch (Exception e) {
			if (DEBUG) {
				Log.d(TAG, "Cannot fetch mentions timeline, " + e.getClass().getSimpleName());
			}
			return null;
		}
	}
}
