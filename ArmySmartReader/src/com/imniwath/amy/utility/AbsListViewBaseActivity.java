package com.imniwath.amy.utility;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AbsListView;

public class AbsListViewBaseActivity extends Activity {
	protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
	protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";
	protected AbsListView listView;
	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		pauseOnScroll = savedInstanceState.getBoolean(STATE_PAUSE_ON_SCROLL,
				false);
		pauseOnFling = savedInstanceState
				.getBoolean(STATE_PAUSE_ON_FLING, true);
	}

	@Override
	public void onResume() {
		super.onResume();
		//applyScrollListener();
	}

	private void applyScrollListener() {
		listView.setOnScrollListener(new PauseOnScrollListener(imageLoader,
				pauseOnScroll, pauseOnFling));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
		outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
	}
}
