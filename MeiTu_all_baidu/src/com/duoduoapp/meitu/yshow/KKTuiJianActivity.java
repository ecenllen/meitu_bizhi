package com.duoduoapp.meitu.yshow;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.duoduoapp.meitu.utils.ADControl;
import com.duoduoapp.meitu.yshow.bean.DialogFactory;
import com.duoduoapp.meitu.yshow.bean.JsonParse;
import com.duoduoapp.meitu.yshow.bean.kkjsonModel;
import com.hlxwdsj.bj.vz.R;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

//import com.youngfhsher.fishertv.dyd.Um;

public class KKTuiJianActivity extends Activity {

	private final long	time		= System.currentTimeMillis();
	private String		TEST_FILE_NAME;
	private Dialog		mDialog;
	private kkjsonModel kkmodellist	= new kkjsonModel();

	private void showRequestDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "正在加载美人数据，请不要关闭...");
		mDialog.show();
	}
	private ADControl adControl;
	private void ShowMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private void uploadThreadMethod() {
		if (!isNetOK()) {
			Message msg = new Message();
			msg.arg1 = 2;// ��ȡʧ��
			myHandler.sendMessage(msg);
			return;
		}
		showRequestDialog();
		Thread uploadthread = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					kkmodellist = JsonParse
							.ParseKKChangXiangModel("http://www.kktv1.com/CDN/output/M/3/I/10002002/P/start-0_offset-200_platform-2/json.js");
					Message msg = new Message();
					msg.arg1 = 0;// ��ȡʧ��
					myHandler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message msg = new Message();
					msg.arg1 = 1;// ��ȡʧ��
					myHandler.sendMessage(msg);
				}
			}
		});
		uploadthread.start();
	}

	private final Handler	myHandler	= new Handler() {
											@Override
											public void handleMessage(Message msg) {
												super.handleMessage(msg);
												if (mDialog != null && mDialog.isShowing()) {
													mDialog.dismiss();
												}
												switch (msg.arg1) {
												case 0:
													KKActivitykkAdapter adapter = new KKActivitykkAdapter(kkmodellist.roomList,
															KKTuiJianActivity.this, type);
													gridviewtv.setAdapter(adapter);
													// gridviewtv.setOnItemClickListener(new
													// OnItemClickListener() {
													//
													// @Override
													// public void
													// onItemClick(AdapterView<?>
													// arg0, View arg1, int
													// position, long arg3) {
													// Intent intent = new
													// Intent(KKTuiJianActivity.this,
													// KKLiveActivity.class);
													// // 房间ID(必选)
													// intent.putExtra("roomId",
													// kkmodellist.roomList.get(position).roomId);
													// startActivity(intent);
													// }
													// });
													break;
												case 1:// �û������������ڷ�����������
													ShowMessage("获取美人数据失败!!");
													break;

												case 2:
													Toast.makeText(KKTuiJianActivity.this, "没有网络连接",Toast.LENGTH_SHORT).show();
													break;
												}
											};
										};

	public boolean isNetOK() {
		ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		return wifi || internet;
	}

	private GridView	gridviewtv;
	private int			type;

	void markDir(String dir) {
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
		if (sdCardExist) {

			File dirfile = new File(dir);
			// System.out.println("�洢·����" + dir);
			if (!dirfile.exists()) {
				dirfile.mkdir();
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.kkchangxiangactivity);
		type = getIntent().getExtras().getInt("ordertype", 0);
		adControl=new ADControl();
		gridviewtv = (GridView) findViewById(R.id.gridviewtv);//
		uploadThreadMethod();

		MobclickAgent.onResume(KKTuiJianActivity.this);
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 AddBaiduAd();
	}

	private void AddBaiduAd() {
		LinearLayout baiduad = (LinearLayout) findViewById(R.id.baiduad);
		adControl.addAd(baiduad, this);
	}
	long	exitTime	= 0;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		adControl.ShowTPAD(this);
	}

	/**
	 * ��װapk
	 * */
	private void installApk(String filename) {
		File file = new File(filename);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(Uri.fromFile(file), type);
		startActivity(intent);

	}

	int								fileSize;
	int								downLoadFileSize;
	String							fileEx, fileNa, filename;
	private final ProgressDialog	mpDialog	= null;

}
