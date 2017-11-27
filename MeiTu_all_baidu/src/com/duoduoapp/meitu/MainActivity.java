package com.duoduoapp.meitu;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.duoduoapp.meitu.adapter.MainAdapter;
import com.duoduoapp.meitu.bean.MainListBean;
import com.duoduoapp.meitu.ui.CpuWebActivity;
import com.duoduoapp.meitu.ui.GZHAddActivity;
import com.duoduoapp.meitu.ui.LoadType;
import com.duoduoapp.meitu.ui.UIShowClick;
import com.duoduoapp.meitu.utils.AppConfig;
import com.duoduoapp.meitu.utils.BaiduUrlUtils;
import com.duoduoapp.meitu.utils.RecyclerViewOnItemClickListener;
import com.duoduoapp.meitu.utils.WXGZHBean;
import com.duoduoapp.meitu.yshow.KKTuiJianActivity;
import com.hlxwdsj.bj.vz.R;
import com.duoduoapp.meitu.adapter.SlidingLeftAdapter;
import com.duoduoapp.meitu.itf.IData;
import com.duoduoapp.meitu.itf.SlidingMenuListener;
import com.duoduoapp.meitu.ui.UISearch;
import com.duoduoapp.meitu.utils.ADControl;
import com.duoduoapp.meitu.utils.DataUtil;
import com.duoduoapp.meitu.utils.Logger;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;


import org.json.JSONException;

public class MainActivity extends BaseActivity implements SlidingMenuListener,SlidingLeftAdapter.SlidingListener,OnRefreshListener,OnLoadMoreListener,RecyclerViewOnItemClickListener {

    private  SlidingMenu slidingMenu;
    private Context context;
    private SlidingLeftAdapter slidingAdapter;
    private final List<String> beans = new ArrayList<String>();
    private ListView listView;
    private final List<Boolean> selectors = new ArrayList<Boolean>();
    private LinearLayout la_menu, la_more, la_search, la_guanyuwomen, la_manager, la_redian, la_tuijian, la_meinv, la_guanzhu, la_fenxiang;
    private TextView tv_title;
    static int index = 0;
    private LinearLayout AdLinearLayout;
    private MainAdapter adapter;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<MainListBean> adapterShowBeans = new ArrayList<>();
    private List<MainListBean> showBeans = new ArrayList<>();
    private int pn=0;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView recyclerView;
    private Handler uiHandler;
    private ADControl adControl;
    private LoadType loadType=LoadType.REFRESH;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.sliding_menu_left);
        context = MainActivity.this;
        uiHandler=new MainHandler(this);
        initData();
        initView();
        initPop();

        initClick();
        adControl=new ADControl();
        adControl.update(this);
        adControl.addAd(AdLinearLayout,this);
        requestData();
    }

    private void requestData() {
        showLoadDialog();
        loadData();
    }

    private void loadData() {
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    showBeans.clear();
                    showBeans=DataUtil.getBaiduBizhi(BaiduUrlUtils.getBaiduUrl(pn,tv_title.getText().toString()));
                    uiHandler.sendEmptyMessage(2000);
                } catch (IOException e) {
                    e.printStackTrace();
                    uiHandler.sendEmptyMessage(3000);
                    Logger.error(e);
                } catch (JSONException e) {
                    Logger.error(e);
                    uiHandler.sendEmptyMessage(3000);
                    e.printStackTrace();

                }
            }
        });
    }

    private void initData() {
        DataUtil.initAll();
        beans.clear();
        selectors.clear();
        for (int i = 0; i < IData.T1_LIST.size(); i++) {
            if (!AppConfig.isSearch()) {
                if (IData.T1_LIST.get(i).equals("美女") || IData.T1_LIST.get(i).equals("热门") || IData.T1_LIST.get(i).equals("游戏") || IData.T1_LIST.get(i).equals("酷车")
                        || IData.T1_LIST.get(i).equals("明星")) {
                    continue;
                }
            }
            beans.add(IData.T1_LIST.get(i));
            if (i == 0) {
                selectors.add(true);
            } else {
                selectors.add(false);
            }
        }

    }

    /**
     * 初始化视图
     */
    private void initView() {
        la_menu = (LinearLayout) findViewById(R.id.la_menu);
        la_more = (LinearLayout) findViewById(R.id.la_more);
        la_search = (LinearLayout) findViewById(R.id.la_search);
        if (!AppConfig.isSearch()) {
            la_search.setVisibility(View.GONE);
        } else {
            la_search.setVisibility(View.VISIBLE);
        }
        AdLinearLayout=(LinearLayout)findViewById(R.id.AdLinearLayout);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(beans.get(0));
        slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT); // 设置模式.左边
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setShadowWidthRes(R.dimen.sliding_wight); // 宽度
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_last_wight); // 剩余宽度
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); // 设置全屏可以滑动
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setBehindScrollScale(0); // 设置后面不动
        slidingMenu.toggle();
        listView = (ListView) slidingMenu.findViewById(R.id.lv_sliding);
        slidingAdapter = new SlidingLeftAdapter(context, beans, selectors);
        slidingAdapter.setOnSlidingListener(this);
        listView.setAdapter(slidingAdapter);
        swipeToLoadLayout= (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        recyclerView= (RecyclerView) findViewById(R.id.swipe_target_my);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 10);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top=15;

            }
        });
        adapter=new MainAdapter(this,adapterShowBeans);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE ){
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)){
                        swipeToLoadLayout.setLoadingMore(true);
                    }
                }
            }
        });
    }
    private void initClick() {
        la_menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showSliding();
            }
        });
        la_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showPop();
            }
        });
        la_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), UISearch.class);
                startActivity(intent);
            }
        });
        slidingMenu.setOnClosedListener(new OnClosedListener() {

            @Override
            public void onClosed() {
                setSlidengEnabled();
            }
        });

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_UP) { // 菜单键替换成,显示侧边栏
            if (slidingMenu.isMenuShowing()) {
                hideSliding();
                return true;
            } else {
                if (pop != null) {
                    if (isShowPop) {
                        hidePop();
                    }
                }
                showSliding();
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (!slidingMenu.isMenuShowing()) {
                showSliding();
                if (pop != null) {
                    if (isShowPop) {
                        hidePop();
                    }
                }
                return true;
            } else {

                adControl.ShowTPAD(context);

            }

            return true;
        }
        return true;

    }

    @Override
    public void setSliding(boolean sliding) {
        if (slidingMenu != null) {
            slidingMenu.setSlidingEnabled(sliding);

        }
    }

    @Override
    public void showSliding() {
        this.showMenu();
        setSliding(true);
    }

    @Override
    public void hideSliding() {
        this.showContent();
        setSlidengEnabled();
    }

    public  void hide() {
        if (slidingMenu != null) {
            slidingMenu.toggle();
        }
    }

    private  void setSlidengEnabled() {
        if (index == 0) {
            slidingMenu.setSlidingEnabled(true);
        } else {
            slidingMenu.setSlidingEnabled(false);
        }

    }

    private PopupWindow pop;
    private View view;

    @SuppressWarnings("deprecation")
    private void initPop() {
        if (pop == null || view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.pop_more, null);
            pop = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
            pop.setOutsideTouchable(true);
            pop.setBackgroundDrawable(new BitmapDrawable()); // 不设置,点击外面不消失.

            la_manager = (LinearLayout) view.findViewById(R.id.la_manager);
            la_tuijian = (LinearLayout) view.findViewById(R.id.la_tuijian);
            la_redian = (LinearLayout) view.findViewById(R.id.la_redian);
            la_meinv = (LinearLayout) view.findViewById(R.id.la_meinv);
            la_guanzhu = (LinearLayout) view.findViewById(R.id.la_guanzhu);
            la_fenxiang = (LinearLayout) view.findViewById(R.id.la_fenxiang);
            if (!AppConfig.isShowSelfAD()) {
                la_tuijian.setVisibility(View.GONE);
            }
            if (!AppConfig.isShowCpuWeb()) {
                la_redian.setVisibility(View.GONE);
            }
            if (!AppConfig.isShowMeinv()) {
                la_meinv.setVisibility(View.GONE);
            }
            if (!AppConfig.isShowWXGZH()) {
                la_guanzhu.setVisibility(View.GONE);
            }
            if (!AppConfig.isFengxiang()) {
                la_fenxiang.setVisibility(View.GONE);
            }
            la_guanyuwomen = (LinearLayout) view.findViewById(R.id.la_guanyuwomen);
            la_manager.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    hidePop();
                    Intent intent = new Intent();
                    intent.setClass(context, ManagerActivity.class);
                    context.startActivity(intent);
                }
            });
            la_tuijian.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TVTuijianActivity.class);
                    context.startActivity(intent);
                }
            });
            la_redian.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CpuWebActivity.class);
                    context.startActivity(intent);
                }
            });
            la_meinv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, KKTuiJianActivity.class).putExtra("ordertype", 0);
                    context.startActivity(intent);

                }
            });
            la_guanzhu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
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
                public void onClick(View view) {
                    shareApplication((Activity) context);
                }
            });


            la_guanyuwomen.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    hidePop();
                    Intent intent = new Intent();
                    intent.setClass(context, TVAboutActivity.class);
                    context.startActivity(intent);
                }
            });
        }
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
                    if (AppConfig.wxgzhBeans != null && AppConfig.wxgzhBeans.size() > 0) {//可以分享朋友圈
                        if (!initSharePengYouQuanIntent(AppConfig.wxgzhBeans.get(0))) {//如果分享朋友圈失败
                            if (!initSharePengYouIntent(AppConfig.wxgzhBeans.get(0))) {//如果分享朋友也失败
                                intent.putExtra(Intent.EXTRA_TEXT, AppConfig.publicConfigBean.fenxiangInfo);
                            } else {//分享朋友成功
                                return;
                            }
                        } else {//分享朋友圈成功
                            return;
                        }
                    } else {//没有微信公众号的图片，则只能发送给朋友

                        if (!initSharePengYouIntent(new WXGZHBean())) {
                            intent.putExtra(Intent.EXTRA_TEXT, AppConfig.publicConfigBean.fenxiangInfo);
                        } else {
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

    private boolean isShowPop = false;

    private void showPop() {
        if (!isShowPop) {
            Logger.debug("!pop.isShowing()");
            pop.showAsDropDown(la_more);
            isShowPop = true;
        } else {
            hidePop();
        }
    }

    private void hidePop() {
        isShowPop = false;
        if (pop.isShowing()) {
            pop.dismiss();
        }
    }

    long time = 0;

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (System.currentTimeMillis() - time > 120 * 1000) {
            time = System.currentTimeMillis();

            adControl.homeGet5Score(MainActivity.this);
        }
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    private ProgressDialog progressDialog;

    private void showLoadDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载数据中,请稍后...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

    }

    private void hideLoadDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
            progressDialog = null;
        }
    }
    private void onLoad() {
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
    }


    @Override
    public void onSliding(String text) {
        if (tv_title == null) {
            return;
        }else {
            tv_title.setText(text);
        }
        hide();
        setSlidengEnabled();
        pn=0;
        loadType=LoadType.REFRESH;
        requestData();
    }

    @Override
    public void onRefresh() {
        pn=0;
        loadType=LoadType.REFRESH;
        requestData();
    }

    @Override
    public void onLoadMore() {
        pn+=20;
        loadType=LoadType.LOAD;
        requestData();
    }

    @Override
    public void onItemClick(View view, int position) {
        if ("ad".equals(adapterShowBeans.get(position).getAd_platform())) {
            AppConfig.openAD(context, adapterShowBeans.get(position), "yuansheng_count");
        } else {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClass(context, UIShowClick.class);
            intent.putExtra(IData.EXTRA_TYPE, IData.EXTRA_SHOW);
            intent.putExtra(IData.EXTRA_DATA, adapterShowBeans.get(position));
            startActivity(intent);
        }
    }

    private  static class MainHandler extends Handler{
        WeakReference<MainActivity> weakReference;

        public MainHandler(MainActivity mainActivity) {
            weakReference =new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity activity=weakReference.get();
            switch (msg.what) {

                case 2000: // 加载成功
                    activity.hideLoadDialog();
                    if (activity.adapter == null) {
                        return;
                    }
                    if (activity.loadType == LoadType.REFRESH) {
                        activity.adapterShowBeans.clear();
//                        adapterShowBeans.addAll(showBeans);
                        activity.adapterShowBeans.addAll(AppConfig.getAdMIXShowListBeans(activity.getApplicationContext(), activity.showBeans));
                        activity.recyclerView.smoothScrollToPosition(0);
                        activity.adapter.notifyDataSetChanged();
                    } else if (activity.loadType == LoadType.LOAD) {
                        if(activity.showBeans!=null&&activity.showBeans.size()>0){
//                            adapterShowBeans.clear();
                            activity.adapterShowBeans.addAll(AppConfig.getAdMIXShowListBeans(activity.getApplicationContext(), activity.showBeans));
                            activity.recyclerView.smoothScrollToPosition(activity.adapterShowBeans.size()-activity.showBeans.size());
                            activity.adapter.notifyDataSetChanged();
                        }else {
                            activity.pn-=20;
                            Toast.makeText(activity.getApplicationContext(),"亲，已经是最后一页了",Toast.LENGTH_SHORT).show();
                        }
                    }
                    activity.onLoad();
                    break;
                case 3000: // 加载失败
                    activity.hideLoadDialog();
                    if(activity.loadType==LoadType.LOAD){
                        activity.pn-=20;
                    }
                    activity.onLoad();
                    Toast.makeText(activity.getApplicationContext(), "亲,数据加载失败了!", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    }
}
