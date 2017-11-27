package com.duoduoapp.meitu;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.duoduoapp.brothers.AbsBaseFragment;
import com.hlxwdsj.bj.vz.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 以后fragment需继承此类,可以使用 下拉刷新,上拉加载更多,图片异步加载等功能
 * 
 * @author Shanlin
 * 
 */
public class BaseFragment extends AbsBaseFragment {

	public static DisplayImageOptions options;
	public static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		options = new DisplayImageOptions.Builder() //
				.showImageOnLoading(R.drawable.ic_stub)//
				.showImageForEmptyUri(R.drawable.ic_empty)//
				.showImageOnFail(R.drawable.ic_error)//
				.cacheInMemory(true)//
				.cacheOnDisk(true)//
				.considerExifParams(true)//
				.displayer(new RoundedBitmapDisplayer(20))//
				.build();

	}

	@Override
	public void onDestroy() {
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
