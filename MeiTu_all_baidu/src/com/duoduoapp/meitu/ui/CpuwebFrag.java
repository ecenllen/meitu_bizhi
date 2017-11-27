package com.duoduoapp.meitu.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.duoduoapp.meitu.itf.IData;
import com.duoduoapp.meitu.utils.AppConfig;
import com.hlxwdsj.bj.vz.R;


/**
 * 搜索精品
 *
 * @author Administrator
 */
public class CpuwebFrag extends Fragment{

    private String id;

    public CpuwebFrag() {
    }

    @SuppressLint("ValidFragment")
    public CpuwebFrag(String id) {
        this.id = id;
    }

    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.context = getActivity();

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }

        super.onDestroy();

    }

    private WebView mWebView = null;

    public Boolean canGoBack() {
        if (mWebView != null && mWebView.canGoBack()) {
            return true;
        }
        return false;
    }

    public void goBack() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }

    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.cpuwebfrag, container, false);
            view.findViewById(R.id.btn_fenxiang).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mWebView != null) {
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.setType("text/plain");
                        if (AppConfig.publicConfigBean != null && AppConfig.publicConfigBean.fenxiangInfo != null && AppConfig.isFengxiang()) {
                            intent.putExtra(Intent.EXTRA_TEXT, AppConfig.publicConfigBean.fenxiangInfo + "\r\n" + "在这里我们发现一款好棒的节目:【" + mWebView.getUrl() + "】推荐给您");
                        } else {
                            intent.putExtra(Intent.EXTRA_TEXT, "推荐给您一个有趣的网页:" + mWebView.getUrl());
                        }

                        startActivity(intent);
                    }
                }
            });
            mWebView = (WebView) view.findViewById(R.id.cpuWebview);
            mWebView.setVerticalScrollBarEnabled(false);
            mWebView.setHorizontalScrollBarEnabled(false);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            // 如果是图片频道，则必须设置该接口为true，否则页面无法展现
            webSettings.setDomStorageEnabled(true);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url); //在本webview里面加载
//                    context.startActivity(new Intent(context, WebActivity.class).putExtra("url", url));
                    return true;
                }
            });
            if (AppConfig.configBean != null) {//只有当配置获取到了才能展示
                if (AppConfig.configBean.cpuidorurl != null && (AppConfig.configBean.cpuidorurl.startsWith("http://") || AppConfig.configBean.cpuidorurl.startsWith("https://"))) {//要求使用链接
                    if (AppConfig.configBean.cpuidorurl.contains("%s")){
                        mWebView.loadUrl(String.format(AppConfig.configBean.cpuidorurl, id));
                    } else{
                        mWebView.loadUrl(AppConfig.configBean.cpuidorurl);
                    }
                } else {
                    if (AppConfig.configBean.cpuidorurl != null && !AppConfig.configBean.cpuidorurl.equals("")) {
                        mWebView.loadUrl(String.format("http://cpu.baidu.com/%s/%s", id, AppConfig.configBean.cpuidorurl));
                    }else {
                        mWebView.loadUrl(String.format("http://cpu.baidu.com/%s/efd26f23", id));
                    }
                }
            }

        } else {

            ViewGroup p = (ViewGroup) view.getParent();
            if (p != null) {
                p.removeView(view);
            }
        }

        return view;
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // ADControl.addAd((LinearLayout)
        // getActivity().findViewById(R.id.AdLinearLayout), getActivity());
    }

}
