package com.duoduoapp.meitu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hlxwdsj.bj.vz.R;


public class ManagerAdapter extends BaseAdapter{
	private Context context;
	private List<String> beans;
	private LayoutInflater inflater;

	private ViewHolder holder;
	private List<Boolean> filters;
	private boolean isShowEdit = false;

	public ManagerAdapter(Context context, List<String> beans, List<Boolean> filters) {
		this.context = context;
		this.beans = beans;

		inflater = LayoutInflater.from(context);
		this.filters = filters;
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

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.show_grid_item, null);
			holder.rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
			holder.iv_image = (SimpleDraweeView) view.findViewById(R.id.iv_image);
			holder.iv_edit = (ImageView) view.findViewById(R.id.iv_edit);
			holder.tv_text = (TextView) view.findViewById(R.id.tv_text);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final int p = i;
		holder.tv_text.setVisibility(View.GONE);
//		imageLoader.displayImage("file://" + beans.get(p), holder.iv_image, options, null);
		DraweeController controller= Fresco.newDraweeControllerBuilder()
				.setUri("file://" +beans.get(p))
				.setAutoPlayAnimations(true)
				.build();

		holder.iv_image.setController(controller);
//		holder.iv_image.setImageURI();
		if (isShowEdit) {
			holder.iv_edit.setVisibility(View.VISIBLE);
			if (filters.get(p)) {
				holder.iv_edit.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.checkbox_ok));
			} else {
				holder.iv_edit.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.checkbox_no));
			}
		} else {
			holder.iv_edit.setVisibility(View.GONE);
		}
		return view;
	}

	public void showEdit() {
		isShowEdit = true;
		notifyDataSetChanged();
	}

	public void hideEdit() {
		isShowEdit = false;
		notifyDataSetChanged();
	}

	public boolean isShowEdit() {
		return isShowEdit;
	}

	private class ViewHolder {
		@SuppressWarnings("unused")
		public RelativeLayout rl_content;
		public SimpleDraweeView iv_image;
		public  ImageView iv_edit;
		public TextView tv_text;
	}
}
