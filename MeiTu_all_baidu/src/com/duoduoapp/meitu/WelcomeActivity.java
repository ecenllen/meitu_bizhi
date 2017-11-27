package com.duoduoapp.meitu;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duoduoapp.meitu.itf.IData;
import com.duoduoapp.meitu.permission.PermissionActivity;
import com.duoduoapp.meitu.permission.PermissionChecker;
import com.duoduoapp.meitu.utils.ADControl;
import com.duoduoapp.meitu.utils.AppConfig;
import com.duoduoapp.meitu.utils.KPAdListener;
import com.duoduoapp.meitu.utils.PlayerFileUtil;
import com.hlxwdsj.bj.vz.R;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author  admin
 */
public class WelcomeActivity extends Activity {


	// /**
	// * 存入数据库过关情况
	// */
	// public static List<DetailedBean> dbDetailedBeans = new
	// ArrayList<DetailedBean>();
	// /**
	// * 关卡集合
	// */
	// public static List<DetailedBean> detailedBeans = new
	// ArrayList<DetailedBean>();
	//
	// public static DBHelper dbHelper;
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private Context context;
	public static char[] hanzi = null;

	private SharedPreferences mSettings;
	private TextView txtappname;

	/**
	 * 当设置开屏可点击时，需要等待跳转页面关闭后，再切换至您的主窗口。故此时需要增加waitingOnRestart判断。
	 * 另外，点击开屏还需要在onRestart中调用jumpWhenCanClick接口。
	 */
	public boolean waitingOnRestart = false;
	static final String[] PERMISSIONS = new String[]{
			Manifest.permission.CAMERA,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.READ_PHONE_STATE,
			Manifest.permission.RECORD_AUDIO,


	};
	private static final int REQUEST_CODE = 0; // 请求码
	private PermissionChecker mPermissionChecker; // 权限检测器
	private ADControl adControl;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome);
		context = WelcomeActivity.this;
		mPermissionChecker=new PermissionChecker(this);
		initVersionCode();
		adControl=new ADControl();


	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mPermissionChecker.lackPermissions(PERMISSIONS)) {
			startPermissionsActivity();
		}else {
			initAppConfig();
			createFolder();
			initAds();
		}
	}

	private void initAppConfig() {
		ApplicationInfo appInfo;
		try {
			appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			AppConfig.versioncode=GetVersionCode(this);
			AppConfig.APPKEY = appInfo.metaData.getString("UMENG_APPKEY");
			AppConfig.Channel = appInfo.metaData.getString("UMENG_CHANNEL");
		} catch (PackageManager.NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		AppConfig.youkulibPath = getCacheDir() + File.separator + "videoparse.jar";// 初始化引擎存放位置
		AppConfig.GZHPath = IData.DEFAULT_GZH_CACHE ;// 公众号的目录不能用缓存目录
		AppConfig.InitLocal(this);
	}
	public  String GetVersionCode(Context context) {
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			return String.valueOf(info.versionCode); //获取版本cood
		} catch (PackageManager.NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	private void createFolder() {
		PlayerFileUtil.creatFolder(IData.DEFAULT_DISK_CACHE);
		PlayerFileUtil.creatFolder(IData.DEFAULT_DOWNLOADE_FOLDER);
		PlayerFileUtil.creatFolder(IData.DEFAULT_FILES_CACHE);
		PlayerFileUtil.creatFolder(IData.DEFAULT_GZH_CACHE);
	}
	private void startPermissionsActivity() {
		PermissionActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 拒绝时, 关闭页面, 缺少主要权限, 无法运行
		if (requestCode == REQUEST_CODE && resultCode == PermissionActivity.PERMISSIONS_DENIED) {
			finish();
		}
	}
	private void initAds() {
		adControl.initAll(WelcomeActivity.this);
		adControl.lastshowadTime = 0;
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				AppConfig.Init(context);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (AppConfig.isShowKP()) {
							adControl.ShowKp(context, (RelativeLayout) WelcomeActivity.this.findViewById(R.id.adsRl), listener);
						}else
						{
							jump();
						}
					}
				});
			}
		});
	}

	private void initVersionCode() {
		txtappname = (TextView) findViewById(R.id.txtappname);
		try {
			txtappname.setText(getString(R.string.app_name) + "(版本:" + getVersionName(WelcomeActivity.this, getPackageName()) + ")");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (waitingOnRestart) {
			jumpWhenCanClick();
		}
	}

	KPAdListener listener = new KPAdListener() {
		@Override
		public void onAdDismissed() {
			Log.i("RSplashActivity", "onAdDismissed");
			jumpWhenCanClick();// 跳转至您的应用主界面
		}

		@Override
		public void onAdFailed(String arg0) {
			Log.i("RSplashActivity", arg0);
			WelcomeActivity.this.findViewById(R.id.adsRl).setVisibility(View.INVISIBLE);
			WelcomeActivity.this.findViewById(R.id.lyt_bottom).setVisibility(View.VISIBLE);
			jump();
		}

		@Override
		public void onAdPresent() {
			Log.i("RSplashActivity", "onAdPresent");
		}

		@Override
		public void onAdClick() {
			Log.i("RSplashActivity", "onAdClick");
			// 设置开屏可接受点击时，该回调可用
		}
	};


	public static String getVersionName(Context context, String packageName) throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(packageName, 0);
		String version = packInfo.versionName;
		return version;
	}

	private void jumpWhenCanClick() {
		Log.d("test", "this.hasWindowFocus():" + this.hasWindowFocus());
		if (this.hasWindowFocus() || waitingOnRestart) {
			adControl.initAll(WelcomeActivity.this);
			mSettings = getSharedPreferences("userinfo", Context.MODE_PRIVATE); // 模式为私有
			Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
			startActivity(intent);
			WelcomeActivity.this.finish();
		} else {
			waitingOnRestart = true;
		}

	}

	/**
	 * 不可点击的开屏，使用该jump方法，而不是用jumpWhenCanClick
	 */
	private void jump() {
		adControl.initAll(WelcomeActivity.this);
		mSettings = getSharedPreferences("userinfo", Context.MODE_PRIVATE); // 模式为私有
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
			}
		}, 1000);
	}
}
