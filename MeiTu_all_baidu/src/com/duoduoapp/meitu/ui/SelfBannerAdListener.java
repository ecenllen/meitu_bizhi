package com.duoduoapp.meitu.ui;


import com.duoduoapp.meitu.utils.ADBean;

/**
 * Created by yuminer on 2017/5/26.
 */
public interface SelfBannerAdListener {
    void onAdClick(ADBean adBean);
    void onAdFailed();
    void onADReceiv(ADBean adBean);
}
