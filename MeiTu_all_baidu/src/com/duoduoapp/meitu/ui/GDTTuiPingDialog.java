package com.duoduoapp.meitu.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hlxwdsj.bj.vz.R;
import com.qq.e.ads.nativ.NativeAD;
import com.qq.e.ads.nativ.NativeADDataRef;
import com.qq.e.comm.util.AdError;

import java.util.List;
import java.util.Random;

public class GDTTuiPingDialog extends Dialog {
    private SelfBannerAdListener listener;
    private SimpleDraweeView my_image_view;
    private View rl_content, ad_close;
    Button bt_quit, bt_look;
    Context context;
    //    ADBean bean;
    Boolean ischange;
    NativeADDataRef adItem;

    public GDTTuiPingDialog(final Context context) {
        super(context, R.style.dialog);
        this.context = context;

//        List<ADBean> beans = AppConfig.GetSelfADByCount(context, 1, "tp_count");
//        if (null != beans && beans.size() == 1) {
//            bean = beans.get(0);
//        }

    }
    public static List<NativeADDataRef> adItems;
    public static NativeAD nativeAD;
    public static long initTime=0;

    public static void Init(Context context,String appid,String sid) {
        GDTTuiPingDialog.nativeAD = new NativeAD(context, appid, sid, new NativeAD.NativeAdListener() {
            @Override
            public void onADLoaded(List<NativeADDataRef> list) {
                GDTTuiPingDialog.adItems = list;
                initTime=System.currentTimeMillis();
            }

            @Override
            public void onNoAD(AdError adError) {
                GDTTuiPingDialog.adItems=null;
            }


            @Override
            public void onADStatusChanged(NativeADDataRef nativeADDataRef) {

            }

            @Override
            public void onADError(NativeADDataRef nativeADDataRef, AdError adError) {
                GDTTuiPingDialog.adItems=null;
            }

        });
        GDTTuiPingDialog.nativeAD.loadAD(5);
    }

    View.OnClickListener btListen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_look:
                    if (ischange) {
//                        if (listener != null)
//                            listener.onAdClick(bean);
//                        AppConfig.openAD(context, bean, "tp_count");
                        if(adItem!=null){
                        adItem.onClicked(v);
                        }
                        GDTTuiPingDialog.this.dismiss();
                    } else {
                        ((Activity) context).finish();
                    }
                    break;
                case R.id.bt_quit:
                    if (ischange) {
                        ((Activity) context).finish();
                    } else {
//                        if (listener != null)
//                            listener.onAdClick(bean);
//                        AppConfig.openAD(context, bean, "tp_count");
                        if(adItem!=null)
                        adItem.onClicked(v);
                        GDTTuiPingDialog.this.dismiss();
                    }
                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.selftuipingdialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        setCanceledOnTouchOutside(false);
//		setCancelable(false);
        bt_look = (Button) findViewById(R.id.bt_look);
        bt_quit = (Button) findViewById(R.id.bt_quit);
        bt_quit.setOnClickListener(btListen);
        bt_look.setOnClickListener(btListen);
        ischange = new Random(System.currentTimeMillis()).nextBoolean();
        if (ischange) {
            bt_quit.setText("确定退出");
            bt_look.setText("去看看");
        } else {
            bt_quit.setText("去看看");
            bt_look.setText("确定退出");
        }

        my_image_view = (SimpleDraweeView) view.findViewById(R.id.my_image_view);
        rl_content = view.findViewById(R.id.rl_content);
        ad_close = view.findViewById(R.id.ad_close);
        ad_close.setVisibility(View.GONE);
        rl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (listener != null)
//                    listener.onAdClick(bean);
//                AppConfig.openAD(context, bean, "tp_count");
            }
        });
        ad_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GDTTuiPingDialog.this.dismiss();
                System.out.println("广告被关闭");
            }
        });
//		Window dialogWindow = getWindow();
//		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay(); // 获取屏幕宽、高用
        if (adItems != null && adItems.size() > 0) {
            int index = new Random(System.currentTimeMillis()).nextInt(adItems.size());
            adItem = adItems.get(index);
            ViewGroup.LayoutParams params = my_image_view.getLayoutParams();
            params.width = (int) (display.getWidth() * 0.8);
            params.height = (int) (0.56 * params.width);
            my_image_view.setLayoutParams(params);
            my_image_view.setImageURI(adItem.getImgUrl());
            System.out.println("图片:"+adItem.getImgUrl());
            adItem.onExposured(my_image_view);
        }else{
            my_image_view.setVisibility(View.GONE);
            ad_close.setVisibility(View.GONE);
            findViewById(R.id.ad_log).setVisibility(View.GONE);

            findViewById(R.id.tvMsg).setVisibility(View.VISIBLE);
            if (ischange) {
                bt_quit.setText("确定退出");
                bt_look.setText("取消");
            } else {
                bt_quit.setText("取消");
                bt_look.setText("确定退出");
            }

        }

//		lp.width = (int) (display.widthPixels * 0.8); // 高度设置为屏幕的0.6
//		dialogWindow.setAttributes(lp);
//        if (bean != null) {
////            ViewGroup.LayoutParams params = my_image_view.getLayoutParams();
////            params.width = (int) (display.getWidth() * 0.8);
////            params.height = (int) (bean.getAd_thumbnailscal() * params.width);
////            my_image_view.setLayoutParams(params);
//
//            my_image_view.setImageURI(bean.getAd_thumbnail());
//            if (listener != null)
//                listener.onADReceiv(bean);
//        } else {
//            if (listener != null)
//                listener.onAdFailed();
//            this.dismiss();
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setADListener(SelfBannerAdListener listener) {
        this.listener = listener;
    }

}