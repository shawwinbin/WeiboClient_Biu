
package com.dudutech.biu.api;

import com.dudutech.biu.model.MessageListModel;
import com.dudutech.biu.network.WeiboParameters;
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
