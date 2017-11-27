package com.duoduoapp.meitu.bean;

import java.io.Serializable;
import java.util.List;

public class GroupBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 843218101L;

	private String group_title = "";

	private List<GroupListBean> groupListBeans; // 数据集合

	public String getGroup_title() {
		return group_title;
	}

	public void setGroup_title(String group_title) {
		this.group_title = group_title;
	}

	public List<GroupListBean> getGroupListBeans() {
		return groupListBeans;
	}

	public void setGroupListBeans(List<GroupListBean> groupListBeans) {
		this.groupListBeans = groupListBeans;
	}

}
