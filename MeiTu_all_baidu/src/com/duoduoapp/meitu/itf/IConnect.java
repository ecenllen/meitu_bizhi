package com.duoduoapp.meitu.itf;

import java.io.IOException;

import com.duoduoapp.meitu.bean.QueryBean;

public interface IConnect{

	

	/**
	 * 搜索关键字,返回json
	 * 
	 * @param param
	 * @return
	 * @throws IOException 
	 */
	public String imageSearch(QueryBean param) throws IOException;

	/**
	 * 根据t1和listtype 获得图片的集合
	 * <BR>
	 * @param param
	 * @return
	 * @throws IOException 
	 */
	public String imageLists(String url, QueryBean param) throws IOException;

	/**
	 * 点击 一组图片显示,一组图片
	 * 
	 * @param param
	 * @return
	 * @throws IOException 
	 */
	public String imageGroup(String url, QueryBean param) throws IOException;

	/**
	 * 图片下载
	 * 
	 * @param url
	 *            下载地址
	 * @param name
	 *            名字
	 * @return
	 */
	public String imageDownLoad(String url, String name);
}
