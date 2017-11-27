package com.duoduoapp.meitu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.duoduoapp.brothers.BaseActivity;
import com.duoduoapp.meitu.adapter.ManagerAdapter;
import com.duoduoapp.meitu.itf.IData;
import com.duoduoapp.meitu.ui.UIShowClick;
import com.duoduoapp.meitu.utils.ADControl;
import com.hlxwdsj.bj.vz.R;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ManagerActivity extends BaseActivity{

	private final ExecutorService executorService = Executors.newFixedThreadPool(1);
	private final List<String> fileLists = new ArrayList<String>();
	private final List<String> adapterLists = new ArrayList<String>();
	private final List<Boolean> filters = new ArrayList<Boolean>();
	private ManagerAdapter adapter;
	private GridView gridView;
	private Context context;

	private LinearLayout title_layout_back;
	private RelativeLayout rl_edit, rl_delok, rl_del, rl_ok;
	private ADControl adControl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_manager);
		context = ManagerActivity.this;
		initReceiver();
		initView();
		initClick();
		initData();
		adControl = new ADControl();
	}

	private void initData() {
		fileLists.clear();
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				String[] lists = new File(IData.DEFAULT_IMAGE_FOLDER).list();
				if (lists == null) {
					return;
				}
				for (int i = 0; i < lists.length; i++) {
					if (!"".equals(imageEnd(lists[i]))) {
						fileLists.add(IData.DEFAULT_IMAGE_FOLDER + lists[i]);
					}
				}
				uiHandler.sendEmptyMessage(2000);
			}
		});
	}

	private void initView() {
		title_layout_back = (LinearLayout) findViewById(R.id.title_layout_back);
		rl_edit = (RelativeLayout) findViewById(R.id.rl_edit);
		rl_delok = (RelativeLayout) findViewById(R.id.rl_delok);
		rl_del = (RelativeLayout) findViewById(R.id.rl_del);
		rl_ok = (RelativeLayout) findViewById(R.id.rl_ok);
		gridView = (GridView) findViewById(R.id.gridView);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ManagerAdapter(context, adapterLists, filters);
		gridView.setAdapter(adapter);

		rl_edit.setVisibility(View.VISIBLE);
		rl_delok.setVisibility(View.GONE);
	}

	private int index = 0;

	private void initClick() {

		title_layout_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ManagerActivity.this.finish();
			}
		});
		rl_edit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (adapter == null) {
					return;
				}
				if (adapterLists.isEmpty()) {
					Toast.makeText(context, "亲,当前没有图片哦!", Toast.LENGTH_SHORT).show();
					return;
				}
				rl_edit.setVisibility(View.GONE);
				rl_delok.setVisibility(View.VISIBLE);
				adapter.showEdit();

			}
		});
		rl_del.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (filters.isEmpty()) {
					Toast.makeText(context, "亲,已经删除完了哦!", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!filters.contains(true)) {
					Toast.makeText(context, "亲,请选择一张或多张哦!", Toast.LENGTH_SHORT).show();
					return;
				}
				for (int i = 0; i < filters.size(); i++) {
					if (filters.get(i)) {
						File file = new File(adapterLists.get(i));
						if (file.exists()) {
							index++;
							file.delete();
						}
					}
				}
				rl_edit.setVisibility(View.VISIBLE);
				rl_delok.setVisibility(View.GONE);
				adapter.hideEdit();
				uiHandler.sendEmptyMessage(1000);
				Toast.makeText(context, "亲,成功删除" + index + "张图片哦!", Toast.LENGTH_SHORT).show();
				index = 0;
			}
		});
		rl_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				index = 0;
				rl_edit.setVisibility(View.VISIBLE);
				rl_delok.setVisibility(View.GONE);
				for (int i = 0; i < filters.size(); i++) {
					filters.set(i, false);
				}
				adapter.hideEdit();
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, int i, long l) {
				if (adapter.isShowEdit()) {

					if (!filters.isEmpty()) {
						if (filters.get(i)) {
							filters.set(i, false);
						} else {
							filters.set(i, true);

						}
						adapter.notifyDataSetChanged();
					}
				} else {
					Intent intent = new Intent();
					intent.setClass(context, UIShowClick.class);
					intent.putExtra(IData.EXTRA_TYPE, IData.EXTRA_MANAGER);
					intent.putExtra(IData.EXTRA_DATA, adapterLists.get(i));
					context.startActivity(intent);
				}
			}
		});
	}

	BroadcastReceiver receiver;

	private void initReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(IData.ACTION_IMAGE_CHANGED);
		receiver = new UpData();
		registerReceiver(receiver, filter);
	}

	Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1000:
					initData();
					break;
				case 2000:
					if (adapter == null) {
						return;
					}
					adapterLists.clear();
					filters.clear();
					adapterLists.addAll(fileLists);
					for (int i = 0; i < adapterLists.size(); i++) {
						filters.add(false);
					}
					adapter.notifyDataSetChanged();
					break;
				default:
					break;
			}

		}

		;
	};

	private String imageEnd(String imageUrl) {
		String re = "";
		if (imageUrl.endsWith(".jpg")) {
			re = ".jpg";
		} else if (imageUrl.endsWith(".png")) {
			re = ".png";
		} else if (imageUrl.endsWith(".jpng")) {
			re = ".jpng";
		}
		return re;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}

	long time = 0;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showHengfu();
		// QihooAdAgent.enableFloat(this);
	}

	private void showHengfu() {
		LinearLayout AdLinearLayout = (LinearLayout) findViewById(R.id.AdLinearLayout);
		adControl.addAd(AdLinearLayout, ManagerActivity.this);

	}

	class UpData extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			uiHandler.sendEmptyMessage(1000);
		}

	}

}
