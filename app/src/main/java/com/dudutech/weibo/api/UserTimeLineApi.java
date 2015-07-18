/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.api;

import com.dudutech.weibo.model.MessageListModel;
import com.dudutech.weibo.network.WeiboParameters;
import com.google.gson.Gson;

import org.json.JSONObject;



/* Fetches messages published by an exact user */
public class UserTimeLineApi extends BaseApi
{
	public static MessageListModel fetchUserTimeLine(String uid, int count, int page) {

		WeiboParameters params = new WeiboParameters();
		params.put("uid", uid);
		params.put("count", count);
		params.put("page", page);

		try {
			JSONObject json = request(UrlConstants.USER_TIMELINE, params, HTTP_GET);
			return new Gson().fromJson(json.toString(), MessageListModel.class);
		} catch (Exception e) {
			return null;
		}
	}
}
