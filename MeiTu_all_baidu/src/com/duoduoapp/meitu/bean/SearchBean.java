package com.duoduoapp.meitu.bean;

import java.io.Serializable;
import java.util.List;

public class SearchBean implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4568130548521L;
	private String total = ""; // 总数
	private boolean end = false; // 是否结束
	private String lastindex = ""; //
	private List<SearchListBean> searchListBeans;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public String getLastindex() {
		return lastindex;
	}

	public void setLastindex(String lastindex) {
		this.lastindex = lastindex;
	}

	public List<SearchListBean> getSearchListBeans() {
		return searchListBeans;
	}

	public void setSearchListBeans(List<SearchListBean> searchListBeans) {
		this.searchListBeans = searchListBeans;
	}

}
