package com.duoduoapp.meitu.bean;

import java.io.Serializable;

public class GroupListBean  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 32165346101L;
	private String imageid = "";
	private String group_id = "";
	private String pic_url = "";
	private String pic_size = "";
	private String pic_title = "";
	private String downurl = "";

	public String getImageid() {
		return imageid;
	}

	public void setImageid(String imageid) {
		this.imageid = imageid;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public String getPic_size() {
		return pic_size;
	}

	public void setPic_size(String pic_size) {
		this.pic_size = pic_size;
	}

	public String getPic_title() {
		return pic_title;
	}

	public void setPic_title(String pic_title) {
		this.pic_title = pic_title;
	}

	public String getDownurl() {
		return downurl;
	}

	public void setDownurl(String downurl) {
		this.downurl = downurl;
	}

}
