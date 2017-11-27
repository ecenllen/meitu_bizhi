package com.duoduoapp.brothers.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.duoduoapp.brothers.bean.LeagueInfo;

/**
 * 解析jison文件
 * @author Shanlin
 *
 */
public class JsonUtil {

	public static final String TAG = JsonUtil.class.getName();

	/**
	 * 获取json中的信息
	 * @param json
	 * @return List
	 */
	public static List<LeagueInfo> getLeagueInfos(String json){
		List<LeagueInfo> infos = new ArrayList<LeagueInfo>();
		try {
			JSONObject jo = new JSONObject(json);
			JSONArray jArray = jo.getJSONArray("league");
			for (int i = 0; i < jArray.length(); i++) {
				LeagueInfo info = new LeagueInfo();
				JSONObject jObject = (JSONObject) jArray.get(i);
				info.setId(jObject.getInt("id"));
				info.setAppName(jObject.getString("appName"));
				info.setDescribe(jObject.getString("describe"));
				info.setImgUrl(jObject.getString("imgUrl"));
				info.setNextPage(jObject.getString("nextPage"));
				info.setPackageName(jObject.getString("packageName"));
				info.setUser(jObject.getString("user"));
				Log.e(TAG, "info" + info);
				infos.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
			infos = null;
		}
		
		return infos ;
	}
}
