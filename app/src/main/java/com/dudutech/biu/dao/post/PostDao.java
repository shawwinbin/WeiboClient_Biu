/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.biu.dao.post;

import android.graphics.Bitmap;

import com.dudutech.biu.api.BaseApi;
import com.dudutech.biu.api.UrlConstants;
import com.dudutech.biu.model.MessageModel;
import com.dudutech.biu.network.WeiboParameters;
import com.google.gson.Gson;

import org.json.JSONObject;


public class PostDao extends BaseApi
{
	public static final int EXTRA_NONE = 0;
	public static final int EXTRA_COMMENT = 1;
	public static final int EXTRA_COMMENT_ORIG = 2;
	public static final int EXTRA_ALL = 3;
	
	public static boolean newPost(String status) {
		WeiboParameters params = new WeiboParameters();
		params.put("status", status);
		try {
			JSONObject json = request(UrlConstants.UPDATE, params, HTTP_POST);
			MessageModel msg = new Gson().fromJson(json.toString(), MessageModel.class);
			if (msg == null || msg.idstr == null || msg.idstr.trim().equals("")) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	// Picture size must be smaller than 5M
	public static boolean newPostWithPic(String status, Bitmap pic) {
		WeiboParameters params = new WeiboParameters();
		params.put("status", status);
		params.put("pic", pic);
		try {
			JSONObject json = request(UrlConstants.UPLOAD, params, HTTP_POST);
			MessageModel msg = new Gson().fromJson(json.toString(), MessageModel.class);

			if (msg == null || msg.idstr == null || msg.idstr.trim().equals("")) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}
	
	public static boolean newRepost(long id, String status, int extra) {
		WeiboParameters params = new WeiboParameters();
		params.put("status", status);
		params.put("id", id);
		params.put("is_comment", extra);

		try {
			JSONObject json = request(UrlConstants.REPOST, params, HTTP_POST);
			MessageModel msg = new Gson().fromJson(json.toString(), MessageModel.class);

			if (msg == null || msg.idstr == null || msg.idstr.trim().equals("")) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}
	
	// Status destroyer
	public static void deletePost(long id) {
		WeiboParameters params = new WeiboParameters();
		params.put("id", id);
		
		try {
			request(UrlConstants.DESTROY, params, HTTP_POST);
		} catch (Exception e) {
			// Nothing can be done
		}
	}
	


	// Upload pictures
	public static String uploadPicture(Bitmap picture) {
		WeiboParameters params = new WeiboParameters();
		params.put("pic", picture);

		try {
			JSONObject json = request(UrlConstants.UPLOAD_PIC, params, HTTP_POST);
			return json.optString("pic_id");
		} catch (Exception e) {
			return null;
		}
	}

	// Post with multi pictures
	// @param pics: ids returned by uploadPicture, split with ","
	public static boolean newPostWithMultiPics(String status, String pics) {
		WeiboParameters params = new WeiboParameters();
		params.put("status", status);
		params.put("pic_id", pics);
		try {
			JSONObject json = request(UrlConstants.UPLOAD_URL_TEXT, params, HTTP_POST);
			MessageModel msg = new Gson().fromJson(json.toString(), MessageModel.class);
			if (msg == null || msg.idstr == null || msg.idstr.trim().equals("")) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}


}
