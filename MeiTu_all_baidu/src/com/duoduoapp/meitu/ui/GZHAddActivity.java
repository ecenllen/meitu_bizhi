package com.duoduoapp.meitu.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.duoduoapp.meitu.itf.IData;
import com.duoduoapp.meitu.utils.ADControl;
import com.duoduoapp.meitu.utils.AppConfig;
import com.duoduoapp.meitu.utils.WXGZHBean;
import com.hlxwdsj.bj.vz.R;
import com.viewpagerindicator.TabPageIndicator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索界面
 *
 * @author duoduoapp
 */
public class GZHAddActivity extends FragmentActivity{

    // private String query = "";
    // private String type = "";
    // /private BaseAdapter adapter;
    private Activity context;
    public View view;
    private LinearLayout la_back, la_search;
    private EditText et_search;

    private ViewPager pager;
    private TabPageIndicator indicator;
    private FragmentManager fManager;
    private FragmentPagerAdapter adapter;
private WXGZHBean wxgzhbean;
    private LinearLayout adLayout;
//    private int gzhIndex;
    private int currentPage = 0;
    String[] gzhTypes;
    private ADControl adControl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gzhaddactivity);
        context = GZHAddActivity.this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.wxgzhbean = (WXGZHBean)bundle.getSerializable("wxgzhbean");
            gzhTypes = wxgzhbean.type.split(",");
            for (int i = 0; i < gzhTypes.length; i++) {

                String content = "方法" + (i+1);
                if (i == 0) {
                    content += "(推荐)";

                }

                CONTENT.add(content);

            }
        }
        initView();
        initClick();
        adControl=new ADControl();
    }

    private void initView() {
        adLayout = (LinearLayout) findViewById(R.id.AdLinearLayout);

        la_back = (LinearLayout) findViewById(R.id.la_back);
        la_search = (LinearLayout) findViewById(R.id.la_search);
        et_search = (EditText) findViewById(R.id.et_search);
//		la_search.setVisibility(View.GONE);
        et_search.setVisibility(View.INVISIBLE);
        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (TabPageIndicator) findViewById(R.id.indicator);
        fManager = getSupportFragmentManager();
        adapter = new GoogleMusicAdapter(fManager);
        pager.setAdapter(adapter);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        indicator.setViewPager(pager);
        indicator.setCurrentItem(0);

    }

     Handler sHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            indicator.setCurrentItem(1);
//										Logger.debug(this.getClass().getName() + " no jingpin!");
        }

    };

    private void initClick() {
        la_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                GZHAddActivity.this.finish();
            }
        });
        la_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (gzhTypes[currentPage].equals("pengyouquan")) {
                    //shareWeChat("/storage/emulated/0/tencent/MicroMsg/WeiXin/mmexport1490770449993.jpg");
                    String introduction = "";
                    introduction += wxgzhbean.introduction + "\n";
                    introduction += String.format("您可以点击以下网站，在随后界面中点击右上方蓝色文字\"%s\"进行关注，也可点击二维码，在随后的界面中长按二维码进行关注，关注之后即可免费为您提供以上服务。\n",wxgzhbean.displayName);
                    introduction += "网站:" + wxgzhbean.url;
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(introduction);
                    initSharePengYouQuanIntent(wxgzhbean);
                } else if (gzhTypes[currentPage].equals("pengyou")) {
                    initSharePengYouIntent(wxgzhbean);
                }else if (gzhTypes[currentPage].equals("putong")) {
                    String introduction = "";
                    introduction += wxgzhbean.introduction + "\n";
                    introduction += String.format("您可以点击以下网站，在随后界面中点击右上方蓝色文字\"%s\"进行关注，关注之后即可免费为您提供以上服务。\n",wxgzhbean.displayName);
                    introduction += "网站:" + wxgzhbean.url;
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(introduction);
                    Toast.makeText(context,"已经为您复制到粘贴板了",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 标题
     */
    private List<String> CONTENT = new ArrayList<String>();


    /**
     * 滑动
     *
     * @author Administrator
     */
    class GoogleMusicAdapter extends FragmentPagerAdapter {

        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return getFragment(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT.get(position);
        }

        @Override
        public int getCount() {
            return CONTENT.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    private Fragment getFragment(int position) {
        Fragment re = null;
//		if (iMemoryCache.containsKey(position) && iMemoryCache.get(position) != null) {
//			return iMemoryCache.get(position);
//		}
        if (gzhTypes[position].equals("pengyouquan"))
            re = new GZHAddPengYouQuanFrag();
        else if (gzhTypes[position].equals("pengyou")) {
            re = new GZHAddPengYouFrag();
        }else if (gzhTypes[position].equals("putong")) {
            re = new GZHAddPuTongFrag(wxgzhbean);
        }
//		iMemoryCache.put(position, re);
        return re;
    }

    // ///////////////////点击空白影藏软键盘/////////////////////////////
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // ///////////////////结束点击空白影藏软键盘/////////////////////////////
    long time = 0;

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // QihooAdAgent.enableFloat(this);
        adControl.addAd(adLayout, context);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    private void initSharePengYouQuanIntent(WXGZHBean bean) {
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
                    introduction += bean.introduction + "\n";
                    introduction += String.format("您可以点击以下网站，在随后界面中点击右上方蓝色文字\"%s\"进行关注，也可点击二维码，在随后的界面中长按二维码进行关注，关注之后即可免费为您提供以上服务。\n",bean.displayName);
                    introduction += "网站:" + bean.url;

                    share.putExtra("Kdescription", introduction);
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(AppConfig.GZHPath + bean.id + ".jpg"))); // Optional, just if you wanna share an image.
                    //   share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("/storage/emulated/0/tencent/MicroMsg/WeiXin/mmexport1490770449993.jpg"))); // Optional, just if you wanna share an image.
                    share.setPackage(info.activityInfo.packageName);

                    found = true;
                    break;
                }
            }
            if (!found) {
                Toast.makeText(context,"您没有安装微信或者该方法暂时不支持哦!",Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(Intent.createChooser(share, "选择"));
        }
    }

    private void shareWeChat(String path) {
        Uri uriToImage = Uri.fromFile(new File(path));
        Intent shareIntent = new Intent();
        //发送图片到朋友圈
        //ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        //发送图片给好友。
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        shareIntent.setComponent(comp);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, "分享图片"));
    }

    private void initSharePengYouIntent(WXGZHBean bean) {
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
                    introduction += bean.introduction + "\n";
                    introduction += String.format("您可以点击以下网站，在随后界面中点击右上方蓝色文字\"%s\"进行关注，关注之后即可免费为您提供以上服务。\n",bean.displayName);
                    introduction += "网址:" + bean.url;
                    share.putExtra(Intent.EXTRA_TEXT, introduction);
//                   share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(AppConfig.GZHPath+bean.id+".jpg"))); // Optional, just if you wanna share an image.
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found) {
                Toast.makeText(context,"您没有安装微信或者该方法暂时不支持哦!",Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(Intent.createChooser(share, "选择"));
        }
    }
}
