
package com.shaweibo.biu.global;

import android.graphics.Bitmap;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class Constants
{
	public static final int DB_CACHE_DAYS = 10;


	public static final int MAX_WEIBO_LENGTH = 140;

	

	// Statuses
	public static final int HOME_TIMELINE_PAGE_SIZE = 25;
	
	// SQL
	public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS ";
	private static final String APP_ID = "211160679";
	private static final String APP_KEY_HASH = "1e6e33db08f9192306c4afa0a61ad56c";
	private static final String REDIRECT_URI = "http://oauth.weico.cc";
	private static final String PACKAGE_NAME = "com.eico.weico";
	private static final String SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";


	//loading status
	public static enum LOADING_STATUS {
		NORMAL,
		FINISH,
		FAIL
	}

	public static ColorGenerator generator = ColorGenerator.DEFAULT;
    //imageloader  display options

	public static  DisplayImageOptions options = new DisplayImageOptions.Builder()
//			.showImageOnLoading(R.drawable.ic_stub) // resource or bitmap
//			.showImageForEmptyUri(R.drawable.ic_empty) // resource or bitmap
//			.showImageOnFail(R.drawable.ic_error) // resource or bitmap
			.resetViewBeforeLoading(false)  // default
			.delayBeforeLoading(1000)
			.cacheInMemory(false) // default
			.cacheOnDisk(true) // default
//			.preProcessor(...)
//			.postProcessor(...)
//	        .extraForDownloader(...)
			.imageScaleType(ImageScaleType.NONE) // default
			.bitmapConfig(Bitmap.Config.ARGB_8888) // default
//	       .decodingOptions(...)
			.displayer(new SimpleBitmapDisplayer()) // default
//			.handler(new Handler()) // default
			.build();

	public static  DisplayImageOptions timelineListOptions = new DisplayImageOptions.Builder()
			.resetViewBeforeLoading(true)  // default
			.delayBeforeLoading(1000)
			.cacheInMemory(true) // default
			.cacheOnDisk(true) // default
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
			.bitmapConfig(Bitmap.Config.ARGB_8888) // default
			.displayer(new SimpleBitmapDisplayer()) // default
			.build();

	public static  DisplayImageOptions avatarOptions = new DisplayImageOptions.Builder()
		.resetViewBeforeLoading(true)  // default
		.cacheInMemory(true) // default
		.cacheOnDisk(true) // default
		.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
		.bitmapConfig(Bitmap.Config.ARGB_8888) // default
		.displayer(new RoundedBitmapDisplayer(1000)) // default
		.build();

	public static  DisplayImageOptions getAvatarOptions(String s){


		TextDrawable drawable = TextDrawable.builder()
				.buildRound(s,generator.getRandomColor());
		DisplayImageOptions avatarOptions = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true)  // default
				.cacheInMemory(true) // default
				.cacheOnDisk(true) // default
				.showImageOnLoading(drawable)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				.bitmapConfig(Bitmap.Config.ARGB_8888) // default
				.displayer(new RoundedBitmapDisplayer(1000)) // default
				.build();

		return  avatarOptions;

	}


}
