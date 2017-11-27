package com.duoduoapp.meitu.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuminer on 2017/3/23.
 */
public class ConfigBean {
//    public String videosourceVersion = "";//当前会加载那些视频渠道的视频
//    public String selfadVersion = "";
//    public String wxgzhversion = "";
//    public String goodPinglunVersion = "";
//    public String onlineVideoParseVersion = "";//在线视频解析地址的jar的version
   public UpdateBean updatemsg=new UpdateBean();
    public Map<String, String> ad_banner_idMap = new HashMap<String, String>();
    public Map<String, String> ad_kp_idMap = new HashMap<String, String>();
    public Map<String, String> ad_cp_idMap = new HashMap<String, String>();
    public Map<String, String> ad_tp_idMap = new HashMap<String, String>();
    public String cpuidorurl="";//baidu的内容联盟的链接或者id，如果是链接，直接解析
    public String nomeinvchannel = "";
    public String nocpuadchannel = "";//没有内容联盟内容的渠道
    public String nofenxiang = "";
    public String nosearch = "";
    public String nohaoping = "";
    public String noadbannerchannel = "";//没广告条的版本
    public String noadkpchannel = "";//没有开屏的版本
    public String noadtpchannel = "";//没有开屏的版本
    public String noadcpchannel = "";//没有插屏的版本
    public String noupdatechannel = "";
    public String noselfadchannel = "";
    public String noaddvideochannel = "";
    public String noadVideowebchannel = "";//使用第三方去广告链接对web地址进行转化播放，这时候是给浏览器播放
    public String playonwebchannel = "";//使用网页播放的版本
    public String nogzhchannel = "";
    public String bannertype = "";
    public String cptype = "";
    public String kptype = "";
    public String tptype = "";



}
