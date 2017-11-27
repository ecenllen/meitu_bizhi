package com.duoduoapp.meitu.bean;

import android.support.v4.app.Fragment;

/**
 * 左边侧滑类<br>
 * 显示菜单
 * 
 * @author yuxu
 * 
 */
public class SlidingLeftBean {

	private String name; // 菜单名字
	private Fragment fragment; // 菜单跳转的fragment类

	public SlidingLeftBean() {

	}

	public SlidingLeftBean(String name, Fragment fragment) {
		this.name = name;
		this.fragment = fragment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

}
