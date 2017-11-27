package com.duoduoapp.meitu.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.duoduoapp.meitu.bean.GroupBean;
import com.duoduoapp.meitu.bean.GroupListBean;
import com.duoduoapp.meitu.bean.MainListBean;
import com.duoduoapp.meitu.bean.QueryBean;
import com.duoduoapp.meitu.bean.SearchBean;
import com.duoduoapp.meitu.bean.SearchListBean;
import com.duoduoapp.meitu.bean.ShowBean;
import com.duoduoapp.meitu.bean.ShowListBean;
import com.duoduoapp.meitu.itf.Connect;
import com.duoduoapp.meitu.itf.IData;

/**
 * 解析网上数据 类
 * 
 * @author duoduoapp
 * 
 */
public class DataUtil {

	private static Connect connect;

	static {
		connect = Connect.getInstance();
	}

	/**
	 * 初始化数据
	 */
	public static void initAll() {
		FileUtil.creatFolder(IData.DEFAULT_IMAGE_CACHE);
		FileUtil.creatFolder(IData.DEFAULT_IMAGE_FOLDER);
	}

	// //////////////////////主页显示数据,包括各个标签的////////////////////////////

	public static List<ShowBean> getShowBeans(QueryBean param) throws IOException, JSONException {
		String json = connect.imageLists(IData.URL_SHOW_LIST, param);
		return jxShowBeans(json);
	}

	private static List<ShowBean> jxShowBeans(String json) throws IOException, JSONException {
		List<ShowBean> showBeans = new ArrayList<ShowBean>();
		List<ShowListBean> showListBeans = new ArrayList<ShowListBean>();
		JSONObject jObject = new JSONObject(json);
		ShowBean showBean = new ShowBean();
		showBean.setEnd(jObject.getBoolean("end"));
		showBean.setCount(jObject.getInt("count"));
		showBean.setLastid(jObject.getInt("lastid"));
		if (!"null".equals(jObject.get("list"))) {
			JSONArray jArray = jObject.getJSONArray("list");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject jo = jArray.getJSONObject(i);
				ShowListBean listBean = new ShowListBean();
				listBean.setId(jo.getString("id"));
				listBean.setImageid(jo.getString("imageid"));
				listBean.setGroup_title(jo.getString("group_title"));
				listBean.setCover_imgurl(jo.getString("cover_imgurl"));
				listBean.setTotal_count(jo.getString("total_count"));
				listBean.setQhimg_thumb_url(jo.getString("qhimg_thumb_url"));
				showListBeans.add(listBean);
			}
		}
		showBean.setShowListBeans(showListBeans);
		showBeans.add(showBean);

		return showBeans;
	}

	// /////////////////////点击每组图片的/////////////////////////////////////////

	public static List<GroupBean> getGroupBeans(QueryBean param) throws IOException, JSONException {
		String json = connect.imageLists(IData.URL_GROUP_LIST, param);
		return jxGroupBeans(json);
	}

	public static List<GroupBean> getGroupBeans(String url, QueryBean param) throws IOException, JSONException {
		String json = connect.imageLists(url, param);
		return jxGroupBeans(json);
	}

	private static List<GroupBean> jxGroupBeans(String json) throws IOException, JSONException {
		List<GroupBean> showBeans = new ArrayList<GroupBean>();
		List<GroupListBean> showListBeans = new ArrayList<GroupListBean>();
		JSONObject jObject = new JSONObject(json);
		GroupBean showBean = new GroupBean();
		showBean.setGroup_title(jObject.getString("group_title"));
		if (!"null".equals(jObject.get("list"))) {
			JSONArray jArray = jObject.getJSONArray("list");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject jo = jArray.getJSONObject(i);
				GroupListBean listBean = new GroupListBean();
				listBean.setGroup_id(jo.getString("group_id"));
				listBean.setDownurl(jo.getString("downurl"));
				listBean.setImageid(jo.getString("imageid"));
				listBean.setPic_size(jo.getString("pic_size"));
				listBean.setPic_title(jo.getString("pic_title"));
				listBean.setPic_url(jo.getString("pic_url"));

				showListBeans.add(listBean);
			}
		}
		showBean.setGroupListBeans(showListBeans);
		showBeans.add(showBean);

		return showBeans;
	}

	// 搜索图片数据/////////////////////////////////////////////

	public static List<SearchBean> getSearchBeans(QueryBean param) throws IOException, JSONException {
		String json = connect.imageLists(IData.URL_SEARCH, param);
		return jxSearchBeans(json);
	}

	private static List<SearchBean> jxSearchBeans(String json) throws IOException, JSONException {
		List<SearchBean> showBeans = new ArrayList<SearchBean>();
		List<SearchListBean> showListBeans = new ArrayList<SearchListBean>();
		JSONObject jObject = new JSONObject(json);
		SearchBean showBean = new SearchBean();
		showBean.setTotal(jObject.getString("total"));
		showBean.setEnd(jObject.getBoolean("end"));
		showBean.setLastindex(jObject.getString("lastindex"));
		if (!"null".equals(jObject.get("list"))) {
			JSONArray jArray = jObject.getJSONArray("list");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject jo = jArray.getJSONObject(i);
				SearchListBean listBean = new SearchListBean();
				listBean.setId(jo.getString("id"));
				listBean.setTitle(jo.getString("title"));
				listBean.setImgsize(jo.getString("imgsize"));
				listBean.set_thumb(jo.getString("_thumb"));
				listBean.setGrpcnt(jo.getString("grpcnt"));
				listBean.setImg(jo.getString("img"));
				listBean.setImgtype(jo.getString("imgtype"));
				listBean.setThumb(jo.getString("thumb"));
				showListBeans.add(listBean);
			}
		}
		showBean.setSearchListBeans(showListBeans);
		showBeans.add(showBean);
		return showBeans;
	}

	/**
	 * 
	 * @param url
	 * @param name
	 * @return
	 */
	public static String downLoadImage(String url, String name) {
		return connect.imageDownLoad(url, name);
	}

	public static List<MainListBean> getBaiduBizhi(String url) throws IOException,JSONException{
		String result=connect.getBaiduSearchResult(IData.BAIDU_BIZHI_LIAN_JIE,url);
		return getBaiduBizhiResult(result);
	}

	private static List<MainListBean> getBaiduBizhiResult(String result) throws JSONException {
		List<MainListBean> list=new ArrayList<>();
		JSONObject object=new JSONObject(result);
		int response=object.getInt("bdIsClustered");
		if(response==1){
			JSONArray array=object.getJSONArray("data");
			if(array!=null&&array.length()>0){
				for(int i=0;i<array.length();i++){
					JSONObject bean= (JSONObject) array.get(i);
					MainListBean main=new MainListBean();
					if(bean!=null&&bean.has("hoverURL")){
						main.setHoverURL(bean.getString("hoverURL"));
					}if(bean!=null&&bean.has("middleURL")){
						main.setMiddleURL(bean.getString("middleURL"));
					}if(bean!=null&&bean.has("thumbURL")){
						main.setThumbURL(bean.getString("thumbURL"));
					}
					if(main.getHoverURL()!=null||main.getThumbURL()!=null||main.getMiddleURL()!=null){
						list.add(main);
					}

				}
			}
		}
		return list;
	}
}
