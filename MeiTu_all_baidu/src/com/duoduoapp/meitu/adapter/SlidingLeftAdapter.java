package com.duoduoapp.meitu.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duoduoapp.meitu.MainActivity;
import com.hlxwdsj.bj.vz.R;
import com.duoduoapp.meitu.itf.IData;

/**
 * 左侧侧滑适配器
 * 
 * @author yuxu
 * 
 */
public class SlidingLeftAdapter extends BaseAdapter{

	private Context context;
	private ViewHolder holder;
	private List<String> beans;
	private LayoutInflater inflater;
	private List<Boolean> selectors;
	private SlidingListener listener;
	public void setOnSlidingListener(SlidingListener listener){
		this.listener=listener;
	}
	public SlidingLeftAdapter(Context context, List<String> beans, List<Boolean> selectors) {
		this.context = context;
		this.beans = beans;
		inflater = LayoutInflater.from(context);
		this.selectors = selectors;
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
	public View getView(int p, View view, ViewGroup group) {
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.sliding_left_item, null);
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.kuai = view.findViewById(R.id.kuai);
			holder.content = (LinearLayout) view.findViewById(R.id.content);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}
		final int i = p;
		holder.name.setText(beans.get(i));
		if (selectors.get(p)) {
			holder.kuai.setVisibility(View.VISIBLE);
			holder.content.setBackgroundColor(context.getResources().getColor(R.color.danlvse));
		} else {
			holder.kuai.setVisibility(View.INVISIBLE);
			holder.content.setBackgroundColor(context.getResources().getColor(R.color.sliding_bg_color));
		}
		holder.content.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				for (int j = 0; j < selectors.size(); j++) {
					if (j == i) {
						selectors.set(i, true);
					} else {
						selectors.set(j, false);
					}
				}
				notifyDataSetChanged();

//				MainActivity.setTitle(beans.get(i));
				listener.onSliding(beans.get(i));
				Intent intent = new Intent();
				intent.setAction(IData.ACTION_IMAGE_CHANGED);
				intent.putExtra(IData.EXTRA_TYPE, IData.MAP_T1_FAN.get(beans.get(i)));
				context.sendBroadcast(intent);
			}
		});
		return view;
	}

	private class ViewHolder {
		public LinearLayout content;
		public View kuai;
		public TextView name;
	}
	public interface SlidingListener{
		void onSliding(String text);
	}
}
