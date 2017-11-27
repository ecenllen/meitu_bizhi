package com.duoduoapp.meitu.yshow;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

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
}
