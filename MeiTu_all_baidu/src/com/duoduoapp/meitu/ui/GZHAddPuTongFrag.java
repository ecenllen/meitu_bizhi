package com.duoduoapp.meitu.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duoduoapp.meitu.itf.IData;
import com.duoduoapp.meitu.utils.WXGZHBean;
import com.hlxwdsj.bj.vz.R;


/**
 * 搜索精品
 *
 * @author Administrator
 */
public class GZHAddPuTongFrag extends Fragment{

    private Activity context;
    private TextView txtFangfa;
    private TextView txtCopy;
    WXGZHBean bean;
    public  GZHAddPuTongFrag(){}
    @SuppressLint("ValidFragment")
    public GZHAddPuTongFrag(WXGZHBean bean) {

        this.bean = bean;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    private View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.gzh_addputongfrag, container, false);

        }
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null) {
            p.removeAllViewsInLayout();
        }
        txtFangfa = (TextView) view.findViewById(R.id.txtFangfa);
        txtCopy = (TextView) view.findViewById(R.id.txtCopy);
        txtFangfa.setText(String.format("点击右上角(关注)按钮复制以下文字发送给任意一个好友或者朋友圈，在您的好友或者朋友圈中点击链接，随后点击右上方\"%s\"进行关注", bean.displayName));
        String introduction = "";
        introduction += bean.introduction + "\n";
        introduction += "您可以点击以下网站，在随后界面中点击右上方蓝色文字进行关注，也可点击二维码，在随后的界面中长按二维码进行关注，关注之后即可免费为您提供以上服务。\n";
        introduction += "网址:" + bean.url;
        txtCopy.setText(introduction);
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
