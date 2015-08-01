
package com.dudutech.biu.api;

import com.dudutech.biu.network.WeiboParameters;

import org.json.JSONObject;


/* Current Account Api of Sina Weibo */
public class AccountApi extends BaseApi {
    public static String getUid() {
        try {

            JSONObject json = request(UrlConstants.GET_UID, new WeiboParameters(), HTTP_GET);
            return json.optString("uid");
        } catch (Exception e) {
            return null;
        }
    }
}
