/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.dudutech.weibo.api;

import android.graphics.Bitmap;

import com.dudutech.weibo.model.MessageModel;
import com.dudutech.weibo.network.WeiboParameters;
import com.google.gson.Gson;

import org.json.JSONObject;


public class PostApi extends BaseApi
{
	public static final int EXTRA_NONE = 0;
	public static final int EXTRA_COMMENT = 1;
	public static final int EXTRA_COMMENT_ORIG = 2;
	public static final int EXTRA_ALL = 3;
	
	public static boolean newPost(String status, String version) {
		WeiboParameters params = new WeiboParameters();
		params.put("status", status);
//		params.put("annotations", parseAnnotation(version));
		
		try {
			JSONObject json = request(Constants.UPDATE, params, HTTP_POST);
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
			JSONObject json = request(Constants.UPLOAD, params, HTTP_POST);
			MessageModel msg = new Gson().fromJson(json.toString(), MessageModel.class);

			if (msg == null || msg.idstr == null || msg.idstr.trim().equals("")) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}
	
	public static boolean newRepost(long id, String status, int extra, String version) {
		WeiboParameters params = new WeiboParameters();
		params.put("status", status);
		params.put("id", id);
		params.put("is_comment", extra);
//		params.put("annotations", parseAnnotation(version));

		try {
			JSONObject json = request(Constants.REPOST, params, HTTP_POST);
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
			request(Constants.DESTROY, params, HTTP_POST);
		} catch (Exception e) {
			// Nothing can be done
		}
	}
	
	// Add to favorite
	public static void fav(long id) {
		WeiboParameters params = new WeiboParameters();
		params.put("id", id);
		
		try {
			request(Constants.FAVORITES_CREATE, params, HTTP_POST);
		} catch (Exception e) {
			
		}
	}
	
	// Remove from favorite
	public static void unfav(long id) {
		WeiboParameters params = new WeiboParameters();
		params.put("id", id);
		
		try {
			request(Constants.FAVORITES_DESTROY, params, HTTP_POST);
		} catch (Exception e) {
			
		}
	}

	// Upload pictures
	public static String uploadPicture(Bitmap picture) {
		WeiboParameters params = new WeiboParameters();
		params.put("pic", picture);

		try {
			JSONObject json = request(Constants.UPLOAD_PIC, params, HTTP_POST);
			return json.optString("pic_id");
		} catch (Exception e) {
			return null;
		}
	}

	// Post with multi pictures
	// @param pics: ids returned by uploadPicture, split with ","
	public static boolean newPostWithMultiPics(String status, String pics, String version) {
		WeiboParameters params = new WeiboParameters();
		params.put("status", status);
		params.put("pic_id", pics);
//		params.put("annotations", parseAnnotation(version));

		try {
			JSONObject json = request(Constants.UPLOAD_URL_TEXT, params, HTTP_POST);
			MessageModel msg = new Gson().fromJson(json.toString(), MessageModel.class);
			if (msg == null || msg.idstr == null || msg.idstr.trim().equals("")) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

//	public static String parseAnnotation(String version) {
//		AnnotationModel anno = new AnnotationModel();
//		anno.bl_version = version;
//		return "[" + new Gson().toJson(anno) + "]";
//	}

}
