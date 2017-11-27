package com.duoduoapp.meitu.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.duoduoapp.meitu.bean.GroupBean;
import com.duoduoapp.meitu.bean.GroupListBean;
import com.duoduoapp.meitu.bean.MainListBean;
import com.duoduoapp.meitu.itf.IData;
import com.duoduoapp.meitu.utils.ADControl;
import com.duoduoapp.meitu.utils.AppConfig;
import com.duoduoapp.meitu.utils.FileUtil;
import com.duoduoapp.meitu.utils.Logger;
import com.duoduoapp.meitu.utils.ScaleUtils;
import com.duoduoapp.meitu.utils.SelectUrlUtils;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.hlxwdsj.bj.vz.R;
public class UIShowClick extends Activity{

	private Context context;
	private String type = "";
	private MainListBean showBean;
	private LinearLayout la_back, la_download, la_setpaper, la_guanzhu,setLockScreen;
	private MyViewPager pager;
	private PagerAdapter adapter;
	private final ExecutorService executorService = Executors.newFixedThreadPool(1);
	private final List<String> images = new ArrayList<String>();
	private String imageUrl = "";
	private ADControl adControl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.ui_showclick);
		context = UIShowClick.this;
		initIntent();
		initData();
		initView();
		initClick();
		adControl=new ADControl();
		//注释
	}

	private void initIntent() {
		Intent intent = getIntent();
		if (intent != null) {
			type = intent.getExtras().getString(IData.EXTRA_TYPE);
			if (IData.EXTRA_SHOW.equals(type)) {
				showBean = (MainListBean) intent.getExtras().getSerializable(IData.EXTRA_DATA);
				imageUrl = SelectUrlUtils.selectUrl(showBean);
			} else if (IData.EXTRA_MANAGER.equals(type)) {
				imageUrl = intent.getExtras().getString(IData.EXTRA_DATA);
			}
		}
	}


	private void initData() {
		images.clear();
		if (IData.EXTRA_SHOW.equals(type)) {
			images.add(imageUrl);
		} else if (IData.EXTRA_MANAGER.equals(type)) {
			images.add("file://" + imageUrl);
		}
	}

	private void initView() {
		la_back = (LinearLayout) findViewById(R.id.la_back);
		la_download = (LinearLayout) findViewById(R.id.la_download);
		la_setpaper = (LinearLayout) findViewById(R.id.la_setpaper);
		la_guanzhu = (LinearLayout) findViewById(R.id.la_guanzhu);
		setLockScreen= (LinearLayout) findViewById(R.id.la_setLockScreen);
		if(!AppConfig.isShowWXGZH()){
			la_guanzhu.setVisibility(View.GONE);
		}
		if (IData.EXTRA_MANAGER.equals(type)) {
			la_download.setVisibility(View.GONE);
		}
		pager = (MyViewPager) findViewById(R.id.vp_show);
		adapter = new ImagePagerAdapter(images);
		pager.setAdapter(adapter);
	}

	private void initClick() {
		la_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				UIShowClick.this.finish();
			}
		});
		la_guanzhu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if (AppConfig.isShowWXGZH() && AppConfig.wxgzhBeans != null && AppConfig.wxgzhBeans.size() > 0) {
					int gzhIndex = new Random().nextInt(AppConfig.wxgzhBeans.size());
					Intent intent = new Intent(context, GZHAddActivity.class);
					intent.putExtra("wxgzhbean", AppConfig.wxgzhBeans.get(gzhIndex));
					startActivity(intent);
				}
			}
		});
		la_download.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View view) {
				if (AppConfig.isShowHaoPing()) {
					if (!adControl.getIsGiveHaoping(context)) {
						adControl.homeGet5Score(UIShowClick.this);
						return;
					}
				}
				String end = "";
				end = imageEnd(imageUrl);
				if ("".equals(end)) {
					Toast.makeText(context, "亲,当前格式不支持!", Toast.LENGTH_SHORT).show();
					return;
				}
				String []arr=imageUrl.split("/");
				File file = new File(IData.DEFAULT_IMAGE_FOLDER + arr[arr.length-1]);
				if (file.exists()) {
					Toast.makeText(context, "亲,图片已经存在了!", Toast.LENGTH_SHORT).show();
				} else {
					FileBinaryResource resource = (FileBinaryResource)Fresco.getImagePipelineFactory().getMainFileCache().getResource(new
							SimpleCacheKey(images.get(0).toString()));
					if(resource!=null){
						saveImage(resource.getFile(), file, false,false);
					}else {
						Toast.makeText(context, "亲,保存失败了!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		la_setpaper.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View view) {
				if (AppConfig.isShowHaoPing()) {
					if (!adControl.getIsGiveHaoping(context)) {
						adControl.homeGet5Score(UIShowClick.this);
						return;
					}
				}
				if (!IData.EXTRA_MANAGER.equals(type)) {

					String end = "";
					end = imageEnd(imageUrl);
					if ("".equals(end)) {
						Toast.makeText(context, "当前格式不支持!", Toast.LENGTH_SHORT).show();
						return;
					}
					String []arr=imageUrl.split("/");
					File file = new File(IData.DEFAULT_IMAGE_FOLDER + arr[arr.length-1]);
					if (file.exists()) {
						setWallPaper(IData.DEFAULT_IMAGE_FOLDER + arr[arr.length-1]);
						Logger.error("set :" + IData.DEFAULT_IMAGE_FOLDER + arr[arr.length-1]);
					} else {
						Logger.error("down load :" + IData.DEFAULT_IMAGE_FOLDER + arr[arr.length-1]);
						FileBinaryResource resource = (FileBinaryResource)Fresco.getImagePipelineFactory().getMainFileCache().getResource(new
								SimpleCacheKey(images.get(0).toString()));
						if(resource!=null){
							saveImage(resource.getFile(), file, true,false);
						}else {
							Toast.makeText(context, "亲,设置失败了!", Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					setWallPaper(imageUrl);
				}
			}
		});
		setLockScreen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AppConfig.isShowHaoPing()) {
					if (!adControl.getIsGiveHaoping(context)) {
						adControl.homeGet5Score(UIShowClick.this);
						return;
					}
				}
				if(Build.VERSION.SDK_INT<Build.VERSION_CODES.N){
					Toast.makeText(UIShowClick.this,"亲，只支持Android7.0以上手机",Toast.LENGTH_SHORT).show();
					return;
				}
				if (!IData.EXTRA_MANAGER.equals(type)) {

					String end = "";
					end = imageEnd(imageUrl);
					if ("".equals(end)) {
						Toast.makeText(context, "当前格式不支持!", Toast.LENGTH_SHORT).show();
						return;
					}
					String []arr=imageUrl.split("/");
					File file = new File(IData.DEFAULT_IMAGE_FOLDER + arr[arr.length-1]);
					if (file.exists()) {
						setLockScreenWallPaper(IData.DEFAULT_IMAGE_FOLDER + arr[arr.length-1]);
						Logger.error("set :" + IData.DEFAULT_IMAGE_FOLDER + arr[arr.length-1]);
					} else {
						Logger.error("down load :" + IData.DEFAULT_IMAGE_FOLDER + arr[arr.length-1]);
						FileBinaryResource resource = (FileBinaryResource)Fresco.getImagePipelineFactory().getMainFileCache().getResource(new
								SimpleCacheKey(images.get(0).toString()));
						if(resource!=null){
							saveImage(resource.getFile(), file, false,true);
						}else {
							Toast.makeText(context, "亲,设置失败了!", Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					setLockScreenWallPaper(imageUrl);
				}
			}
		});
	}

	private void setLockScreenWallPaper(String imageUrl) {
		try {
			FileInputStream fis = new FileInputStream(imageUrl);
			Bitmap bitmap  = BitmapFactory.decodeStream(fis);
			WallpaperManager instance = WallpaperManager.getInstance(this.getApplicationContext());
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				instance.setBitmap(bitmap,null,false,WallpaperManager.FLAG_LOCK);
				Toast.makeText(this,"锁屏壁纸设置成功",Toast.LENGTH_SHORT).show();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

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


	Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

				case 4000: // 设置背景
					String where = (String) msg.obj;
					setWallPaper(where);
					break;
				case 7000: // 设置背景
					String url = (String) msg.obj;
					setLockScreenWallPaper(url);
					break;
				case 5000:
					Toast.makeText(context, "亲,保存成功了!", Toast.LENGTH_SHORT).show();
					break;
				case 6000:
					Toast.makeText(context, "亲,保存失败了!", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		}

	};


	private void saveImage(final File file, File saveFile, final boolean set,final boolean setLock) {

		String path = "";
		try {
			path = FileUtil.saveFile(file, saveFile);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.error(e);
		}
		Message msg = new Message();
		msg.obj = path;
		if (!"".equals(path)) {
			if(setLock){
				msg.what=7000;
				uiHandler.sendMessage(msg);
				return;
			}
			if (!set) { // 不设置
				uiHandler.sendEmptyMessage(5000);
			} else {
				msg.what = 4000;
				uiHandler.sendMessage(msg);
			}
		} else {
			uiHandler.sendEmptyMessage(6000);

		}
		Logger.debug("文件路径:" + path);
	}
	private void setWallPaper(String where) {
		File file = new File(where);
		if (!file.exists()) {
			return;
		}

		Bitmap bitmap= ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(where), 360, 640);
		try
		{

			WallpaperManager instance = WallpaperManager.getInstance(this.getApplicationContext());
			instance.setBitmap(bitmap);
			Toast.makeText(this,"壁纸设置成功",Toast.LENGTH_SHORT).show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private class ImagePagerAdapter extends PagerAdapter {

		private final List<String>		images;
		private final LayoutInflater	inflater;

		ImagePagerAdapter(List<String> images) {
			this.images = images;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return images == null ? 0 : images.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
			assert imageLayout != null;
			final ZoomableDraweeView imageView = (ZoomableDraweeView) imageLayout.findViewById(R.id.image);
			final int p = position;
			ImageRequest request =
					ImageRequestBuilder.newBuilderWithSource(Uri.parse(images.get(p)))
							.setResizeOptions(new ResizeOptions(ScaleUtils.getWidth(UIShowClick.this),ScaleUtils.getHeight(UIShowClick.this)))
							.setProgressiveRenderingEnabled(true)//支持图片渐进式加载
							.setAutoRotateEnabled(true) //如果图片是侧着,可以自动旋转
							.build();
			PipelineDraweeController controller =
					(PipelineDraweeController) Fresco.newDraweeControllerBuilder()
							.setImageRequest(request)
							.setOldController(imageView.getController())
							.setAutoPlayAnimations(true) //自动播放gif动画
							.build();
			imageView.setController(controller);
			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showHengfu();

	}

	private void showHengfu() {
		LinearLayout AdLinearLayout = (LinearLayout) findViewById(R.id.AdLinearLayout);
		adControl.addAd(AdLinearLayout, UIShowClick.this);

	}

}
