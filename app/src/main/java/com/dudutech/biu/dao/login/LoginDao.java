package com.dudutech.biu.dao.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.dudutech.biu.dao.UrlConstants;
import com.dudutech.biu.dao.HttpClientUtils;
import com.dudutech.biu.dao.post.BasePostDao;
import com.dudutech.biu.dao.relationship.GroupDao;
import com.dudutech.biu.dao.WeiboParameters;


import org.json.JSONObject;

import java.util.Arrays;
import java.util.ArrayList;


public class LoginDao
{
	private static final String TAG = LoginDao.class.getSimpleName();

	private static Context mContext;

	private SharedPreferences mPrefs;
	private String mAccessToken;
	private String mUid;
	private long mExpireDate;

	private ArrayList<String> mNames = new ArrayList<String>();
	private ArrayList<String> mTokens = new ArrayList<String>();
	private ArrayList<Long> mExpireDates = new ArrayList<Long>();
	private static LoginDao mInstance;


	public  static LoginDao getInstance(Context context){
		if(mInstance==null){
			mContext=context;
			mInstance =new LoginDao(context);
		}
		return  mInstance;
	}

	private LoginDao(Context context) {
		mContext = context;
		mPrefs = context.getSharedPreferences("access_token", Context.MODE_PRIVATE);
		mAccessToken = mPrefs.getString("access_token", null);
		mUid = mPrefs.getString("uid", "");
		mExpireDate = mPrefs.getLong("expires_in", Long.MIN_VALUE);

		if (mAccessToken != null) {
			BasePostDao.setAccessToken(mAccessToken);
			HttpClientUtils.setAccessToken(mAccessToken);
		}
		parseMultiUser();
	}

	public void login(String token, String expire) {
		mAccessToken = token;
		BasePostDao.setAccessToken(mAccessToken);
		HttpClientUtils.setAccessToken(mAccessToken);
		mExpireDate = System.currentTimeMillis() + Long.valueOf(expire) * 1000;
		mUid = getUidByToken();
		GroupDao groupDao=new GroupDao(mContext);
		groupDao.getGroups();
		groupDao.cache();



	}

	public void logout() {
		mAccessToken = null;
		mExpireDate = Long.MIN_VALUE;
		mPrefs.edit().remove("access_token").remove("expires_in").remove("uid").commit();
	}

	public void cache() {
		mPrefs.edit().putString("access_token", mAccessToken)
				.putLong("expires_in", mExpireDate)
				.putString("uid", mUid)
				.commit();
	}

	public String getAccessToken() {
		return mAccessToken;
	}

	public String getUid() {
		return mUid;
	}

	public long getExpireDate() {
		return mExpireDate;
	}

	public String[] getUserNames() {
		return mNames.toArray(new String[mNames.size()]);
	}


	private void parseMultiUser() {
		String str = mPrefs.getString("names", "");
		if (str == null || str.trim().equals(""))
			return;

		mNames.addAll(Arrays.asList(str.split(",")));

		str = mPrefs.getString("tokens", "");
		if (str == null || str.trim().equals(""))
			return;

		mTokens.addAll(Arrays.asList(str.split(",")));

		str = mPrefs.getString("expires", "");
		if (str == null || str.trim().equals(""))
			return;

		String[] s = str.split(",");
		for (int i = 0; i < s.length; i++) {
			mExpireDates.add(Long.valueOf(s[i]));
		}

		if (mTokens.size() != mNames.size() ||
				mTokens.size() != mExpireDates.size() ||
				mExpireDates.size() != mNames.size()) {
			mNames.clear();
			mTokens.clear();
			mExpireDates.clear();
		}
	}


	public static String getUidByToken() {
		try {

			String json = HttpClientUtils.doGetRequstWithAceesToken(UrlConstants.GET_UID, new WeiboParameters());
			return new JSONObject(json).optString("uid");
		} catch (Exception e) {
			return null;
		}
	}


}
