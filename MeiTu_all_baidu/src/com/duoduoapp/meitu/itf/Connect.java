package com.duoduoapp.meitu.itf;

import java.io.IOException;
import java.util.Set;

import com.duoduoapp.meitu.bean.QueryBean;
import com.duoduoapp.meitu.utils.HttpUtil;

public class Connect implements IConnect {

	private static Connect connect;

	private Connect() {
	}

	public static Connect getInstance() {
		if (connect == null) {
			synchronized (Connect.class) {
				if (connect == null) {
					connect = new Connect();
				}
			}
		}
		return connect;
	}

	// static块,初始化数据
	static {
		IData.MAP_T1.put("92", "热门");
		IData.MAP_T1.put("93", "美女");
		IData.MAP_T1.put("156", "明星");
		IData.MAP_T1.put("157", "风景");
		IData.MAP_T1.put("158", "小清新");
		IData.MAP_T1.put("159", "酷车");
		IData.MAP_T1.put("160", "设计");
		IData.MAP_T1.put("161", "爱情");
		IData.MAP_T1.put("162", "植物");
		IData.MAP_T1.put("163", "动物");
		IData.MAP_T1.put("164", "动漫");
		// MAP_T1.put("165", "星空");
		IData.MAP_T1.put("166", "酷炫");
		IData.MAP_T1.put("167", "军事");
		IData.MAP_T1.put("168", "摄影");
		IData.MAP_T1.put("169", "游戏");
		IData.MAP_T1.put("170", "影视");
		IData.MAP_T1.put("171", "手绘");
		IData.MAP_T1.put("172", "体育");
		Set<String> keys = IData.MAP_T1.keySet();
		IData.T1_LIST.clear();
		for (String key : keys) {
			IData.MAP_T1_FAN.put(IData.MAP_T1.get(key), key);
			IData.T1_LIST.add(IData.MAP_T1.get(key));
		}
	}

	@Override
	public String imageSearch(QueryBean param) throws IOException {
		// TODO Auto-generated method stub
		return new HttpUtil().getHttp(IData.URL_SEARCH + param.toString());
	}

	@Override
	public String imageLists(String url, QueryBean param) throws IOException {
		// TODO Auto-generated method stub
		return new HttpUtil().getHttp(url + param.toString());
	}

	@Override
	public String imageGroup(String url, QueryBean param) throws IOException {
		// TODO Auto-generated method stub
		return new HttpUtil().getHttp(url + param.toString());
	}

	@Override
	public String imageDownLoad(String url, String name) {
		// TODO Auto-generated method stub
		return HttpUtil.httpDownLoad(url, name);
	}

	public String getBaiduSearchResult(String url, String params) throws IOException {
		return new HttpUtil().getHttp(url+params);
	}
}
