/* 
 * Copyright (C) 2014 Peter Cai
 *
 * This file is part of BlackLight
 *
 * BlackLight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BlackLight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BlackLight.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dudutech.biu.api;

import android.text.TextUtils;
import android.util.Log;

import com.dudutech.biu.network.WeiboParameters;
import static com.dudutech.biu.BuildConfig.DEBUG;
import org.json.JSONObject;



/* BlackMagic Login Api */

public class LoginApi extends BaseApi
{
	private static final String TAG = LoginApi.class.getSimpleName();

	public static final String WEICO_SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";

	public static final String WEICO_CLIENT_ID = "211160679";
	public static final String WEICO_REDIRCT_URL = "http://oauth.weico.cc";
	public static final String WEICO_APP_KEY = "1e6e33db08f9192306c4afa0a61ad56c";
	public static final String WEICO_PACKNAME = "com.eico.weico";
	// Returns token and expire date
	public static String[] login(String appId, String appSecret, String username, String passwd) {
		WeiboParameters params = new WeiboParameters();
		params.put("username", username);
		params.put("password", passwd);
		params.put("client_id", appId);
		params.put("client_secret", appSecret);
		params.put("grant_type", "password");

		
		try {
			JSONObject json = requestWithoutAccessToken(UrlConstants.OAUTH2_ACCESS_TOKEN, params, HTTP_POST);
			return new String[]{json.optString("access_token"), json.optString("expires_in")};
		} catch (Exception e) {
			if (DEBUG) {
				Log.e(TAG, "login error:" + e.getClass().getSimpleName());
			}
			return null;
		}
	}

	public static String getOauthLoginPage() {
		return UrlConstants.OAUTH2_ACCESS_AUTHORIZE + "?" + "client_id=" + WEICO_CLIENT_ID
				+ "&response_type=token&redirect_uri=" + WEICO_REDIRCT_URL
				+ "&key_hash=" + WEICO_APP_KEY + (TextUtils.isEmpty(WEICO_PACKNAME) ? "" : "&packagename=" + WEICO_PACKNAME)
				+ "&display=mobile" + "&scope=" + WEICO_SCOPE;
	}

	public static boolean isUrlRedirected(String url) {
		return url.startsWith(WEICO_REDIRCT_URL);
	}
}
