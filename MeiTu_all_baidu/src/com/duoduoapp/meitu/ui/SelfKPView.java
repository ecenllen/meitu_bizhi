package com.duoduoapp.meitu.ui;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duoduoapp.meitu.utils.ADBean;
import com.duoduoapp.meitu.utils.AppConfig;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hlxwdsj.bj.vz.R;

import java.util.List;

public class SelfKPView extends RelativeLayout {
    private SelfKPAdListener listener;
    private SimpleDraweeView my_image_view;
    private View rl_content;
    private TextView tv_close;
    private Context context;
    ADBean bean;

    boolean isdismiss = false;

    public SelfKPView(final Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.selfkpview, this);
        my_image_view = (SimpleDraweeView) findViewById(R.id.my_image_view);
        rl_content = findViewById(R.id.rl_content);
        tv_close = (TextView) findViewById(R.id.tv_close);
        List<ADBean> beans = AppConfig.GetSelfADByCount(context, 1, "kp_count");
        if (null != beans && beans.size() == 1) {
            bean = beans.get(0);

        }
        rl_content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onAdClick(bean);
                AppConfig.openAD(context, bean, "kp_count");
            }
        });
        tv_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (!isdismiss) {
                        isdismiss = true;
                        listener.onAdDismissed(bean);
                    }
                }

            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (bean != null) {
            my_image_view.setImageURI(bean.getAd_kp());
            if (listener != null)
                listener.onAdPresent(bean);
            mCountDownTimer.start();

        } else {
            if (listener != null)
                listener.onAdFailed(bean);
            rl_content.setVisibility(View.GONE);
        }
    }

    public void setADListener(SelfKPAdListener listener) {
        this.listener = listener;
    }

    int i = 10;
    private CountDownTimer mCountDownTimer = new CountDownTimer(5 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (isdismiss)//在别的地方关闭的
                cancel();
            else
                tv_close.setText(millisUntilFinished / 1000 + "");
            ;
        }

        @Override
        public void onFinish() {
            isdismiss = true;
            listener.onAdDismissed(bean);

        }
    };


}