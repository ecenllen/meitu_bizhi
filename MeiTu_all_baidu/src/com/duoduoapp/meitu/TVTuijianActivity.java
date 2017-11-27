package com.duoduoapp.meitu;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.duoduoapp.meitu.adapter.TuijianAdapter;
import com.duoduoapp.meitu.itf.IData;
import com.duoduoapp.meitu.utils.ADBean;
import com.duoduoapp.meitu.utils.ADControl;
import com.duoduoapp.meitu.utils.AppConfig;
import com.duoduoapp.meitu.utils.PackageUtil;
import com.hlxwdsj.bj.vz.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 美女推荐
 *
 * @author Administrator
 */
public class TVTuijianActivity extends Activity{

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Activity context;
    private List<ADBean> tuijianBeans = new ArrayList<ADBean>();
    private List<ADBean> adapterBeans = new ArrayList<ADBean>();
    private LinearLayout title_layout_back, la_more;
    private ListView listView;
    private TuijianAdapter adapter;
    private PackageBroadcast packageBroadcast;
    private ADControl adControl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tuijian);
        context = TVTuijianActivity.this;
        initBroadcast();
        initView();
        setOnclick();
        initData();

        Map<String, String> map_ekv = new HashMap<String, String>();
        map_ekv.put("show", "精品推荐");
        MobclickAgent.onEvent(context, "wall_count", map_ekv);
        adControl=new ADControl();
    }

    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.setPriority(Integer.MAX_VALUE);
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        packageBroadcast = new PackageBroadcast();
        context.registerReceiver(packageBroadcast, filter);

    }

    private void removeBroadcast() {
        if (packageBroadcast != null) {
            context.unregisterReceiver(packageBroadcast);
            packageBroadcast = null;

        }
    }

    private void initView() {
        title_layout_back = (LinearLayout) findViewById(R.id.title_layout_back);
        la_more = (LinearLayout) findViewById(R.id.la_more);
        la_more.setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new TuijianAdapter(context, adapterBeans);
        listView.setAdapter(adapter);

    }

    private void setOnclick() {

//        la_more.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                showPop();
//            }
//        });
        title_layout_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                context.finish();
            }
        });

    }

    public Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1000: // 更新界面
                    if (adapter != null && tuijianBeans != null) {
                        adapterBeans.clear();
                        adapterBeans.addAll(tuijianBeans);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2000: // 下载apk并安装
                    break;
                default:
                    break;
            }
        }

        ;
    };


    private void initData() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                tuijianBeans.clear();
                List<ADBean> nothaveselfadBeans = new ArrayList<ADBean>();//没有安装的
                List<ADBean> haveselfadBeans = new ArrayList<ADBean>();//已经安装的
                List<ADBean> otherselfadBeans = new ArrayList<ADBean>();//其他广告类型
                if (AppConfig.selfadBeans != null && AppConfig.selfadBeans.size() > 0) {
                    for (int i = 0; i < AppConfig.selfadBeans.size(); i++) {
                        ADBean bean = AppConfig.selfadBeans.get(i);
                        if (bean.getAd_type() == 1) {//下载类型
                            boolean is = PackageUtil.isInstallApp(context, bean.getAd_packagename());
                            bean.setAd_have(is);
                            if (is) {
                                haveselfadBeans.add(bean);
                            }else {
                                nothaveselfadBeans.add(bean);
                            }
                        } else//其他类型，公众号，网页等
                        {
                            otherselfadBeans.add(bean);
                        }
                    }


                }
                if (nothaveselfadBeans.size() > 0){
                    tuijianBeans.addAll(nothaveselfadBeans);
                }
                if (otherselfadBeans.size() > 0){
                    tuijianBeans.addAll(otherselfadBeans);
                }
                if (haveselfadBeans.size() > 0){
                    tuijianBeans.addAll(haveselfadBeans);
                }
                uiHandler.sendEmptyMessage(1000);
            }

        });
    }

    long time1 = 0;

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (System.currentTimeMillis() - time1 > 120 * 1000) {
            time1 = System.currentTimeMillis();
            adControl.homeGet5Score(context);
        }
        LinearLayout adLayout = (LinearLayout) findViewById(R.id.AdLinearLayout);
        // QihooAdAgent.enableFloat(this);
        adControl.addAd(adLayout, context);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        removeBroadcast();
    }

    private PopupWindow pop;
    private View view;
    private LinearLayout la_manager, /* la_about, */
            la_history, la_haoping, la_favorite, la_tuijian;


    @SuppressWarnings("unused")
    private boolean isShowPop = false;
    private long exitTime = 0;

    private void showPop() {

        la_more.setClickable(false);
        pop.showAsDropDown(la_more);
        // pop.showAsDropDown(la_more);
        isShowPop = true;

    }

    private void hidePop() {
        isShowPop = false;
        if (pop.isShowing()) {
            pop.dismiss();
        }
    }

    /**
     * 应用程序安装卸载 广播
     *
     * @author Administrator
     */
    class PackageBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            Log.d("tt", intent.getAction());
            if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
                Log.d("tt", Intent.ACTION_PACKAGE_ADDED);
                initData();
            } else if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
                Log.d("tt", Intent.ACTION_PACKAGE_REMOVED);
                initData();
            }
        }
    }


}
