package com.duoduoapp.meitu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.duoduoapp.brothers.BaseActivity;
import com.duoduoapp.meitu.itf.IData;
import com.duoduoapp.meitu.ui.GZHAddActivity;
import com.duoduoapp.meitu.utils.ADControl;
import com.duoduoapp.meitu.utils.AppConfig;
import com.duoduoapp.meitu.utils.WXGZHBean;
import com.hlxwdsj.bj.vz.R;


import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 关于界面
 *
 * @author Administrator
 */
public class TVAboutActivity extends BaseActivity{

    private LinearLayout la_ding, la_cai, la_xiongdi, title_layout_back, la_update, la_re, la_jingpin, la_guanzhu, la_tuijian, la_fenxiang;
    private View v_line;
    private View include;
    private Activity context;

    private boolean isShowAD = false;
    LinearLayout adLayout;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ADControl adControl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about);
        context = TVAboutActivity.this;

        // isShowAD = ADControl.IsShowAD(AboutActivity.this);
        // initImageLoader();
        // imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        initView();
        setOnclick();
        adControl=new ADControl();
    }

    private void initView() {
        adLayout = (LinearLayout) findViewById(R.id.AdLinearLayout);
        include = (View) findViewById(R.id.about_include);
        la_cai = (LinearLayout) include.findViewById(R.id.la_cai);
        la_ding = (LinearLayout) include.findViewById(R.id.la_ding);
        la_xiongdi = (LinearLayout) include.findViewById(R.id.la_xiongdi);
        la_update = (LinearLayout) include.findViewById(R.id.la_update);
        la_guanzhu = (LinearLayout) include.findViewById(R.id.la_guanzhu);
        la_fenxiang = (LinearLayout) include.findViewById(R.id.la_fenxiang);
        la_tuijian = (LinearLayout) include.findViewById(R.id.la_tuijian);
        la_re = (LinearLayout) include.findViewById(R.id.la_re);
        v_line = (View) include.findViewById(R.id.v_line);
        title_layout_back = (LinearLayout) findViewById(R.id.title_layout_back);
        la_jingpin = (LinearLayout) findViewById(R.id.la_jingpin);
        la_xiongdi.setVisibility(View.GONE);
        if (!AppConfig.isShowWXGZH()) {
            la_guanzhu.setVisibility(View.GONE);
            findViewById(R.id.view_guanzhu).setVisibility(View.GONE);
        }
        if (!AppConfig.isFengxiang()) {
            la_fenxiang.setVisibility(View.GONE);
            findViewById(R.id.view_fenxiang).setVisibility(View.GONE);
        }
        if (!AppConfig.isShowSelfAD()) {
            la_tuijian.setVisibility(View.GONE);
            findViewById(R.id.view_tuijian).setVisibility(View.GONE);
        }


        v_line.setVisibility(View.GONE);
        la_update.setBackgroundResource(R.drawable.la_item_radius_selector);


    }

    // @SuppressWarnings("deprecation")
    // private void initImageLoader() {
    // File cacheFile = StorageUtils.getOwnCacheDirectory(context,
    // DEFAULT_DISK_CACHE.replace(SDCARD, ""));
    // ImageLoaderConfiguration config = new
    // ImageLoaderConfiguration.Builder(context)//
    // .diskCache(new UnlimitedDiscCache(cacheFile))//
    // .imageDownloader(new BaseImageDownloader(context, 10 * 1000, 10 *
    // 1000))//
    // .discCacheFileNameGenerator(new Md5FileNameGenerator())//
    // 将保存的时候的URI名称用MD5
    // // 加密
    // .tasksProcessingOrder(QueueProcessingType.LIFO) //
    // .discCacheSize(50 * 1024 * 1024) // 大小
    // .build();
    //
    // imageLoader.init(config);
    // options = new DisplayImageOptions.Builder()//
    // .showImageOnLoading(R.drawable.ic_stub)//
    // .showImageForEmptyUri(R.drawable.movie)//
    // .showImageOnFail(R.drawable.movie)//
    // .cacheInMemory(true)//
    // .cacheOnDisk(true)//
    // .considerExifParams(true)//
    // .displayer(new RoundedBitmapDisplayer(0))//
    // .resetViewBeforeLoading(false)//
    // .bitmapConfig(Bitmap.Config.RGB_565)//
    // .build();
    //
    // }

    private void setOnclick() {

        la_guanzhu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (AppConfig.isShowWXGZH() && AppConfig.wxgzhBeans != null && AppConfig.wxgzhBeans.size() > 0) {
                    int gzhIndex = new Random().nextInt(AppConfig.wxgzhBeans.size());
                    Intent intent = new Intent(context, GZHAddActivity.class);
                    intent.putExtra("wxgzhbean", AppConfig.wxgzhBeans.get(gzhIndex));
                    startActivity(intent);
                }
            }
        });
        la_fenxiang.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                shareApplication(context);
            }
        });
        la_tuijian.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=   new Intent(context, TVTuijianActivity.class);
                context.startActivity(intent);
            }
        });

        la_ding.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                com.duoduoapp.brothers.utils.Tools.startMarket(context, getPackageName());
            }
        });

        la_cai.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppConfig.publicConfigBean == null || "".equals(AppConfig.publicConfigBean.Information)) {
                    new AlertDialog.Builder(context).setTitle("提示")
                            .setMessage("请添加QQ群:286239217进行反馈")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                } else {

                    adControl.joinQQGroup(context);

                }
            }
        });
        title_layout_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TVAboutActivity.this.finish();
            }
        });
        la_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Log.e("test", "---- update ----");
                if (!adControl.update(context)){
                    Toast.makeText(getApplicationContext(), "已经是最新版本", Toast.LENGTH_SHORT).show();
                }

            }
        });

        la_re.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                executorService.execute(new Runnable() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(context, "亲,开始清除缓存!", Toast.LENGTH_SHORT).show();
                            }

                        });

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(context, "亲,清除缓存成功!", Toast.LENGTH_SHORT).show();
                            }

                        });

                    }
                });
            }
        });
    }
    /**
     * 分享应用
     */
    private void shareApplication(Activity context) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        if (AppConfig.isFengxiang()) {
            if (AppConfig.publicConfigBean != null) {
                if (AppConfig.publicConfigBean.fenxiangInfo != null) {
                    if(AppConfig.wxgzhBeans!=null&&AppConfig.wxgzhBeans.size()>0) {//可以分享朋友圈
                        if(!initSharePengYouQuanIntent(AppConfig.wxgzhBeans.get(0))){//如果分享朋友圈失败
                            if(!initSharePengYouIntent(AppConfig.wxgzhBeans.get(0))){//如果分享朋友也失败
                                intent.putExtra(Intent.EXTRA_TEXT, AppConfig.publicConfigBean.fenxiangInfo);
                            }else {//分享朋友成功
                                return;
                            }
                        }else {//分享朋友圈成功
                            return;
                        }
                    }else{//没有微信公众号的图片，则只能发送给朋友

                        if(!initSharePengYouIntent(new WXGZHBean())){
                            intent.putExtra(Intent.EXTRA_TEXT, AppConfig.publicConfigBean.fenxiangInfo);
                        }
                        else {
                            return;
                        }

                    }

                } else {
                    intent.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款非常强大的杀毒软件:\n名称：" + context.getString(R.string.app_name) + "\n包名:" + context.getPackageName());
                }
            } else {
                intent.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款非常强大的杀毒软件:\n名称：" + context.getString(R.string.app_name) + "\n包名:" + context.getPackageName());
            }
        } else {
            intent.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款非常强大的杀毒软件:\n名称：" + context.getString(R.string.app_name) + "\n包名:" + context.getPackageName());
        }
        startActivity(intent);
    }

    private boolean initSharePengYouIntent(WXGZHBean bean) {
        String type = "com.tencent.mm";
        boolean found = false;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type)) {
                    ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
                    share.setComponent(comp);
                    share.setAction(Intent.ACTION_SEND);
                    share.putExtra(Intent.EXTRA_SUBJECT, "分享");

                    String introduction = "";
                    if (AppConfig.publicConfigBean != null && AppConfig.publicConfigBean.fenxiangInfo != null && !"".equals(AppConfig.publicConfigBean.fenxiangInfo)) {
                        introduction = AppConfig.publicConfigBean.fenxiangInfo;
                    } else {
                        introduction += bean.introduction + "\n";
                        introduction += String.format("您可以点击以下网站，在随后界面中点击右上方蓝色文字\"%s\"进行关注，关注之后即可免费为您提供以上服务。\n", bean.displayName);
                        introduction += "网址:" + bean.url;
                    }
                    share.putExtra(Intent.EXTRA_TEXT, introduction);
//                   share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(AppConfig.GZHPath+bean.id+".jpg"))); // Optional, just if you wanna share an image.
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found) {

                return false;
            }
            startActivity(Intent.createChooser(share, "选择"));
            return true;
        }
        return false;
    }

    private Boolean initSharePengYouQuanIntent(WXGZHBean bean) {
        String type = "com.tencent.mm";
        boolean found = false;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type)) {
                    ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                    share.setComponent(comp);
                    share.putExtra(Intent.EXTRA_SUBJECT, "分享");
                    share.setAction(Intent.ACTION_SEND);
                    String introduction = "";
                    if (AppConfig.publicConfigBean != null && AppConfig.publicConfigBean.fenxiangInfo != null && !"".equals(AppConfig.publicConfigBean.fenxiangInfo)) {
                        introduction = AppConfig.publicConfigBean.fenxiangInfo;
                    } else {
                        introduction += bean.introduction + "\n";
                        introduction += String.format("您可以点击以下网站，在随后界面中点击右上方蓝色文字\"%s\"进行关注，也可点击二维码，在随后的界面中长按二维码进行关注，关注之后即可免费为您提供以上服务。\n", bean.displayName);
                        introduction += "网站:" + bean.url;
                    }
                    share.putExtra("Kdescription", introduction);
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(AppConfig.GZHPath + bean.id + ".jpg"))); // Optional, just if you wanna share an image.
                    //   share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("/storage/emulated/0/tencent/MicroMsg/WeiXin/mmexport1490770449993.jpg"))); // Optional, just if you wanna share an image.
                    share.setPackage(info.activityInfo.packageName);

                    found = true;
                    break;
                }
            }
            if (!found) {

                return false;
            }
            startActivity(Intent.createChooser(share, "选择"));
            return true;
        }
        return false;
    }

    // private long exitTime = 0;
    long time = 0;
    private long exitTime = 0;

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        showHengfu();
        if (System.currentTimeMillis() - time > 120 * 1000) {
            time = System.currentTimeMillis();
            adControl.homeGet5Score(context);
        }
        AddBaiduAd();
    }

    private void AddBaiduAd() {
        LinearLayout baiduad = (LinearLayout) findViewById(R.id.baiduad);
        adControl.addAd(baiduad, this);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }

    private void showHengfu() {
        // LinearLayout AdLinearLayout = (LinearLayout)
        // findViewById(R.id.AdLinearLayout);
        // ADControl.C360_AD_Banner(AdLinearLayout, AboutActivity.this);

    }
}
