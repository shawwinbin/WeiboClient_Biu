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
import android.text.TextUtils;
import android.util.Log;

import com.dudutech.biu.R;
import com.dudutech.biu.global.MyApplication;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/* Helper functions */
public class Utility
{
	private static final String TAG = Utility.class.getSimpleName();


	public static int expireTimeInDays(long time) {
		return (int) TimeUnit.MILLISECONDS.toDays(time - System.currentTimeMillis());
	}

	public static boolean isTokenExpired(long time) {
		return time <= System.currentTimeMillis();
	}

	public static String dealSourceString(String from) {
		int start = from.indexOf(">") + 1;
		int end = from.lastIndexOf("<");
		return from.substring(start, end);
	}





	public static int getFontHeight(Context context, float fontSize) {
		// Convert Dp To Px
		float px = context.getResources().getDisplayMetrics().density * fontSize + 0.5f;

		// Use Paint to get font height
		Paint p = new Paint();
		p.setTextSize(px);
		FontMetrics fm = p.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.ascent);
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
	public static File getOutputImageFile(){
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), "Biu");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				Log.d(TAG, "failed to create directory");
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

	public static String  countUinit ;
	public static String getCountString(int count) {
		String result = "";

		if(TextUtils.isEmpty(countUinit)){
			countUinit= MyApplication.getInstance().getString(R.string.ten_thousand);
		}

		if (count < 0) {
			return "0";
		}

		if (count < 10000) {

			result = count + "";

		} else  {
			result = (count / 10000) + countUinit;

		}


		return result;
	}

	public static boolean isCacheAvailable(long createTime, int availableDays) {
		return System.currentTimeMillis() <= createTime + TimeUnit.DAYS.toMillis(availableDays);
	}
}
