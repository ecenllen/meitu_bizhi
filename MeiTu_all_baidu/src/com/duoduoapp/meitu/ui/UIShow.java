package com.duoduoapp.meitu.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.duoduoapp.meitu.BaseFragment;
import com.duoduoapp.meitu.adapter.ShowAdapter;
import com.duoduoapp.meitu.bean.QueryBean;
import com.duoduoapp.meitu.bean.ShowBean;
import com.duoduoapp.meitu.bean.ShowListBean;
import com.duoduoapp.meitu.itf.IData;
import com.duoduoapp.meitu.utils.ADControl;
import com.duoduoapp.meitu.utils.AppConfig;
import com.duoduoapp.meitu.utils.DataUtil;
import com.duoduoapp.meitu.utils.Logger;
import com.hlxwdsj.bj.vz.R;
import com.markmao.pulltorefresh.widget.XScrollView;
import com.markmao.pulltorefresh.widget.XScrollView.IXScrollViewListener;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint({ "HandlerLeak", "ValidFragment" })
public class UIShow extends BaseFragment{

	private final QueryBean queryBean;
	private String type = "";
	private List<ShowBean> showBeans = new ArrayList<ShowBean>();
	private final List<ShowListBean> adapterShowBeans = new ArrayList<ShowListBean>();
	private ShowAdapter adapter;
	private MyGridView gridView;
	private String listType = "hot";
	private final ExecutorService executorService = Executors.newFixedThreadPool(1);
	private LoadType loadType;
	private Context context;
	LinearLayout lyt360;
	LinearLayout AdLinearLayout;
	private ADControl adControl;
	public UIShow(QueryBean queryBean) {
		// TODO Auto-generated constructor stub
		this.queryBean = queryBean;
		listType = queryBean.getListtype();
		type = queryBean.getT1();
		loadType = LoadType.REFRESH;
	}

	BroadcastReceiver	receiver;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.context = getActivity();
		IntentFilter filter = new IntentFilter();
		filter.addAction(IData.ACTION_IMAGE_CHANGED);
		receiver = new Type();
		getActivity().registerReceiver(receiver, filter);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ui_show, container, false);
		adControl=new ADControl();
		scrollView = (XScrollView) view.findViewById(R.id.sv_refresh);
		lyt360 = (LinearLayout) view.findViewById(R.id.lyt360);
		AdLinearLayout = (LinearLayout) view.findViewById(R.id.AdLinearLayout);
		scrollView.setPullLoadEnable(true);
		scrollView.setPullRefreshEnable(true);
		scrollView.setAutoLoadEnable(false);
		scrollView.setIXScrollViewListener(listener);
		scrollView.setRefreshTime(getTime());
		gridView = (MyGridView) LayoutInflater.from(getActivity()).inflate(R.layout.show_grid, null);
		adapter = new ShowAdapter(getActivity(), adapterShowBeans);
		gridView.setAdapter(adapter);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		scrollView.setView(gridView);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, int i, long l) {
                if ("ad".equals(adapterShowBeans.get(i).getAd_platform())) {

                    AppConfig.openAD(context, adapterShowBeans.get(i),"yuansheng_count");
                } else {
                    Intent intent = new Intent();
                    intent.setClass(context, UIShowClick.class);
                    intent.putExtra(IData.EXTRA_TYPE, IData.EXTRA_SHOW);
                    intent.putExtra(IData.EXTRA_DATA, adapterShowBeans.get(i));
                    getActivity().startActivity(intent);
                }
			}
		});
		Message msg = new Message();
		msg.what = 1000;
		msg.obj = queryBean;
		uiHandler.sendMessage(msg);
		showHengfu();
		return view;
	}


	Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1000: // 加载数据
					showLoadDialog();
					QueryBean queryBean = (QueryBean) msg.obj;
					loadData(queryBean);
					break;
				case 2000: // 加载成功
					hideLoadDialog();
					if (adapter == null) {
						return;
					}
					if (loadType == LoadType.REFRESH) {
						adapterShowBeans.clear();
						adapterShowBeans.addAll(AppConfig.getMIXShowListBeans(context, showBeans.get(0).getShowListBeans()));
						gridView.setSelection(1);
						adapter.notifyDataSetChanged();
					} else if (loadType == LoadType.LOAD) {
						adapterShowBeans.addAll(AppConfig.getMIXShowListBeans(context, showBeans.get(0).getShowListBeans()));
						adapter.notifyDataSetChanged();
					}
					onLoad();
					break;
				case 3000: // 加载失败
					hideLoadDialog();
					onLoad();
					Toast.makeText(context, "亲,数据加载失败了!", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		}

	};

	private void loadData(final QueryBean queryBean) {
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				try {
					showBeans.clear();
					showBeans = DataUtil.getShowBeans(queryBean);
					uiHandler.sendEmptyMessage(2000);
				} catch (IOException e) {
					e.printStackTrace();
					uiHandler.sendEmptyMessage(3000);
					Logger.error(e);
				} catch (JSONException e) {
					Logger.error(e);
					e.printStackTrace();
					uiHandler.sendEmptyMessage(3000);
				}
			}
		});
	}

	private ProgressDialog	progressDialog;

	private void showLoadDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(getActivity());
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

	@SuppressLint("SimpleDateFormat")
	private String getTime() {
		String re = "";
		re = new SimpleDateFormat(IData.DATE_FORMAT).format(new Date(System.currentTimeMillis()));
		return re;
	}

	// ////////////////////////下拉刷新,上啦加载//////////////////////////////

	IXScrollViewListener	listener	= new IXScrollViewListener() {

											@Override
											public void onRefresh() {
												loadType = LoadType.REFRESH;
												QueryBean bean = new QueryBean(type, listType);
												loadData(bean);
											}

											@Override
											public void onLoadMore() {
												loadType = LoadType.LOAD;
												QueryBean bean = new QueryBean(type, listType);
												if (showBeans.isEmpty()) {
													loadData(bean);
												} else {
													if (showBeans.get(0).isEnd()) {
														onLoad();
														uiHandler.post(new Runnable() {
															@Override
															public void run() {
																Toast.makeText(getActivity(), "亲,已经是最后一页了哦!", Toast.LENGTH_SHORT).show();
															}
														});
														return;
													}
													bean.setSn(showBeans.get(0).getLastid() + "");
													loadData(bean);
												}

											}
										};

	private void onLoad() {
		onLoad(true);
	}

	private void onLoad(boolean refresh) {
		scrollView.stopRefresh();
		scrollView.stopLoadMore();
		if (refresh) {
			scrollView.setRefreshTime(getTime());
		}
	}

	long	time	= 0;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (System.currentTimeMillis() - time > 120 * 1000) {
			time = System.currentTimeMillis();
			// showChaping();
		}
	}

	private void showHengfu() {
		adControl.addAd(AdLinearLayout, getActivity());

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		executorService.shutdownNow();
		if (receiver != null && context != null) {
			context.unregisterReceiver(receiver);
		}
	}

	class Type extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null) {
				return;
			}
			loadType = LoadType.REFRESH;
			type = intent.getStringExtra(IData.EXTRA_TYPE);
			QueryBean bean = new QueryBean(type, listType);
			Message msg = new Message();
			msg.what = 1000;
			msg.obj = bean;
			uiHandler.sendMessage(msg);
		}

	}
}
