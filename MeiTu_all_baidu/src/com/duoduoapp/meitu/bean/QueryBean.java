package com.duoduoapp.meitu.bean;

import java.io.Serializable;
import java.net.URLEncoder;

import com.duoduoapp.meitu.itf.IData;
import com.duoduoapp.meitu.utils.Logger;

/**
 * 查询参数
 * 
 * @author duoduoapp
 * 
 */
public class QueryBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 84649161L;
	private String t1 = ""; // 种类 热门,美女,汽车之类的
	private String sn = "";// 从哪里加载
	private String pn = IData.DEFAULT_PN; // 加载多少张
	private String listtype = IData.DEFAULT_LISTTYPE; // 类型 new or hot
	private String ch = "wallpaper"; // 不变
	private String src = "srp"; // 不变
	private String q = ""; // 查询关键字
	private String id = ""; // id ,显示一组图片时用上

	public QueryBean() {
	}

	/**
	 * 显示界面
	 * 
	 * @param t1
	 *            显示种类
	 * @param listtype
	 *            热门或者最新
	 */
	public QueryBean(String t1, String listtype) {
		this.t1 = t1;
		this.listtype = listtype;
	}

	/**
	 * 查询关键字
	 * 
	 * @param q
	 */
	public QueryBean(String q) {
		this.q = q;
	}

	public String getT1() {
		return t1;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}

	public String getListtype() {
		return listtype;
	}

	public void setListtype(String listtype) {
		this.listtype = listtype;
	}

	public String getCh() {
		return ch;
	}

	public String getSrc() {
		return src;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String toString() {
		String toString = "";
		toString = "ch=" + ch + "&src=" + src + (listtype == null || "".equals(listtype) ? "" : "&listtype=" + listtype)
				+ (t1 == null || "".equals(t1) ? "" : "&t1=" + t1) + (sn == null || "".equals(sn) ? "" : "&sn=" + sn)
				+ (pn == null || "".equals(pn) ? "" : "&pn=" + pn) + (id == null || "".equals(id) ? "" : "&id=" + id)
				+ (q == null || "".equals(q) ? "" : "&q=" + URLEncoder.encode(q));
		Logger.debug(toString);
		return toString;
	}
}
