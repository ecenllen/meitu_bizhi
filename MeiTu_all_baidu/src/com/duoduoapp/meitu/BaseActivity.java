package com.duoduoapp.meitu;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.duoduoapp.brothers.SlidingBaseActivity;
import com.hlxwdsj.bj.vz.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 基类,本类包含XListView(下拉刷新,下拉加载) imageloader(图片异步加载),slidingmenu(侧滑), 
 * @author Shanlin
 *
 */
public class BaseActivity extends SlidingBaseActivity {

	
	public static DisplayImageOptions options;
	public static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		options = new DisplayImageOptions.Builder() //
		.showImageOnLoading(R.drawable.ic_stub)// 加载时显示
		.showImageForEmptyUri(R.drawable.ic_empty)// 连接空显示
		.showImageOnFail(R.drawable.ic_error)// 加载出错显示
		.cacheInMemory(true)//
		.cacheOnDisk(true)//
		.considerExifParams(true)//
		.displayer(new RoundedBitmapDisplayer(20))//
		.build();
		
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AnimateFirstDisplayListener.displayedImages.clear();
	}
	
	public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

}
