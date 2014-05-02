package com.imniwath.read.news.adapter;

import java.util.ArrayList;

import android.content.ClipData.Item;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imniwath.read.news.amyreader.R;
import com.imniwath.read.news.bean.menu_bean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class Menu_adapter extends BaseAdapter {
	private Context context;
	private static ArrayList<menu_bean> items;
	DisplayImageOptions options;
	protected LayoutInflater layoutInflater;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	public Menu_adapter(Context context) {
		this.context=context;
		items = new ArrayList<menu_bean>();
		layoutInflater = LayoutInflater.from(this.context);
		options = new DisplayImageOptions.Builder()
			    .showImageOnLoading(R.drawable.loading)
			    .showImageForEmptyUri(R.drawable.ic_empty)
			    .showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}
    public void clearItemm(){
    	if(items!=null){items.clear();}
    }
    public Boolean isnulldata(){
    	if(items==null){
    	return true;	
    	}else{
    	 return false;	
    	}
    	 
    }
    public void stopimageloader(){
    	imageLoader.stop();
    }
	public void additem(menu_bean bean) {
		items.add(bean);
		notifyDataSetChanged();
	}
	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return items.get(position);
	}
	public ArrayList<menu_bean> getItemobj() {
		return items;
	}
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		View row = convertView;
		if (row == null) {
			row =layoutInflater.inflate(R.layout.sliding_nav_items, null);
			holder = new ViewHolder();
			holder.navText=(TextView)row.findViewById(R.id.navText);
			holder.imageView=(ImageView) row.findViewById(R.id.icon_menu);
			assert row != null;
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		 menu_bean itemlist = items.get(position);
		 if(itemlist.getIcon()!=null){
			 imageLoader.displayImage(itemlist.getIcon(), holder.imageView,
						options, new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String arg0, View arg1) {
								//holder.progressBar.setProgress(0);
								//holder.progressBar.setVisibility(View.VISIBLE);
							}

							@Override
							public void onLoadingFailed(String arg0, View arg1,
									FailReason arg2) {
								//holder.progressBar.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingComplete(String arg0, View arg1,
									Bitmap arg2) {
								//holder.progressBar.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingCancelled(String arg0, View arg1) {
								// holder.progressBar.setVisibility(View.GONE);
							}
						});
		 }else{
		 holder.imageView.setImageResource(R.drawable.ic_home); 
		 }
		String summary = itemlist.getName();
		if (summary.length() > 20) {
			summary = summary.substring(0, 20);
			summary = summary + "...";
		}
		holder.navText.setText(summary);
		return row;
	}
	private class ViewHolder {
		public ImageView imageView;
		public ProgressBar progressBar;
		public TextView navText;
	}
}
