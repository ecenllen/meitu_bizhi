package com.duoduoapp.meitu.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duoduoapp.meitu.itf.IData;
import com.hlxwdsj.bj.vz.R;


/**
 * 搜索精品
 * 
 * @author Administrator
 * 
 */
public class GZHAddPengYouFrag extends Fragment{


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	private View view = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (null == view) {
			view = inflater.inflate(R.layout.gzh_addpengyoufrag, container, false);

		}
		ViewGroup p = (ViewGroup) view.getParent();
		if (p != null) {
			p.removeAllViewsInLayout();
		}

		return view;
	}


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// ADControl.addAd((LinearLayout)
		// getActivity().findViewById(R.id.AdLinearLayout), getActivity());
	}
}
