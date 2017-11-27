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
import android.widget.RelativeLayout;

import com.hlxwdsj.bj.vz.R;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeAD;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;

import java.util.List;
import java.util.Random;

public class GDTMuBanTuiPingDialog extends Dialog {
    private SelfBannerAdListener listener;
    //    private SimpleDraweeView my_image_view;
    private RelativeLayout rl_content;
    Button bt_quit, bt_look;
    Context context;
    //    ADBean bean;
    Boolean ischange;
    NativeExpressADView adItem;

    public GDTMuBanTuiPingDialog(final Context context) {
        super(context, R.style.dialog);
        this.context = context;

//        List<ADBean> beans = AppConfig.GetSelfADByCount(context, 1, "tp_count");
//        if (null != beans && beans.size() == 1) {
//            bean = beans.get(0);
//        }

    }

    public static List<NativeExpressADView> adItems;
    public static NativeAD nativeAD;
    public static long initTime = 0;

    public static void Init(Context context, String appid, String sid) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.8);
        int height = (int) (0.8 * width);
        ADSize adSize = new ADSize(width, height);

        NativeExpressAD nativeExpressAD =
                new NativeExpressAD(context, adSize, appid, sid, new NativeExpressAD.NativeExpressADListener() {
                    @Override
                    public void onNoAD(AdError adError) {
                        GDTMuBanTuiPingDialog.adItems = null;
                    }

                    @Override
                    public void onADLoaded(List<NativeExpressADView> list) {
                        GDTMuBanTuiPingDialog.adItems = list;
                        initTime = System.currentTimeMillis();
                    }

                    @Override
                    public void onRenderFail(NativeExpressADView nativeExpressADView) {

                    }

                    @Override
                    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

                    }

                    @Override
                    public void onADExposure(NativeExpressADView nativeExpressADView) {

                    }

                    @Override
                    public void onADClicked(NativeExpressADView nativeExpressADView) {

                    }

                    @Override
                    public void onADClosed(NativeExpressADView nativeExpressADView) {
                        GDTMuBanTuiPingDialog.adItems.remove(nativeExpressADView);
                    }

                    @Override
                    public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

                    }

                    @Override
                    public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

                    }
                });
        nativeExpressAD.loadAD(10);
    }

    View.OnClickListener btListen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_look:
                    if (ischange) {//去看看
//                        if (listener != null)
//                            listener.onAdClick(bean);
//                        AppConfig.openAD(context, bean, "tp_count");

                        GDTMuBanTuiPingDialog.this.dismiss();
                    } else {
                        ((Activity) context).finish();
                    }
                    break;
                case R.id.bt_quit:
                    if (ischange) {
                        ((Activity) context).finish();
                    } else {//去看看
//                        if (listener != null)
//                            listener.onAdClick(bean);
//                        AppConfig.openAD(context, bean, "tp_count");

                        GDTMuBanTuiPingDialog.this.dismiss();
                    }
                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.selfmubantuipingdialog, null);
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
            bt_look.setText("取消");
        } else {
            bt_quit.setText("取消");
            bt_look.setText("确定退出");
        }

//        my_image_view = (SimpleDraweeView) view.findViewById(R.id.my_image_view);
        rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);

        rl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (listener != null)
//                    listener.onAdClick(bean);
//                AppConfig.openAD(context, bean, "tp_count");
            }
        });

//		Window dialogWindow = getWindow();
//		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay(); // 获取屏幕宽、高用
        if (adItems != null && adItems.size() > 0) {
            int index = new Random(System.currentTimeMillis()).nextInt(adItems.size());
            adItem = adItems.get(index);
            ViewGroup.LayoutParams params = rl_content.getLayoutParams();
            params.width = (int) (display.getWidth() * 0.95);
            params.height = (int) (0.8 * params.width);
            rl_content.setLayoutParams(params);

            if (rl_content.getChildCount() > 0) {
                rl_content.removeAllViews();
            }

            ViewGroup parent = (ViewGroup) adItem.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            adItem.setVisibility(View.VISIBLE);
            rl_content.addView(adItem);
            adItem.render();
        } else {
//            my_image_view.setVisibility(View.GONE);

//            findViewById(R.id.ad_log).setVisibility(View.GONE);

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