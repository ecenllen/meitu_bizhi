package com.duoduoapp.meitu.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.duoduoapp.brothers.BaseActivity;
import com.duoduoapp.meitu.MainActivity;
import com.duoduoapp.meitu.adapter.MainAdapter;
import com.duoduoapp.meitu.bean.MainListBean;
import com.duoduoapp.meitu.itf.IData;
import com.duoduoapp.meitu.utils.ADControl;
import com.duoduoapp.meitu.utils.AppConfig;
import com.duoduoapp.meitu.utils.BaiduUrlUtils;
import com.duoduoapp.meitu.utils.DataUtil;
import com.duoduoapp.meitu.utils.Logger;
import com.duoduoapp.meitu.utils.RecyclerViewOnItemClickListener;
import com.hlxwdsj.bj.vz.R;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 搜索界面
 * 
 * @author duoduoapp
 * 
 */
public class UISearch extends BaseActivity implements OnRefreshListener,OnLoadMoreListener,RecyclerViewOnItemClickListener {

	private String query = "";
	private String type = "";
	private List<MainListBean> showBeans = new ArrayList<>();
	private List<MainListBean> adapterShowBeans = new ArrayList<>();
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private LoadType loadType = LoadType.REFRESH;
	private Context context;
	private LinearLayout la_back, la_search;
	private EditText et_search;
	private LinearLayout AdLinearLayout;
	private int pn=0;
	private ADControl adControl;
	private MainAdapter adapter;
	private SwipeToLoadLayout swipeToLoadLayout;
	private RecyclerView recyclerView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.ui_search);
		context = UISearch.this;
		initView();
		initClick();
		adControl=new ADControl();
	}



	private void initView() {
		AdLinearLayout = (LinearLayout)findViewById(R.id.AdLinearLayout);
		la_back = (LinearLayout) findViewById(R.id.la_back);
		la_search = (LinearLayout) findViewById(R.id.la_search);
		et_search = (EditText) findViewById(R.id.et_search);
		adapter = new MainAdapter(context, adapterShowBeans);
		swipeToLoadLayout= (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
		recyclerView= (RecyclerView) findViewById(R.id.swipe_target_my);
		swipeToLoadLayout.setOnRefreshListener(this);
		swipeToLoadLayout.setOnLoadMoreListener(this);
		recyclerView.setLayoutManager(new GridLayoutManager(this,3));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 10);
		recyclerView.setHasFixedSize(true);
		recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
			@Override
			public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
				super.onDraw(c, parent, state);
			}

			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
				super.getItemOffsets(outRect, view, parent, state);
				outRect.top=15;

			}
		});
		adapter.setOnItemClickListener(this);
		recyclerView.setAdapter(adapter);
		recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				if (newState == RecyclerView.SCROLL_STATE_IDLE ){
					if (!ViewCompat.canScrollVertically(recyclerView, 1)){
						swipeToLoadLayout.setLoadingMore(true);
					}
				}
			}
		});

	}

	private void initClick() {
		la_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				UISearch.this.finish();
			}
		});
		la_search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				searchData();
			}
		});
	}

	private void searchData() {
		String s = et_search.getText().toString().trim();
		if (s == null || "".equals(s)) {
            Toast.makeText(context, "亲,请输入关键字后点击查询!", Toast.LENGTH_SHORT).show();
        } else {
            query = s;
			pn=0;
			loadType=LoadType.REFRESH;
            requestData();
        }
	}

	private void requestData() {
		showLoadDialog();
		loadData(query);
	}

	@SuppressLint("HandlerLeak")
	private SearchHandler uiHandler = new SearchHandler(this);

	private void loadData(final String keyword) {
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				try {
					showBeans.clear();
					showBeans = DataUtil.getBaiduBizhi(BaiduUrlUtils.getBaiduUrl(pn,keyword));
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

	private ProgressDialog progressDialog;

	private void showLoadDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context);
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
			int[] l = { 0, 0 };
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


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showHengfu();
		// QihooAdAgent.enableFloat(this);
	}

	private void showHengfu() {
		adControl.addAd(AdLinearLayout, UISearch.this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		executorService.shutdownNow();
	}

	@Override
	public void onRefresh() {
		String keyword=et_search.getText().toString().trim();
		if(TextUtils.isEmpty(keyword)){
			Toast.makeText(this,"亲，请输入关键字后搜索",Toast.LENGTH_SHORT).show();
			swipeToLoadLayout.setRefreshing(false);
			return;
		}
		if(!keyword.equals(query)){
			Toast.makeText(this,"搜索关键字发生变化，重新搜索",Toast.LENGTH_SHORT).show();
			swipeToLoadLayout.setRefreshing(false);
			searchData();
			return;
		}
		pn=0;
		loadType=LoadType.REFRESH;
		requestData();

	}

	@Override
	public void onLoadMore() {
		String keyword=et_search.getText().toString().trim();
		if(TextUtils.isEmpty(keyword)){
			Toast.makeText(this,"亲，请输入关键字后搜索",Toast.LENGTH_SHORT).show();
			swipeToLoadLayout.setLoadingMore(false);
			return;
		}
		if(!keyword.equals(query)){
			Toast.makeText(this,"搜索关键字发生变化，重新搜索",Toast.LENGTH_SHORT).show();
			swipeToLoadLayout.setLoadingMore(false);
			searchData();
			return;
		}
		pn+=20;
		loadType=LoadType.LOAD;
		requestData();
	}

	@Override
	public void onItemClick(View view, int position) {
		if ("ad".equals(adapterShowBeans.get(position).getAd_platform())) {
			AppConfig.openAD(context, adapterShowBeans.get(position), "yuansheng_count");
		} else {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClass(context, UIShowClick.class);
			intent.putExtra(IData.EXTRA_TYPE, IData.EXTRA_SHOW);
			intent.putExtra(IData.EXTRA_DATA, adapterShowBeans.get(position));
			startActivity(intent);
		}
	}
	private  static class SearchHandler extends Handler{
		WeakReference<UISearch> weakReference;

		public SearchHandler(UISearch uiSearch) {
			weakReference =new WeakReference<>(uiSearch);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			UISearch activity=weakReference.get();
			switch (msg.what) {

				case 2000: // 加载成功
					activity.hideLoadDialog();
					if (activity.adapter == null) {
						return;
					}
					if (activity.loadType == LoadType.REFRESH) {
						activity.adapterShowBeans.clear();
						activity.adapterShowBeans.addAll(AppConfig.getAdMIXShowListBeans(activity.getApplicationContext(), activity.showBeans));
						activity.recyclerView.smoothScrollToPosition(0);
						activity.adapter.notifyDataSetChanged();
					} else if (activity.loadType == LoadType.LOAD) {
						if(activity.showBeans!=null&&activity.showBeans.size()>0){
							activity.adapterShowBeans.addAll(AppConfig.getAdMIXShowListBeans(activity.getApplicationContext(), activity.showBeans));
							activity.recyclerView.smoothScrollToPosition(activity.adapterShowBeans.size()-activity.showBeans.size());
							activity.adapter.notifyDataSetChanged();
						}else {
							activity.pn-=20;
							Toast.makeText(activity.getApplicationContext(),"亲，已经是最后一页了",Toast.LENGTH_SHORT).show();
						}
					}
					activity.onLoad();
					break;
				case 3000: // 加载失败
					activity.hideLoadDialog();
					if(activity.loadType==LoadType.LOAD){
						activity.pn-=20;
					}
					activity.onLoad();
					Toast.makeText(activity.getApplicationContext(), "亲,数据加载失败了!", Toast.LENGTH_SHORT).show();
				default:
					break;
			}
		}
	}
	private void onLoad() {
		swipeToLoadLayout.setRefreshing(false);
		swipeToLoadLayout.setLoadingMore(false);
	}
}
