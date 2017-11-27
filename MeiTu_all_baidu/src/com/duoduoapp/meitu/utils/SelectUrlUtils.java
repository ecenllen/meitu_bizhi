package com.duoduoapp.meitu.utils;

import com.duoduoapp.meitu.bean.MainListBean;

/**
 * Created by dkli on 2017/11/24.
 */

public class SelectUrlUtils {
    public static String selectUrl(MainListBean bean){
        if(bean.getHoverURL()!=null&&bean.getHoverURL().length()>0){
            return bean.getHoverURL();
        }else if(bean.getMiddleURL()!=null&&bean.getMiddleURL().length()>0){
            return bean.getMiddleURL();
        }
        return bean.getThumbURL();
    }
}
