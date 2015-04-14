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

package com.dudutech.weibo.api;

import android.util.Log;

import com.dudutech.weibo.model.MessageListModel;
import com.dudutech.weibo.network.WeiboParameters;
import com.google.gson.Gson;
import static com.dudutech.weibo.BuildConfig.DEBUG;
import org.json.JSONObject;


/* Fetches a Home Timeline */
public class HomeTimeLineApi extends BaseApi
{
	private static final String TAG = HomeTimeLineApi.class.getSimpleName();
	
	public static MessageListModel fetchHomeTimeLine(int count, int page) {
		WeiboParameters params = new WeiboParameters();
		params.put("count", count);
		params.put("page", page);
		
		try {
			JSONObject json = request(Constants.HOME_TIMELINE, params, HTTP_GET);
			return new Gson().fromJson(json.toString(), MessageListModel.class);
		} catch (Exception e) {
			if (DEBUG) {
				Log.d(TAG, "Cannot fetch home timeline, " + e.getClass().getSimpleName());
				Log.d(TAG, Log.getStackTraceString(e));
			}
			return null;
		}
	}
}
