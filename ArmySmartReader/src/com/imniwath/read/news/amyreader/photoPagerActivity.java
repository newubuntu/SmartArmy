package com.imniwath.read.news.amyreader;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.imniwath.read.news.adapter.photo_adapter;
import com.imniwath.read.news.bean.photo_bean;

public class photoPagerActivity extends Activity {
	private static final String STATE_POSITION = "STATE_POSITION";
	private photo_adapter apadter;
	private ArrayList<photo_bean> data_in;
	private int position_album;
	ViewPager pager;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_pages_layout);
		pager = (ViewPager) findViewById(R.id.pager);
		Bundle bundle = getIntent().getExtras();	
		assert bundle != null;	
		if (savedInstanceState != null) {
	    	//pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}
	    if (getIntent().getExtras() != null) {
		data_in = (ArrayList<photo_bean>) bundle.getSerializable("album_data");
		position_album = (int) bundle.getInt("album_position", 0);
		apadter=new photo_adapter(getApplicationContext(), data_in);	
	    pager.setAdapter(apadter);
		pager.setCurrentItem(position_album);
	    }else{   	
	    }
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}
}