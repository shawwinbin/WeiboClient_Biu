package com.dudutech.biu.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Locale;


/**
 * tools to fetch device information
 * 
 * @author shaw
 *         2015-1-27
 * 
 */
@SuppressLint("DefaultLocale")
public class DeviceUtil {

	private static final String TAG = "DeviceUtil";

	public static boolean isSDcardEnabel()
	{
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static String getSDCardPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
			return sdDir.toString();
		}
		return null;

	}

	/**
	 * sd卡是否有足够的可以空间
	 * 
	 * @return
	 *         boolean
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static boolean isSdCardAvailale() {

		File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		long size = (availableBlocks * blockSize) / 1024;
		if (size < 500) {
			return false;
		} else {
			return true;
		}
	}


	/**
	 * 获取软件版本名称
	 * 
	 *
	 return versionName;
	 }@return
	 */
	public static String getVersionName(Context context) {
		String versionName = "";
		try {
			PackageInfo packageinfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), PackageManager.GET_INSTRUMENTATION);
			versionName = packageinfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}



	public static String getRegion() {
		return Locale.getDefault().getCountry().toString();
	}

	/**
	 * network tools
	 * */
	public static boolean isWifiEnable(Context context) {

		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

		Log.i(TAG, "wifiEnable" + wifiManager.isWifiEnabled());
		return wifiManager.isWifiEnabled();
	}

	public static String getActiveNetWorkName(Context context) {

		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		String result = null;
		do {
			if (connectivity == null) {
				break;
			}

			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {

					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						result = info[i].getTypeName();
					}
				}
			}
		} while (false);
		Log.i(TAG, "getActiveNetWorkName : " + result);
		return result;
	}



	public static final int NETWORKTYPE_INVALID = 0;
	/** wap网络 */
	public static final int NETWORKTYPE_WAP = 1;
	/** 2G网络 */
	public static final int NETWORKTYPE_2G = 2;
	/** 3G和3G以上网络，/
	public static final int NETWORKTYPE_3G = 3;
	/** wifi网络 */
	public static final int NETWORKTYPE_WIFI = 4;

	public static final int NETWORKTYPE_ETHERNET = 5;


	public enum NetWorkType {
		none, mobile, wifi
	}



	public static float getDensity(Context context) {
		DisplayMetrics metrics = getDisplayMetrics(context);
		return metrics.density;
	}

	public static DisplayMetrics getDisplayMetrics(Context context) {
		return context.getResources().getDisplayMetrics();
	}


	/**
	 * return width of screen, with px
	 * 
	 * @return
	 *         int
	 * 
	 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics metrics = getDisplayMetrics(context);
		return metrics.widthPixels;
	}

	/**
	 * return height size of screen, with px
	 * 
	 * @return
	 *         int
	 * 
	 */
	public static int getScreenHeight(Context context) {
		DisplayMetrics metrics = getDisplayMetrics(context);
		return metrics.heightPixels;
	}

	/**
	 * 获得屏幕分辨率
	 * 
	 * @return
	 *         String
	 * 
	 */
	public static String getScreenResolution(Context context) {
		int w = getScreenWidth(context);
		int h = getScreenHeight(context);
		String resolution = "";
		if (w <= h) {
			resolution = w + "*" + h;
		} else {
			resolution = h + "*" + w;
		}
		return resolution;
	}

	/**
	 * 设置成全屏
	 * 
	 * @param activity
	 *            void
	 * 
	 */
	public static void setFullScreen(Activity activity) {
		if (activity == null) {
			return;
		}
		WindowManager.LayoutParams params = activity.getWindow().getAttributes();
		params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		activity.getWindow().setAttributes(params);
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	/**
	 * 设置成非全屏，显示状态栏
	 * 
	 * @param activity
	 *            void
	 * 
	 */
	public static void setNoFullScreen(Activity activity) {
		if (activity == null) {
			return;
		}
		WindowManager.LayoutParams params = activity.getWindow().getAttributes();
		params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		activity.getWindow().setAttributes(params);
		activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	/**
	 * 获得手机状态栏的高度
	 * 
	 * @param context
	 * @return
	 *         int
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public static int getStatusBarHeigh(Context context) {
		int heigh = 0;
		try {

			Class c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			heigh = context.getResources().getDimensionPixelSize(x);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return heigh;
	}

	/**
	 * get the SoftInput state
	 * 
	 * @param activity
	 * @return if shown return true else return false
	 */
	public static boolean softInputIsShow(Activity activity) {
		if (activity == null) {
			return false;
		}
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.isActive();
	}

	/**
	 * open the SoftInput
	 * 
	 * @param activity
	 * @param mEditText
	 *            open for this EditText
	 */
	public static void showSoftInput(Activity activity, EditText mEditText) {
		if (activity == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);
		if (imm != null) {
			imm.toggleSoftInputFromWindow(mEditText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);

		}
	}

	/**
	 * close the SoftInput
	 * 
	 * @param activity
	 */
	public static void closeSoftInput(Activity activity, EditText mEditText) {
		if (activity == null || mEditText == null) {
			return;
		}

		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null && inputMethodManager.isActive()) {
			inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

		}
	}

	public static boolean isSoftInputActive(Activity activity, EditText mEditText) {
		if (activity == null) {
			return false;
		}
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);
		if (imm != null) {
			return imm.isActive();
		}
		return false;
	}

	public static void openImm(Activity context) {
		if (context == null) {
			return;
		}
		View v = context.getWindow().peekDecorView();
		if (v != null && v.getWindowToken() != null) {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(context.INPUT_METHOD_SERVICE);
			// 如果输入法打开则关闭，如果没打开则打开
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
		}
	}


	public static long getAvailableStorage() {
		String storageDirectory = null;
		storageDirectory = Environment.getExternalStorageDirectory().toString();

		try {
			StatFs stat = new StatFs(storageDirectory);
			long avaliableSize = ((long) stat.getAvailableBlocks() * (long)
					stat.getBlockSize());
			return avaliableSize;
		} catch (RuntimeException ex) {
			return 0;
		}
	}

	public static NetWorkType getNetworkType(Context context) {

		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null) {
			switch (networkInfo.getType()) {
				case ConnectivityManager.TYPE_MOBILE:
					return NetWorkType.mobile;
				case ConnectivityManager.TYPE_WIFI:
					return NetWorkType.wifi;
			}
		}

		return NetWorkType.none;
	}


}
