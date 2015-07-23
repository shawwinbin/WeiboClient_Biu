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

package com.dudutech.biu.Utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/* Helper functions */
public class Utility
{
//	private static final String TAG = Utility.class.getSimpleName();
//
//	private static final int REQUEST_CODE = 100001;
//
//	public static String lastPicPath;
//
//	public static int action_bar_title = -1;
//
//	public static int action_bar_spinner = -1;
//
//	static {
//		try {
//			Class<?> clazz = Class.forName("com.android.internal.R$id");
//			Field f = clazz.getDeclaredField("action_bar_title");
//			f.setAccessible(true);
//			action_bar_title = f.getInt(null);
//			f = clazz.getDeclaredField("action_bar_spinner");
//			f.setAccessible(true);
//			action_bar_spinner = f.getInt(null);
//		} catch (Exception e) {
//			if (DEBUG) {
//				Log.e(TAG, "Reflection cannot access internal ids");
//				Log.e(TAG, Log.getStackTraceString(e));
//			}
//		}
//	}
//
	public static int expireTimeInDays(long time) {
		return (int) TimeUnit.MILLISECONDS.toDays(time - System.currentTimeMillis());
	}
//
	public static boolean isTokenExpired(long time) {
		return time <= System.currentTimeMillis();
	}
//
//	public static boolean isCacheAvailable(long createTime, int availableDays) {
//		return System.currentTimeMillis() <= createTime + TimeUnit.DAYS.toMillis(availableDays);
//	}
//
//	public static int lengthOfString(String str) throws UnsupportedEncodingException {
//		// Considers 1 Chinese character as 2 English characters
//		return (str.getBytes("GB2312").length + 1) / 2;
//	}
//
//	public static int getSupportedMaxPictureSize() {
//		int[] array = new int[1];
//		GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, array, 0);
//
//		try {
//			if (array[0] == 0) {
//				GLES11.glGetIntegerv(GLES11.GL_MAX_TEXTURE_SIZE, array, 0);
//
//				if (array[0] == 0) {
//					GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, array, 0);
//
//					if (array[0] == 0) {
//						GLES30.glGetIntegerv(GLES30.GL_MAX_TEXTURE_SIZE, array, 0);
//					}
//				}
//			}
//		} catch (NoClassDefFoundError e) {
//			// Ignore the exception
//		}
//
//		return array[0] != 0 ? array[0] : 2048;
//	}
//
	public static String dealSourceString(String from) {
		int start = from.indexOf(">") + 1;
		int end = from.lastIndexOf("<");
		return from.substring(start, end);
	}
//
//	public static void clearOngoingUnreadCount(Context context) {
//		Settings s = Settings.getInstance(context);
//		s.putString(Settings.NOTIFICATION_ONGOING, "");
//	}
//
//	public static void startServiceAlarm(Context context, Class<?> service, long interval) {
//		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//		Intent i = new Intent(context, service);
//		PendingIntent p = PendingIntent.getService(context, REQUEST_CODE, i, PendingIntent.FLAG_CANCEL_CURRENT);
//		am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, 0, interval, p);
//	}
//
//	public static void stopServiceAlarm(Context context, Class<?> service) {
//		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//		Intent i = new Intent(context, service);
//		PendingIntent p = PendingIntent.getService(context, REQUEST_CODE, i, PendingIntent.FLAG_CANCEL_CURRENT);
//		am.cancel(p);
//	}
//
////	public static void startServices(Context context) {
////		Settings settings = Settings.getInstance(context);
////		int interval = getIntervalTime(settings.getInt(Settings.NOTIFICATION_INTERVAL, 1));
////
////		if (interval > -1) {
////			startServiceAlarm(context, ReminderService.class, interval);
////		}
////	}
//
////	public static void stopServices(Context context) {
////		stopServiceAlarm(context, ReminderService.class);
////	}
////
////	public static void restartServices(Context context) {
////		stopServices(context);
////
////		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
////
////		if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
////			startServices(context);
////		}
////	}
//
//	public static int getIntervalTime(int id) {
//		switch (id){
//		case 0:
//			return 1 * 60 * 1000;
//		case 1:
//			return 3 * 60 * 1000;
//		case 2:
//			return 5 * 60 * 1000;
//		case 3:
//			return 10 * 60 * 1000;
//		case 4:
//			return 30 * 60 * 1000;
//		case 5:
//			return -1;
//		}
//		return -1;
//	}
//
//	public static int getCurrentLanguage(Context context) {
//		int lang = Settings.getInstance(context).getInt(Settings.LANGUAGE, -1);
//		if (lang == -1) {
//			String language = Locale.getDefault().getLanguage();
//			String country = Locale.getDefault().getCountry();
//
//			if (DEBUG) {
//				Log.d(TAG, "Locale.getLanguage() = " + language);
//			}
//
//			if (language.equalsIgnoreCase("zh")) {
//				if (country.equalsIgnoreCase("CN")) {
//					lang = 1;
//				} else {
//					lang = 2;
//				}
//			} else {
//				lang = 0;
//			}
//		}
//
//		return lang;
//	}
//
//	// Must be called before setContentView()
//	public static void changeLanguage(Context context, int lang) {
//		String language = null;
//		String country = null;
//
//		switch (lang) {
//			case 1:
//				language = "zh";
//				country = "CN";
//				break;
//			case 2:
//				language = "zh";
//				country = "TW";
//				break;
//			default:
//				language = "en";
//				country = "US";
//				break;
//		}
//
//		Locale locale = new Locale(language, country);
//		Configuration conf = context.getResources().getConfiguration();
//		conf.locale = locale;
//		context.getApplicationContext().getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());
//	}
//
//	public static View addActionViewToCustom(Activity activity, int id, ViewGroup custom) {
//		View v = activity.findViewById(id);
//
//		if (v != null) {
//			return addActionViewToCustom(v, custom);
//		} else {
//			return null;
//		}
//	}
//
//	public static View addActionViewToCustom(View v, ViewGroup custom) {
//		if (v != null) {
//			ViewGroup parent = (ViewGroup) v.getParent();
//			parent.removeView(v);
//			parent.setVisibility(View.GONE);
//			ViewGroup.LayoutParams params = parent.getLayoutParams();
//			params.width = 0;
//			params.height = 0;
//			parent.setLayoutParams(params);
//			custom.addView(v);
//		}
//
//		return v;
//	}
//
//	// For SDK < 18
//	public static View findActionSpinner(Activity activity) {
//		ActionBar action = activity.getActionBar();
//
//		// Get ActionBarImpl class for ActionView object
//		// Then get spinner from ActionView
//		try {
//			Class<?> clazz = Class.forName("com.android.internal.app.ActionBarImpl");
//			Field f = clazz.getDeclaredField("mActionView");
//			f.setAccessible(true);
//			Object actionView = f.get(action);
//			clazz = Class.forName("com.android.internal.widget.ActionBarView");
//			f = clazz.getDeclaredField("mSpinner");
//			f.setAccessible(true);
//			return (View) f.get(actionView);
//		} catch (Exception e) {
//			if (DEBUG) {
//				Log.e(TAG, "Failed to find spinner");
//				Log.e(TAG, Log.getStackTraceString(e));
//			}
//
//			return null;
//		}
//	}
//
//	public static boolean isChrome() {
//		return Build.BRAND.equals("chromium") || Build.BRAND.equals("chrome");
//	}
//
//	public static int getStatusBarHeight(Context context) {
//		int result = 0;
//		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
//		if (resourceId > 0) {
//			result = context.getResources().getDimensionPixelSize(resourceId);
//		}
//		return result;
//	}
//
//
//	public static int getActionBarHeight(Context context) {
//		TypedValue v = new TypedValue();
//
//		if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, v, true)) {
//			return TypedValue.complexToDimensionPixelSize(v.data, context.getResources().getDisplayMetrics());
//		} else {
//			return 0;
//		}
//	}
//
//	public static int getDecorPaddingTop(Context context) {
//		return getActionBarHeight(context);
//	}
//
//	public static void setActionBarTranslation(Activity activity, float y) {
//		ViewGroup vg = (ViewGroup) activity.findViewById(android.R.id.content).getParent();
//		int count = vg.getChildCount();
//
//		if (DEBUG) {
//			Log.d(TAG, "==========================");
//		}
//
//		// Get the class of action bar
//		Class<?> actionBarContainer= null;
//		Field isSplit = null;
//
//		try {
//			actionBarContainer = Class.forName("com.android.internal.widget.ActionBarContainer");
//			isSplit = actionBarContainer.getDeclaredField("mIsSplit");
//			isSplit.setAccessible(true);
//		} catch (Exception e) {
//			if (DEBUG) {
//				Log.e(TAG, Log.getStackTraceString(e));
//			}
//		}
//
//		for (int i = 0; i < count; i++) {
//			View v = vg.getChildAt(i);
//
//			if (v.getId() != android.R.id.content) {
//				if (DEBUG) {
//					Log.d(TAG, "Found View: " + v.getClass().getName());
//				}
//
//				try {
//					if (actionBarContainer.isInstance(v)) {
//						if (DEBUG) {
//							Log.d(TAG, "Found ActionBarContainer");
//						}
//
//						if (isSplit.getBoolean(v)) {
//							if (DEBUG) {
//								Log.d(TAG, "Found Split Action Bar");
//							}
//
//							continue;
//						}
//					}
//				} catch (Exception e) {
//					if (DEBUG) {
//						Log.e(TAG, Log.getStackTraceString(e));
//					}
//				}
//
//				v.setTranslationY(y);
//			}
//		}
//
//		if (DEBUG) {
//			Log.d(TAG, "==========================");
//		}
//	}
//
//	public static String addUnitToInt(Context context, int i) {
//		String tenThousand = context.getString(R.string.ten_thousand);
//		String million = context.getString(R.string.million);
//		String hundredMillion = context.getString(R.string.hundred_million);
//		String billion = context.getString(R.string.billion);
//
//		if (tenThousand.equals("null")) { // English-styled number format
//			if (i < 1000000) {
//				return String.valueOf(i);
//			} else if (i < 1000000000) { // million
//				return String.valueOf(i / 1000000) + million;
//			} else { // billion
//				return String.valueOf(i / 1000000000) + billion;
//			}
//		} else { // Chinese-styled number format
//			if (i < 10000) {
//				return String.valueOf(i);
//			} else if (i < 100000000) {
//				return String.valueOf(i / 10000) + tenThousand;
//			} else {
//				return String.valueOf(i / 100000000) + hundredMillion;
//			}
//		}
//	}
//
//	public static float dp2px(Context context, float dp) {
//		return context.getResources().getDisplayMetrics().density * dp + 0.5f;
//	}
//
	public static int getFontHeight(Context context, float fontSize) {
		// Convert Dp To Px
		float px = context.getResources().getDisplayMetrics().density * fontSize + 0.5f;

		// Use Paint to get font height
		Paint p = new Paint();
		p.setTextSize(px);
		FontMetrics fm = p.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.ascent);
	}


	
	public static Method findMethod(Class<?> clazz, String name) throws NoSuchMethodException {
		Class<?> cla = clazz;
		Method method = null;
		
		do {
			try {
				method = cla.getDeclaredMethod(name);
			} catch (NoSuchMethodException e) {
				method = null;
				cla = cla.getSuperclass();
			}
		} while (method == null && cla != Object.class);
		
		if (method == null) {
			throw new NoSuchMethodException();
		} else {
			return method;
		}
	}
	
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}
	
	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
		Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

    // SmartBar Support
    public static boolean hasSmartBar() {
        try {
            Method method = Class.forName("android.os.Build").getMethod("hasSmartBar");
            return ((Boolean) method.invoke(null)).booleanValue();
        } catch (Exception e) {
        }

        if (Build.DEVICE.equals("mx2") || Build.DEVICE.equals("mx3")) {
            return true;
        } else if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
            return false;
        }

        return false;
    }

	/**
	 *
	 * @param from color value in the form 0xAARRGGBB.
	 * @param to color value in the form 0xAARRGGBB.
	 */
	public static int getGradientColor(int from, int to, float factor){
		int r = calculateGradient(Color.red(from),Color.red(to),factor); // It's so annoying without lambda.
		int g = calculateGradient(Color.green(from),Color.green(to),factor);
		int b = calculateGradient(Color.blue(from),Color.blue(to),factor);
		int a = calculateGradient(Color.alpha(from),Color.alpha(to),factor);

		return Color.argb(a,r,g,b);
	}

	private static int calculateGradient(int from, int to, float factor){
		return from + (int)((to - from) * factor);
	}
	public static String getPicPathFromUri(Uri uri, Activity activity) {
		String value = uri.getPath();

		if (value.startsWith("/external")) {
			String[] proj = {MediaStore.Images.Media.DATA};
			Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else {
			return value;
		}
	}



	/** Create a File for saving an image*/
	private static File getOutputImageFile(){
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), "BlackLight");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
//				Log.d(TAG, "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator +
				"IMG_"+ timeStamp + ".jpg");
		return mediaFile;
	}


	public static String getCountString(int count) {
		String result = "";

		if (count < 0) {
			return "0";
		}

		if (count < 10000) {

			result = count + "";

		} else if (count < 1000000) {
			result = (count / 1000) + "k";

		}
		else {
			result = (count / 1000000) + "M";
		}

		return result;
	}

	public static boolean isCacheAvailable(long createTime, int availableDays) {
		return System.currentTimeMillis() <= createTime + TimeUnit.DAYS.toMillis(availableDays);
	}
}
