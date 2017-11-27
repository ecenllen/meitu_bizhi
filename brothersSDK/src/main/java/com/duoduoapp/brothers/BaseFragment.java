package com.duoduoapp.brothers;

import android.os.Bundle;

/**
 * 以后fragment需继承此类,可以使用 下拉刷新,上拉加载更多,图片异步加载等功能
 * @author Shanlin
 *
 */
public class BaseFragment extends AbsBaseFragment {



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

}
