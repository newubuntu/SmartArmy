package com.imniwath.read.news.amyreader;
import android.support.v4.app.Fragment;
public class PhotoActivity extends PhotoSingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return new PhotoFragment();
	}
}
