package com.duoduoapp.meitu.ui;


import com.duoduoapp.meitu.utils.ADBean;

/**
 * Created by yuminer on 2017/5/26.
 */
public interface SelfKPAdListener {
    void onAdPresent(ADBean bean);

    void onAdDismissed(ADBean bean);

    void onAdFailed(ADBean bean);

    void onAdClick(ADBean bean);
}
