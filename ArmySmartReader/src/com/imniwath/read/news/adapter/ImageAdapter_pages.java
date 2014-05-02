package com.imniwath.read.news.adapter;

import java.util.ArrayList;

import com.imniwath.read.news.amyreader.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageAdapter_pages extends PagerAdapter {
	private ArrayList<String> images=new ArrayList<String>();
	Context context;
    ImageAdapter_pages(Context context){
    	this.context=context;
    }
    public void additem(String s){
        images.add(s);
        } 
    @Override
    public int getCount() {
      return images.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      ImageView imageView = new ImageView(context);
      int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
      imageView.setPadding(padding, padding, padding, padding);
      imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
     // imageView.setImageResource(images.get(position);
      ((ViewPager) container).addView(imageView, 0);
      return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      ((ViewPager) container).removeView((ImageView) object);
    }
  }
