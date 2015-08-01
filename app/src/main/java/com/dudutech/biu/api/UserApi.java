
package com.dudutech.biu.api;

import android.util.Log;

import com.dudutech.biu.model.UserModel;
import com.dudutech.biu.network.WeiboParameters;
import com.google.gson.Gson;
import static com.dudutech.biu.BuildConfig.DEBUG;
import org.json.JSONObject;





/* Apis to read / write user info */
public class UserApi extends BaseApi
{
	private static String TAG = UserApi.class.getSimpleName();
	
	public static UserModel getUser(String uid) {
		WeiboParameters params = new WeiboParameters();
		params.put("uid", uid);
		
		try {
			JSONObject json = request(UrlConstants.USER_SHOW, params, HTTP_GET);
			UserModel user = new Gson().fromJson(json.toString().replaceAll("-Weibo", ""), UserModel.class);
			return user;
		} catch (Exception e) {
			if (DEBUG) {
				Log.e(TAG, "Failed to fetch user info from net: " + e.getClass().getSimpleName());
			}
			return null;
		}
	}
	
	public static UserModel getUserByName(String name) {
		WeiboParameters params = new WeiboParameters();
		params.put("screen_name", name);

		try {
			JSONObject json = request(UrlConstants.USER_SHOW, params, HTTP_GET);
			UserModel user = new Gson().fromJson(json.toString().replaceAll("-Weibo", ""), UserModel.class);
			return user;
		} catch (Exception e) {
			if (DEBUG) {
				Log.e(TAG, "Failed to fetch user info from net: " + e.getClass().getSimpleName());
			}
			return null;
		}
	}
}
