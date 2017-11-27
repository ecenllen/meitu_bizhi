package com.duoduoapp.brothers.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;

/**
 * 工具类
 * 
 * @author Shanlin
 * 
 */
public class Tools {

	public static final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();

	/**
	 * 跳转市场搜索应用
	 * 
	 * @param context
	 * @param packageName
	 *            包名
	 */
	public static void startMarket(Context context, String packageName) {
		Uri uri = Uri.parse("market://details?id=" + packageName);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		// intent.setData(Uri.parse("market://details?id=" + packageName));
		context.startActivity(intent);
	}

	/**
	 * 跳转市场搜索发布者信息
	 * 
	 * @param context
	 * @param name
	 *            发布者名字
	 */
	public static void searchMarket(Context context, String name) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://search?q=" + name));
		context.startActivity(intent);
	}

	/**
	 * 启动应用
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void startApp(Context context, String packageName) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		context.startActivity(intent);
	}

	public static void initFileDir(String path) {
		String[] ps = path.split("\\/");
		String s = SDCARD + "";
		for (int i = 0; i < ps.length; i++) {
			if ("".equals(ps[i])) {
				continue;
			}
			s += ps[i];
			File f = new File(s);
			if (!f.exists()) {
				f.mkdir();
			}
		}
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
		} else {
			// 如果仅仅是用来判断网络连接
			// 则可以使用 cm.getActiveNetworkInfo().isAvailable();
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}
}
