package com.duoduoapp.meitu.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duoduoapp.meitu.bean.MainListBean;
import com.duoduoapp.meitu.bean.ShowListBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.hlxwdsj.bj.vz.R;

import java.util.List;


public class Show2Adapter extends BaseAdapter{
	private Context context;
	private List<MainListBean> beans;
	private LayoutInflater inflater;
	private double scale;
	private ViewHolder holder;
	private ImagePipeline imagePipeline;

	public Show2Adapter(Context context, List<MainListBean> beans) {
		this.context = context;
		this.beans = beans;
		inflater = LayoutInflater.from(context);
		scale=context.getResources().getDisplayMetrics().density;
		imagePipeline = Fresco.getImagePipeline();
	}

	@Override
	public void notifyDataSetChanged() {
		imagePipeline.clearMemoryCaches();
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return beans == null ? 0 : beans.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return beans == null ? null : beans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.show2_grid_item, null);
			holder.rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
			holder.iv_image = (SimpleDraweeView) view.findViewById(R.id.iv_image);
			holder.ad_log =  view.findViewById(R.id.ad_log);
			holder.tv_text= (TextView) view.findViewById(R.id.tv_text);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final int p = i;
		ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
			@Override
			public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
				if (imageInfo == null) {
					return;
				}

			}

			@Override
			public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {

			}

			@Override
			public void onFailure(String id, Throwable throwable) {
				throwable.printStackTrace();
			}
		};
		DraweeController controller;
		ImageRequest request;
		if ("ad".equals(beans.get(p).getAd_platform())) {
			request= ImageRequestBuilder.newBuilderWithSource(Uri.parse(beans.get(p).getAd_thumbnail())).setResizeOptions(new ResizeOptions((int)scale*100,(int)scale*178)).build();
			controller= Fresco.newDraweeControllerBuilder().setImageRequest(request).setControllerListener(controllerListener).build();
			holder.ad_log.setVisibility(View.VISIBLE);
			holder.tv_text.setText(beans.get(p).getAd_name());
//			holder.iv_image.setImageURI(Uri.parse(beans.get(p).getAd_thumbnail()));
		}else {
			request= ImageRequestBuilder.newBuilderWithSource(Uri.parse(beans.get(p).getHoverURL())).setResizeOptions(new ResizeOptions((int)scale*100,(int)scale*178)).build();
			controller= Fresco.newDraweeControllerBuilder().setImageRequest(request).setControllerListener(controllerListener).build();
			holder.ad_log.setVisibility(View.GONE);
			holder.tv_text.setText("1");
//		imageLoader.displayImage(beans.get(p).getQhimg_thumb_url(), holder.iv_image, options, null);
//		holder.iv_image.setImageURI(Uri.parse(beans.get(p).getHoverURL()));
		}
		holder.iv_image.setController(controller);
		return view;

	}

	private class ViewHolder {
		public RelativeLayout rl_content;
		public SimpleDraweeView iv_image;
		public View	ad_log;
		public TextView tv_text;
	}
}
