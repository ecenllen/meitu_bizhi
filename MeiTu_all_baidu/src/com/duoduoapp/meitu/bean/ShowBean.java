package com.duoduoapp.meitu.bean;

import com.duoduoapp.meitu.utils.ADBean;

import java.io.Serializable;
import java.util.List;

public class ShowBean extends ADBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5613268711L;
	private boolean isEnd = false; // 是否结束
	private int count = 0; // 个数
	private int lastid = 0; // 下一个id
	private List<ShowListBean> showListBeans; // 图片集合

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getLastid() {
		return lastid;
	}

	public void setLastid(int lastid) {
		this.lastid = lastid;
	}

	public List<ShowListBean> getShowListBeans() {
		return showListBeans;
	}

	public void setShowListBeans(List<ShowListBean> showListBeans) {
		this.showListBeans = showListBeans;
	}

}
