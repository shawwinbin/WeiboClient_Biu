package com.dudutech.biu.api;
import android.util.Log;

import com.dudutech.biu.network.HttpUtility;
import com.dudutech.biu.network.WeiboParameters;

import org.json.JSONArray;
import org.json.JSONObject;
import static com.dudutech.biu.BuildConfig.DEBUG;

public abstract class BaseApi
{
	private static final String TAG = BaseApi.class.getSimpleName();
	
	// Http Methods
	protected static final String HTTP_GET = HttpUtility.GET;
	protected static final String HTTP_POST = HttpUtility.POST;
	
	// Access Token
	private static String mAccessToken;

	public static JSONObject request(String url, WeiboParameters params, String method) throws Exception {
		return request(mAccessToken, url, params, method, JSONObject.class);
	}

	public static JSONArray requestArray(String url, WeiboParameters params, String method) throws Exception {
		return request(mAccessToken, url, params, method, JSONArray.class);
	}

	public static <T> T request(String token, String url, WeiboParameters params, String method, Class<T> jsonClass) throws Exception {
		if (token == null) {
			return null;
		} else {
			params.put("access_token", token);
			String jsonData = HttpUtility.doRequest(url, params, method);
			
			if (DEBUG) {
				Log.d(TAG, "jsonData = " + jsonData);
			}
			
			if (jsonData != null && (jsonData.contains("{") || jsonData.contains("["))) {
				return jsonClass.getConstructor(String.class).newInstance(jsonData);
			} else {
				return null;
			}
		}
	}

	public static JSONObject requestWithoutAccessToken(String url, WeiboParameters params, String method) throws Exception {
		String jsonData = HttpUtility.doRequest(url, params, method);
		
		if (DEBUG) {
			Log.d(TAG, "jsonData = " + jsonData);
		}
		
		if (jsonData != null && jsonData.contains("{")) {
			return new JSONObject(jsonData);
		} else {
			return null;
		}
	}
	
	public static String getAccessToken() {
		return mAccessToken;
	}
	
	public static void setAccessToken(String token) {
		mAccessToken = token;
	}
}
