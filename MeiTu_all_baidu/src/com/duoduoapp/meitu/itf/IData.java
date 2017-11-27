package com.duoduoapp.meitu.itf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Environment;

/**
 * 存放数据的地方
 * 
 * @author duoduoapp
 * 
 */
public class IData {

	public static final String EXTRA_DATA = "EXTRA_DATA"; // 数据
	public static final String EXTRA_TYPE = "EXTRA_TYPE"; // 类型  通知首页刷新
	public static final String EXTRA_SHOW = "EXTRA_SHOW"; // show数据
	public static final String EXTRA_MANAGER = "EXTRA_MANAGER"; // manager数据
	public static final String EXTRA_SEARCH = "EXTRA_SEARCH"; // search数据

	public static final String[] CONTENT = new String[] { "热门", "最新" };

	public static final String ACTION_TYPE = "com.duoduoapp.meitu.ACTION_TYPE";
	public static final String ACTION_IMAGE_CHANGED = "com.duoduoapp.meitu.ACTION_IMAGE_CHANGED";

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 默认搜索,刷新图片个数10个
	 */
	public static final String DEFAULT_PN = "20";
	public static final String LISTTYPE_HOT = "hot";
	public static final String LISTTYPE_NEW = "new";
	public static final String DEFAULT_LISTTYPE = LISTTYPE_HOT;

	/**
	 * 显示图片list的,后面加listtype 和 t1 就可得到List Json <br>
	 * 如:http://image.so.com/zj?ch=wallpaper&listtype=hot&t1=93 美女最热
	 */
	public static final String URL_SHOW_LIST = "http://image.so.com/zj?";
	/**
	 * 显示点击每一张图片显示的内容, <br>
	 * 如:http://image.so.com/zvj?ch=wallpaper&t1=93&listtype=hot&id=
	 * ad0b9015e3bc98ec1f7cf28c5ee36005
	 */
	public static final String URL_GROUP_LIST = "http://image.so.com/zvj?";
	/**
	 * 搜索关键字 <br>
	 * 如:http://image.so.com/j?q=hehe&src=srp&sn=60&pn=30
	 */
	public static final String URL_SEARCH = "http://image.so.com/j?";
	/**
	 * 图片请求Referer内容,360做了防盗链的设置
	 */
	public static final String BASE_REFERER = "http://image.so.com/z?";

	public static String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
	/**
	 * 本地美图保存地址 <br>
	 * 格式 {@link IData#DEFAULT_IMAGE_FOLDER}+"/93/xxxxx.jpg"
	 */
	public static String DEFAULT_IMAGE_FOLDER = SDCARD + "/duoduo/meitu/";

	/**
	 * 图片缓存地址
	 */
	public static String DEFAULT_IMAGE_CACHE = SDCARD + "/duoduo/cache/";

	/**
	 * t1集合,key = id ,valuse = name
	 */
	public static HashMap<String, String> MAP_T1 = new HashMap<String, String>();
	/**
	 * t1的逆向集合,key = name ,valuse = id
	 */
	public static HashMap<String, String> MAP_T1_FAN = new HashMap<String, String>();
	/**
	 * 种类名字集合
	 */
	public static List<String> T1_LIST = new ArrayList<String>();

	/**
	 * 本地保存地址 <br>
	 */
	public static String DEFAULT_DOWNLOADE_FOLDER = SDCARD + "/tv1/player/download/";

	/**
	 * 缓存地址
	 */
	public static String DEFAULT_DISK_CACHE = SDCARD + "/tv1/player/cache/";
	/**
	 * 保存文件的
	 */
	public static String DEFAULT_FILES_CACHE = SDCARD + "/tv1/player/files/";

	/**
	 * 保存apk的
	 */
	public static String DEFAULT_APK_CACHE = SDCARD + "/tv1/player/apk/";
	/**
	 * 保存公众号等的图片等信息
	 */
	public static String DEFAULT_GZH_CACHE = SDCARD + "/tv1/player/gzh/";
	/**
	 * 百度壁纸链接
	 */
	public static String BAIDU_BIZHI_LIAN_JIE="https://image.baidu.com/search/acjson?";
}
