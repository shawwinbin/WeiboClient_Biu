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
import com.dudutech.weibo.model.CommentModel;
import com.dudutech.weibo.network.WeiboParameters;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static com.dudutech.weibo.BuildConfig.DEBUG;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class WeiboCommentApi extends BaseApi
{
	private static String TAG = WeiboCommentApi.class.getSimpleName();

	public static CommentListModel fetchCommentOfStatus(long msgId, int count, int page) {
		WeiboParameters params = new WeiboParameters();
		params.put("id", msgId);
		params.put("count", count);
		params.put("page", page);

		try {
			JSONObject json = request(Constants.COMMENTS_SHOW, params, HTTP_GET);

			return new Gson().fromJson(json.toString(), CommentListModel.class);
		} catch (Exception e) {
			if (DEBUG) {
				Log.d(TAG, "Cannot fetch comments timeline, " + e.getClass().getSimpleName());
			}
			return null;
		}
	}
}
