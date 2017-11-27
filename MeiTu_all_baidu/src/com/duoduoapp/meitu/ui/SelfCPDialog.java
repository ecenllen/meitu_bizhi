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

import com.duoduoapp.meitu.utils.ADBean;
import com.duoduoapp.meitu.utils.AppConfig;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hlxwdsj.bj.vz.R;

import java.util.List;

public class SelfCPDialog extends Dialog {
private SelfBannerAdListener listener;
    private SimpleDraweeView my_image_view;
   private View rl_content,ad_close;
	Context context;
	ADBean bean;
	public SelfCPDialog(final Context context) {
		super(context, R.style.dialog);
		this.context=context;

		List<ADBean> beans= AppConfig.GetSelfADByCount(context,1,"cp_count");
		if(null!=beans&&beans.size()==1)
		{bean=beans.get(0);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view =LayoutInflater.from(context).inflate(R.layout.selfcpdialog,null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		my_image_view = (SimpleDraweeView) view.findViewById(R.id.my_image_view);
		rl_content = view.findViewById(R.id.rl_content);
		ad_close = view.findViewById(R.id.ad_close);
		rl_content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener!=null)
					listener.onAdClick(bean);
				AppConfig.openAD(context,bean,"cp_count");
				SelfCPDialog.this.dismiss();
			}
		});
		ad_close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SelfCPDialog.this.dismiss();
				System.out.println("广告被关闭");
			}
		});
//		Window dialogWindow = getWindow();
//		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		Display display =((Activity) context).getWindowManager().getDefaultDisplay(); // 获取屏幕宽、高用
//		lp.width = (int) (display.widthPixels * 0.8); // 高度设置为屏幕的0.6
//		dialogWindow.setAttributes(lp);
		if(bean!=null)
		{
			ViewGroup.LayoutParams params = my_image_view.getLayoutParams();
			params.width = (int)(display.getWidth()*0.8);
			params.height =  (int) (bean.getAd_thumbnailscal() * params.width);
			my_image_view.setLayoutParams(params);
			my_image_view.setImageURI(bean.getAd_thumbnail());
			if(listener!=null)
				listener.onADReceiv(bean);
		}
		else
		{
			if(listener!=null)
				listener.onAdFailed();
			this.dismiss();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	public void setADListener(SelfBannerAdListener listener){
		this.listener=listener;
	}

}