package com.duoduoapp.meitu.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

import static android.support.v4.content.FileProvider.getUriForFile;

/**
 * 
 * 
 * @author Administrator
 */
public class PackageUtil {

	/**
	 * 跳转市场
	 * 
	 * @param activity
	 */
	public static void giveHaoping(Activity activity) {
		Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		try {
			activity.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(activity, "异常", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 跳转浏览�?
	 * 
	 * @param activity
	 * @param url
	 *            地址
	 */
	public static void qidongLiulanqi(Activity activity, String url) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		try {
			activity.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(activity, "异常", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * install package normal by system intent<br>
	 * 安装apk文件
	 * 
	 * @param context
	 * @param filePath
	 *            file path of package
	 * @return whether apk exist
	 */
	public static boolean installNormal(Context context, String filePath) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		File file = new File(filePath);
		if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
			return false;
		}

//		i.setDataAndType(getUriForFile(context,	"com.zpsd.vr", file), "application/vnd.android.package-archive");
//		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(i);

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri contentUri = FileProvider.getUriForFile(context, "com.zpsd.vr.fileprovider",file);
			intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
		} else {
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		}
		context.startActivity(intent);
		return true;
	}


	public static void startOwnApp(Context context) {
		startApp(context, context.getPackageName());
	}

	/**
	 * 打开app
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void startApp(Context context, String packageName) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		context.startActivity(intent);
	}

	/**
	 * 是否安装
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isInstallApp(Activity context, String packageName) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
			ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
	/**
	 * 是否安装
	 *
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isInstallApp(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
			ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
}
