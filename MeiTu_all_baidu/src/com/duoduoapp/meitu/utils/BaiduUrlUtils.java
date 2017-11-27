package com.duoduoapp.meitu.utils;

/**
 * Created by dkli on 2017/11/22.
 */

public class BaiduUrlUtils {

    public static String getBaiduUrl(int pn,String keyWord){
        return "tn=resultjson_com&ipn=rj&ct=201326592&is=&fp=result&queryWord=壁纸 "+keyWord+"&cl=&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=&ic=0&word=壁纸 "+keyWord+"&s=&se=&tab=&width=1080&height=1920&face=0&istype=2&qc=&nc=&fr=&cg=wallpaper&pn="+pn+"&rn=20&gsm=1a4&1511169326145=";
    }
}
