package com.duoduoapp.meitu.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.duoduoapp.meitu.ui.GDTMuBanTuiPingDialog;
import com.duoduoapp.meitu.ui.GDTTuiPingDialog;
import com.duoduoapp.meitu.ui.SelfBannerAdListener;
import com.duoduoapp.meitu.ui.SelfBannerView;
import com.duoduoapp.meitu.ui.SelfCPDialog;
import com.duoduoapp.meitu.ui.SelfKPAdListener;
import com.duoduoapp.meitu.ui.SelfKPView;
import com.duoduoapp.meitu.ui.SelfTuiPingDialog;
import com.duoduoapp.meitu.ui.UpdateDialog;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.BannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.qq.e.ads.interstitial.InterstitialADListener;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

import java.io.Serializable;
import java.util.HashMap;

//import com.baidu.mobads.SplashAd;
//import com.baidu.mobads.SplashAdListener;
//import com.qq.e.ads.splash.SplashAD;
//import com.qq.e.ads.splash.SplashADListener;

public class ADControl {

	private static int score = 0;

	public  long lastshowadTime = 0;


	public static Boolean isonshow = false;
	private static boolean ISGiveHaoping = false;
	private static HashMap<String, String> giveHaoping = new HashMap<String, String>();
	private  Activity context;


	private static long showadTimeDuration = 120 * 1000;

	public  String GetChannel(Activity context) {
		if (Channel == null || "".equals(Channel)) {
			initChannel(context);
		}
		return Channel;
	}

	public  String GetVersionCode(Activity context) {
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			return String.valueOf(info.versionCode); //
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String oldADVersition = "";

	public  void ChangeTVAddrVersion(Context context, String newVersion) {
		SharedPreferences mSettings = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		Editor editor = mSettings.edit();
		editor.putString("addrversion", newVersion);
		editor.commit();
		ADControl.oldADVersition = newVersion;
	}

	public  void initAll(Activity context) {
		this.context = context;

	}

	public  void initChannel(Context context) {
		try {
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			String msg = appInfo.metaData.getString("UMENG_CHANNEL");
			this.Channel = msg;
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}

	}


	private  void ShowGDTKP(Context context, RelativeLayout adsParent, final KPAdListener kpAdListener, String appid, String adplaceid) {

		SplashADListener listener = new SplashADListener() {

			@Override
			public void onADDismissed() {
				kpAdListener.onAdDismissed();
			}

			@Override
			public void onNoAD(AdError adError) {

				kpAdListener.onAdFailed(adError != null ? adError.getErrorMsg() : "");
			}

			@Override
			public void onADPresent() {
				kpAdListener.onAdPresent();
			}

			@Override
			public void onADClicked() {
				kpAdListener.onAdClick();
			}

			@Override
			public void onADTick(long l) {

			}
		};
		new SplashAD((Activity) context, adsParent, appid, adplaceid, listener);
	}

	private  void ShowSelfKP(final Context context, RelativeLayout adsParent, final KPAdListener kpAdListener) {

		SelfKPAdListener listener = new SelfKPAdListener() {
			@Override
			public void onAdDismissed(ADBean bean) {//广告展示完毕
				kpAdListener.onAdDismissed();
			}

			@Override
			public void onAdFailed(ADBean bean) {//广告获取失败
				kpAdListener.onAdFailed("");
			}

			@Override
			public void onAdPresent(ADBean bean) {//广告开始展示
				kpAdListener.onAdPresent();
//                if (bean != null && !TextUtils.isEmpty(bean.getAd_name())) {
//                    Map<String, String> map_ekv = new HashMap<String, String>();
//                    map_ekv.put("show", bean.getAd_name());
//                    MobclickAgent.onEvent(context, "kp_count", map_ekv);
//                }
			}

			@Override
			public void onAdClick(ADBean bean) {//广告被点击
				kpAdListener.onAdClick();
//                if (bean != null && !TextUtils.isEmpty(bean.getAd_name())) {
//                    Map<String, String> map_ekv = new HashMap<String, String>();
//                    map_ekv.put("click", bean.getAd_name());
//                    MobclickAgent.onEvent(context, "kp_count", map_ekv);
//                }
			}
		};
		SelfKPView selfKPView = new SelfKPView(context);
		selfKPView.setADListener(listener);
		adsParent.removeAllViews();
		adsParent.addView(selfKPView);
	}

	//初始化广点通退屏广告
	public  Boolean InitGDTTP(Context context) {
		if (AppConfig.isShowTP())//展示退屏广告
		{
			String kpType = AppConfig.getTPType();//获取开屏广告类型，baidu，gdt，google
			String kp_String = AppConfig.configBean.ad_tp_idMap.get(kpType);
			if (!TextUtils.isEmpty(kp_String)) {
				String[] a = kp_String.split(",");
				if (a.length == 2) {
					String appid = a[0];
					String adplaceid = a[1];
					if ("gdt".equals(kpType)) {
						GDTTuiPingDialog.Init(context, appid, adplaceid);
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else//不展示开屏广告
		{
			return false;
		}

	}

	//初始化广点通退屏广告
	public  Boolean InitGDTMuBanTP(Context context) {
		if (AppConfig.isShowTP())//展示退屏广告
		{
			String kpType = AppConfig.getTPType();//获取开屏广告类型，baidu，gdt，google
			String kp_String = AppConfig.configBean.ad_tp_idMap.get(kpType);
			if (!TextUtils.isEmpty(kp_String)) {
				String[] a = kp_String.split(",");
				if (a.length == 2) {
					String appid = a[0];
					String adplaceid = a[1];
					if ("gdtmb".equals(kpType)) {
						GDTMuBanTuiPingDialog.Init(context, appid, adplaceid);
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else//不展示开屏广告
		{
			return false;
		}

	}

	//初始化广点通退屏广告
	public  Boolean ShowTPAD(Context context) {
		if (AppConfig.isShowTP())//展示开屏广告
		{
			String kpType = AppConfig.getTPType();//获取开屏广告类型，baidu，gdt，google
			String kp_String = AppConfig.configBean.ad_tp_idMap.get(kpType);
			if (!TextUtils.isEmpty(kpType) && "self".equals(kpType)) {//退屏类型为自家的
				SelfTuiPingDialog sfCP = new SelfTuiPingDialog(context);
				sfCP.show();
				return false;
			} else if (!TextUtils.isEmpty(kp_String)) {//并非自家的，来自广点通，百度等，目前只有广点通
				String[] a = kp_String.split(",");
				if (a.length == 2) {
					String appid = a[0];
					String adplaceid = a[1];
					if ("gdt".equals(kpType)) {
						GDTTuiPingDialog sfCP = new GDTTuiPingDialog(context);

						sfCP.show();
						return true;
					} else if ("gdtmb".equals(kpType)) {
						GDTMuBanTuiPingDialog sfCP = new GDTMuBanTuiPingDialog(context);
						sfCP.show();
						return true;
					} else {//有两个id，但是又不是广点通
						SelfTuiPingDialog sfCP = new SelfTuiPingDialog(context, null);
						sfCP.show();
						return false;
					}
				} else {//id没有两个，则暂时表示配置有问题，如果以后某个平台id只有一个，则重新写该方法
					SelfTuiPingDialog sfCP = new SelfTuiPingDialog(context, null);
					sfCP.show();
					return false;
				}
			} else {//如果返回的id为空，又不展示自家广告，这种情况可能是后台配置错误，则不展示广告
				SelfTuiPingDialog sfCP = new SelfTuiPingDialog(context, null);
				sfCP.show();
				return true;
			}
		} else//不展示退屏广告
		{
			SelfTuiPingDialog sfCP = new SelfTuiPingDialog(context, null);
			sfCP.show();
			return false;
		}

	}

	public  void ShowKp(Context context, RelativeLayout adsParent, final KPAdListener kpAdListener) {
		if (AppConfig.isShowKP())//展示开屏广告
		{
			String kpType = AppConfig.getKPType();//获取开屏广告类型，baidu，gdt，google
			String kp_String = AppConfig.configBean.ad_kp_idMap.get(kpType);
			if (!TextUtils.isEmpty(kp_String)) {
				String[] a = kp_String.split(",");
				if (a.length == 2) {
					String appid = a[0];
					String adplaceid = a[1];
					if ("baidu".equals(kpType)) {
						ShowSelfKP(context, adsParent, kpAdListener);
					} else if ("gdt".equals(kpType)) {
						ShowGDTKP(context, adsParent, kpAdListener, appid, adplaceid);
					} else {
						kpAdListener.onAdFailed("其他不支持广告类型" + kp_String);
					}
				} else {
					kpAdListener.onAdFailed("后台获取开屏广告的id为" + kp_String);
				}
			} else {
				ShowSelfKP(context, adsParent, kpAdListener);
			}
		} else//不展示开屏广告
		{
			kpAdListener.onAdFailed("后台不展示开屏广告");
		}

	}



	private  void ShowGDTCP(Context context, String appid, String adplaceid) {


//        SplashAd.setAppSid(context, appid);// 其中的debug需改为您的APPSID
		final InterstitialAD interAd = new InterstitialAD((Activity) context, appid, adplaceid);
		interAd.setADListener(new InterstitialADListener() {
			@Override
			public void onADReceive() {
				interAd.show();
			}

			@Override
			public void onNoAD(AdError adError) {
				lastshowadTime = 0;
			}


			@Override
			public void onADOpened() {

			}

			@Override
			public void onADExposure() {

			}

			@Override
			public void onADClicked() {

			}

			@Override
			public void onADLeftApplication() {

			}

			@Override
			public void onADClosed() {

			}
		});
		interAd.loadAD();
	}

	private  void ShowSelfCP(final Context context) {

		SelfCPDialog sfCP = new SelfCPDialog(context);
		sfCP.setADListener(new SelfBannerAdListener() {
			@Override
			public void onAdClick(ADBean adBean) {
			}

			@Override
			public void onAdFailed() {

			}

			@Override
			public void onADReceiv(ADBean adBean) {
			}
		});
		sfCP.show();

	}

	public  void ShowCp(Context context) {
		if (AppConfig.isShowCP())//展示开屏广告
		{
			if (System.currentTimeMillis() - lastshowadTime < showadTimeDuration) {
				System.out.println("广告时间没到" + (System.currentTimeMillis() - lastshowadTime));
				return;
			}
			lastshowadTime = System.currentTimeMillis();
			String cpType = AppConfig.getCPType();//获取开屏广告类型，baidu，gdt，google
			String kp_String = AppConfig.configBean.ad_cp_idMap.get(cpType);
			if (!TextUtils.isEmpty(kp_String)) {
				String[] a = kp_String.split(",");
				if (a.length == 2) {
					String appid = a[0];
					String adplaceid = a[1];
					if ("baidu".equals(cpType)) {
						ShowSelfCP(context);
					} else if ("gdt".equals(cpType)) {
						ShowGDTCP(context, appid, adplaceid);
					} else if ("self".equals(cpType)) {
						ShowSelfCP(context);
					} else {
						// kpAdListener.onAdFailed("其他不支持广告类型" + kp_String);
					}
				} else {
					// kpAdListener.onAdFailed("后台获取开屏广告的id为" + kp_String);
				}
			} else {
				ShowSelfCP(context);
			}
		} else//不展示开屏广告
		{
			//  kpAdListener.onAdFailed("后台不展示开屏广告");
		}

	}




	public  void addGDTBanner(final LinearLayout lyt, final Activity context, String appid, String adplaceid) {

		lyt.removeAllViews();
		try {
			BannerView bv = new BannerView(context, ADSize.BANNER, appid, adplaceid);
			// 注意：如果开发者的banner不是始终展示在屏幕中的话，请关闭自动刷新，否则将导致曝光率过低。
			// 并且应该自行处理：当banner广告区域出现在屏幕后，再手动loadAD。
			bv.setRefresh(30);
			bv.setADListener(new BannerADListener() {

				@Override
				public void onADClicked() {
					System.out.println("广点通广告被点击");
				}

				@Override
				public void onADLeftApplication() {

				}

				@Override
				public void onADOpenOverlay() {

				}

				@Override
				public void onADCloseOverlay() {

				}

				@Override
				public void onNoAD(AdError adError) {
					addSelfBanner(lyt, context);
				}

				@Override
				public void onADReceiv() {

				}

				@Override
				public void onADExposure() {

				}

				@Override
				public void onADClosed() {

				}
			});
			bv.loadAD();
			lyt.addView(bv);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public  void addGoogleBanner(final LinearLayout lyt, final Activity context, String appid, String adplaceid) {

		lyt.removeAllViews();
//        try {
//            com.google.android.gms.ads.AdView   mAdView = new com.google.android.gms.ads.AdView(context);
//            lyt.addView(mAdView);
//            mAdView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
//            mAdView.setAdUnitId("ca-app-pub-1430179761243955/3577290620");
//            // Create an ad request. Check your logcat output for the hashed device ID to
//            // get test ads on a physical device. e.g.
//            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
//            com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder()
//                    .build();
//            // Start loading the ad in the background.
//            mAdView.loadAd(adRequest);
//
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }

	}

	public  void addSelfBanner(LinearLayout lyt, final Activity context) {

		lyt.removeAllViews();
		try {
			SelfBannerView bv = new SelfBannerView(context);
			bv.setADListener(new SelfBannerAdListener() {
				@Override
				public void onAdClick(ADBean adBean) {
//                    if (adBean != null && !TextUtils.isEmpty(adBean.getAd_name())) {
//                        Map<String, String> map_ekv = new HashMap<String, String>();
//                        map_ekv.put("click", adBean.getAd_name());
//                        MobclickAgent.onEvent(context, "banner_count", map_ekv);
//                        System.out.println("广告被点击:"+adBean.getAd_name());
//                    }
				}

				@Override
				public void onAdFailed() {

				}

				@Override
				public void onADReceiv(ADBean adBean) {
//                    if (adBean != null && !TextUtils.isEmpty(adBean.getAd_name())) {
//                        Map<String, String> map_ekv = new HashMap<String, String>();
//                        map_ekv.put("show", adBean.getAd_name());
//                        MobclickAgent.onEvent(context, "banner_count", map_ekv);
//                        System.out.println("广告被展示:"+adBean.getAd_name());
//                    }
				}
			});
			lyt.addView(bv);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public  void addAd(LinearLayout lyt, Activity context) {
		ShowCp(context);
		if (GDTTuiPingDialog.adItems == null || GDTTuiPingDialog.adItems.size() == 0 || System.currentTimeMillis() - GDTTuiPingDialog.initTime > 45 * 60 * 1000) {
			InitGDTTP(context);
		}
		if (GDTMuBanTuiPingDialog.adItems == null || GDTMuBanTuiPingDialog.adItems.size() == 0 || System.currentTimeMillis() - GDTMuBanTuiPingDialog.initTime > 45 * 60 * 1000) {
			InitGDTMuBanTP(context);
		}
		if (AppConfig.isShowBanner())//展示广告条广告
		{
			String bannerType = AppConfig.getBannerType();//获取开屏广告类型，baidu，gdt，google
			String banner_String = AppConfig.configBean.ad_banner_idMap.get(bannerType);
			if (!TextUtils.isEmpty(banner_String)) {
				String[] a = banner_String.split(",");
				if (a.length == 2) {
					String appid = a[0];
					String adplaceid = a[1];
					if ("baidu".equals(bannerType)) {
						addSelfBanner(lyt, context);
					} else if ("gdt".equals(bannerType)) {
						addGDTBanner(lyt, context, appid, adplaceid);
					} else if ("google".equals(bannerType)) {
						addGoogleBanner(lyt, context, appid, adplaceid);
					} else if ("self".equals(bannerType)) {
						addSelfBanner(lyt, context);
					} else {
//                        kpAdListener.onAdFailed("其他不支持广告类型" + kp_String);
					}
				} else {

//                    kpAdListener.onAdFailed("后台获取开屏广告的id为" + kp_String);
				}
			} else {
				addSelfBanner(lyt, context);
			}
		} else//不展示banner
		{
//            kpAdListener.onAdFailed("后台不展示开屏广告");
		}

	}

	public  void setISGiveHaoping(Context context, Boolean isgivehaoping) {
		ISGiveHaoping = isgivehaoping;
		SharedPreferences mSettings = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE); //
		Editor editor = mSettings.edit();
		editor.putBoolean("ISGiveHaoping", true);
		editor.commit();
	}

	public  boolean getIsGiveHaoping(Context context) {
		SharedPreferences mSettings = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		ISGiveHaoping = mSettings.getBoolean("ISGiveHaoping", false);
		return ISGiveHaoping;
	}

	public  void setScore(Context context, int score) {
		ADControl.score = score;
		SharedPreferences mSettings = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		Editor editor = mSettings.edit();
		editor.putInt("score", ADControl.score);
		editor.commit();
	}

	public static int getScore(Context context) {
		SharedPreferences mSettings = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		return mSettings.getInt("score", -1);

	}

	public  void homeGet5Score(final Activity context) {

		if (getIsGiveHaoping(context)) {
			return;
		}
		if (!AppConfig.isShowHaoPing()) {
			return;
		}
		isonshow = true;

		new AlertDialog.Builder(context).setTitle("申请开放更多节目源")
				.setMessage("\t\t在市场给5星好评,即可开放更多壁纸图片资源！")
				.setPositiveButton("给个好评", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						setISGiveHaoping(context, true);
						goodPinglun(context);
						isonshow = false;
					}
				}).setNegativeButton("以后再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				isonshow = false;
			}
		}).setCancelable(false).show();
	}

	/*
    * 加入QQ群的代码
    * */
	public  boolean joinQQGroup(Context context) {
		try {
			String key = AppConfig.publicConfigBean.qqKey;
			if (key == null || "".equals(key)) {
				Toast.makeText(context, "请手工添加QQ群:286239217", Toast.LENGTH_SHORT).show();
				return false;
			}
			Intent intent = new Intent();
			intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
			// 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

			context.startActivity(intent);
			return true;
		} catch (Exception e) {
			Toast.makeText(context, "请手工添加QQ群:286239217", Toast.LENGTH_SHORT).show();
			// 未安装手Q或安装的版本不支持
			return false;
		}
	}

	public  void goodPinglun(Activity activity) {
		Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		try {
			activity.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(activity, "异常", Toast.LENGTH_SHORT).show();
		}

	}


	public  String Channel = "";


	public  boolean update(Context context) {

		if (AppConfig.isShowUpdate()) {
			int currentVersion = 0;
			try {
				// ---get the package info---
				PackageManager pm = context.getPackageManager();
				PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
				currentVersion = pi.versionCode;
				if (currentVersion < AppConfig.configBean.updatemsg.versioncode) {
					UpdateDialog dg = new UpdateDialog(context);


					dg.show();
//                    Window dialogWindow = dg.getWindow();
//                    Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
//
//                    WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//                    p.x=display.
//                    p.height = (int) (display.getHeight() * 0.8); // 高度设置为屏幕的0.6，根据实际情况调整
//                    p.width = (int) (display.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
//                    dialogWindow.setAttributes(p);
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				Log.e("VersionInfo", "Exception", e);
			}
			return false;

		} else{
			return false;
		}
	}

	public  void updateLiveVersion() {
	}

	public  boolean isLiveUrlUpdate(Activity context) {
		return true;
	}


	static class Config implements Serializable {

		/**
		 *
		 */
		private static final long serialVersionUID = -700152136951314809L;
		String version = "";

		public Config(String version) {
			this.version = version;
		}
	}
}
