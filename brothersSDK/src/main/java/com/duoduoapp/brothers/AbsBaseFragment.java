package com.duoduoapp.brothers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.widget.ListView;

import com.markmao.pulltorefresh.widget.XListView;
import com.markmao.pulltorefresh.widget.XScrollView;


@SuppressLint("NewApi")
public class AbsBaseFragment extends Fragment {


	protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
	protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";

	protected XListView mXListView;
	protected ListView listView;
	protected XScrollView scrollView;
	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;

	@Override
	public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onInflate(activity, attrs, savedInstanceState);
		if (savedInstanceState != null) {
			pauseOnScroll = savedInstanceState.getBoolean(STATE_PAUSE_ON_SCROLL, false);
			pauseOnFling = savedInstanceState.getBoolean(STATE_PAUSE_ON_FLING, true);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
		outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}


}
