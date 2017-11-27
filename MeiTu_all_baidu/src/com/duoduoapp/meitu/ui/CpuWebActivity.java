package com.duoduoapp.meitu.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.duoduoapp.brothers.BaseActivity;

import com.duoduoapp.meitu.cache.IMemoryCache;
import com.duoduoapp.meitu.cache.VideoMemoryCache;
import com.duoduoapp.meitu.itf.IData;
import com.duoduoapp.meitu.utils.ADControl;
import com.duoduoapp.meitu.utils.WXGZHBean;
import com.hlxwdsj.bj.vz.R;
import com.viewpagerindicator.TabPageIndicator;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索界面
 *
 * @author duoduoapp
 */
public class CpuWebActivity extends BaseActivity{

    private Activity context;

    private LinearLayout la_back, la_search,lyt_top;
    private EditText et_search;

    private ViewPager pager;
    private TabPageIndicator indicator;
    private FragmentManager fManager;
    private FragmentPagerAdapter adapter;
    private WXGZHBean wxgzhbean;
    private IMemoryCache<Fragment> iMemoryCache = new VideoMemoryCache<Fragment>();
    private LinearLayout adLayout;
    //    private int gzhIndex;
    private int currentPage = 0;
    String[] gzhTypes;
    private LinearLayout back;
    private ADControl adControl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cpuwebactivity);
        context = CpuWebActivity.this;
        CONTENT.add(new SpinnerItem("娱乐", "1001"));
        CONTENT.add(new SpinnerItem("体育", "1002"));
        CONTENT.add(new SpinnerItem("图片", "1003"));
        CONTENT.add(new SpinnerItem("手机", "1005"));
        CONTENT.add(new SpinnerItem("财经", "1006"));
        CONTENT.add(new SpinnerItem("汽车", "1007"));
        CONTENT.add(new SpinnerItem("房产", "1008"));
        CONTENT.add(new SpinnerItem("时尚", "1009"));
        CONTENT.add(new SpinnerItem("军事", "1012"));
        CONTENT.add(new SpinnerItem("科技", "1013"));
        CONTENT.add(new SpinnerItem("热点", "1021"));
        CONTENT.add(new SpinnerItem("推荐", "1022"));
        CONTENT.add(new SpinnerItem("美女", "1024"));
        CONTENT.add(new SpinnerItem("搞笑", "1025"));
        CONTENT.add(new SpinnerItem("聚合", "1032"));
        CONTENT.add(new SpinnerItem("视频", "1033"));
        CONTENT.add(new SpinnerItem("女人", "1034"));
        CONTENT.add(new SpinnerItem("生活", "1035"));
        CONTENT.add(new SpinnerItem("文化", "1036"));
        CONTENT.add(new SpinnerItem("游戏", "1040"));
        CONTENT.add(new SpinnerItem("母婴", "1042"));
        CONTENT.add(new SpinnerItem("看点", "1047"));
        CONTENT.add(new SpinnerItem("动漫", "1055"));

       // CONTENT.add(new SpinnerItem("推荐-视频频道", "1057"));
        CONTENT.add(new SpinnerItem("音乐-视频频道", "1058"));
        CONTENT.add(new SpinnerItem("搞笑-视频频道", "1059"));
        CONTENT.add(new SpinnerItem("影视-视频频道", "1060"));
        CONTENT.add(new SpinnerItem("娱乐-视频频道", "1061"));
        CONTENT.add(new SpinnerItem("小品-视频频道", "1062"));
//        CONTENT.add(new SpinnerItem("社会-视频频道", "1063"));
//        CONTENT.add(new SpinnerItem("观天下-视频频道", "1064"));
        CONTENT.add(new SpinnerItem("萌萌哒-视频频道", "1065"));
        CONTENT.add(new SpinnerItem("生活-视频频道", "1066"));
        CONTENT.add(new SpinnerItem("游戏-视频频道", "1067"));

//        CONTENT.add(new SpinnerItem("推荐-图片频道", "1068"));
//        CONTENT.add(new SpinnerItem("娱乐-图片频道", "1071"));
//        CONTENT.add(new SpinnerItem("体育-图片频道", "1072"));
//        CONTENT.add(new SpinnerItem("旅游-图片频道", "1073"));
//        CONTENT.add(new SpinnerItem("美食-图片频道", "1074"));
//        CONTENT.add(new SpinnerItem("时尚-图片频道", "1075"));
//        CONTENT.add(new SpinnerItem("汽车-图片频道", "1076"));
//        CONTENT.add(new SpinnerItem("游戏-图片频道", "1077"));
        CONTENT.add(new SpinnerItem("本地化", "1080"));
        initView();
        initClick();
        adControl=new ADControl();
    }

    private void initView() {
        adLayout = (LinearLayout) findViewById(R.id.AdLinearLayout);
        la_back = (LinearLayout) findViewById(R.id.la_back);
        lyt_top = (LinearLayout) findViewById(R.id.lyt_top);
        lyt_top.setVisibility(View.GONE);
        la_search = (LinearLayout) findViewById(R.id.la_search);
        et_search = (EditText) findViewById(R.id.et_search);
//		la_search.setVisibility(View.GONE);
        et_search.setVisibility(View.INVISIBLE);
        pager = (ViewPager) findViewById(R.id.pager);
        back= (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CpuwebFrag currentFrag = (CpuwebFrag) getFragment(currentPage);
                if (currentFrag != null && currentFrag.canGoBack()) {
                    currentFrag.goBack();
                } else {
                    adControl.ShowTPAD(context);
                }
            }
        });
        pager.setOffscreenPageLimit(1);
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


    /**
     * 标题
     */
    private List<SpinnerItem> CONTENT = new ArrayList<SpinnerItem>();

    private void initClick() {
        la_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                CpuWebActivity.this.finish();
            }
        });


    }

    class SpinnerItem {
        /**
         * 频道名称
         */
        String name;
        /**
         * 频道id
         */
        String id;

        public SpinnerItem(String name, String id) {
            this.name = name;
            this.id = id;
        }

    }

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
            return CONTENT.get(position).name;
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
        if (iMemoryCache.containsKey(position) && iMemoryCache.get(position) != null) {
            System.out.println("页面来自缓存");
            return iMemoryCache.get(position);
        }

        re = new CpuwebFrag(CONTENT.get(position).id);

        iMemoryCache.put(position, re);
        return re;
    }


    long time = 0;

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
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
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        CpuwebFrag currentFrag = (CpuwebFrag) getFragment(currentPage);
        if (currentFrag != null && currentFrag.canGoBack()) {
            currentFrag.goBack();
        } else {
            adControl.ShowTPAD(context);
        }
    }


}
