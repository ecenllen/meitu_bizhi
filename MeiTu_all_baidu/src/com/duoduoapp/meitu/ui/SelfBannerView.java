package com.duoduoapp.meitu.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.duoduoapp.meitu.utils.ADBean;
import com.duoduoapp.meitu.utils.AppConfig;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hlxwdsj.bj.vz.R;


import java.util.List;

public class SelfBannerView extends RelativeLayout {
    private SelfBannerAdListener listener;
    private SimpleDraweeView my_image_view;
    private View rl_content, ad_close;
    ADBean bean;

    public SelfBannerView(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.selfbannerview, this);
        my_image_view = (SimpleDraweeView) findViewById(R.id.my_image_view);
        rl_content = findViewById(R.id.rl_content);
        ad_close = findViewById(R.id.ad_close);
        List<ADBean> beans = AppConfig.GetSelfADByCount(context, 1, "banner_count");
        if (null != beans && beans.size() == 1) {
            bean = beans.get(0);
        }
        rl_content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onAdClick(bean);
                AppConfig.openAD(context, bean, "banner_count");
            }
        });
        ad_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_content.setVisibility(View.GONE);
                System.out.println("广告被关闭");
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (bean != null) {
            my_image_view.setImageURI(bean.getAd_banner());
//			my_image_view.setImageURI("http://s10.sinaimg.cn/bmiddle/4539a220g7c24593c3849&690");
            System.out.println("bean.getAd_banner():" + bean.getAd_banner());
            if (listener != null)
                listener.onADReceiv(bean);
        } else {
            if (listener != null)
                listener.onAdFailed();
            rl_content.setVisibility(View.GONE);
        }
    }

    public void setADListener(SelfBannerAdListener listener) {
        this.listener = listener;
    }

}