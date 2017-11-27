package com.duoduoapp.meitu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.hlxwdsj.bj.vz.R;
import com.duoduoapp.meitu.bean.ShowListBean;


public class ShowAdapter extends BaseAdapter{
	private Context context;
	private List<ShowListBean> beans;
	private LayoutInflater inflater;

	private ViewHolder holder;

	public ShowAdapter(Context context, List<ShowListBean> beans) {
		this.context = context;
		this.beans = beans;

		inflater = LayoutInflater.from(context);
		// int width = ((Activity)
		// context).getWindowManager().getDefaultDisplay().getWidth();
		// int height = ((Activity)
		// context).getWindowManager().getDefaultDisplay().getHeight();
		// Logger.debug("当前屏幕分辨率:" + width + "X" + height);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return beans == null ? 0 : beans.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return beans.get(arg0);
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
			view = inflater.inflate(R.layout.show_grid_item, null);
			holder.rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
			holder.iv_image = (SimpleDraweeView) view.findViewById(R.id.iv_image);
			holder.tv_text = (TextView) view.findViewById(R.id.tv_text);
			holder.ad_log =  view.findViewById(R.id.ad_log);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final int p = i;
		if ("ad".equals(beans.get(p).getAd_platform())) {
			holder.ad_log.setVisibility(View.VISIBLE);
			holder.tv_text.setText(beans.get(p).getAd_name());
			holder.iv_image.setImageURI(beans.get(p).getAd_thumbnail());
		}else {
			holder.ad_log.setVisibility(View.GONE);
		holder.tv_text.setText("( " + beans.get(p).getTotal_count() + " )");
//		imageLoader.displayImage(beans.get(p).getQhimg_thumb_url(), holder.iv_image, options, null);
		holder.iv_image.setImageURI(beans.get(p).getQhimg_thumb_url());
		}
		return view;

	}

	private class ViewHolder {
		public RelativeLayout rl_content;
		public SimpleDraweeView iv_image;
		public TextView tv_text;
		public View	ad_log;
	}
}
