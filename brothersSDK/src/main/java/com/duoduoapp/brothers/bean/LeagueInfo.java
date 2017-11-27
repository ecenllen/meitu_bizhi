package com.duoduoapp.brothers.bean;

import android.graphics.drawable.Drawable;

/**
 * 联盟信息
 * 
 * @author Shanlin
 * 
 */
public class LeagueInfo {

	private int id; // id
	private String nextPage = "0"; // 是否有下一页 0是没有1是有
	private String imgUrl = ""; // 应用图片信息
	private String appName = ""; // 应用名字
	private String packageName = ""; // 应用包名
	private String describe = ""; // 描述
	private String haveApp = "0"; // 本机是否含有这个app 0 没有 1 有
	private Drawable icon = null; // 有这个app就使用本地图片
	private String user = ""; // 发布者名字

	public LeagueInfo() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getHaveApp() {
		return haveApp;
	}

	public void setHaveApp(String haveApp) {
		this.haveApp = haveApp;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		String to = getId() + "\t" + getAppName() + "\t" + getPackageName() + "\t" + getImgUrl() + "\t" + getDescribe();
		return to;
	}
}
