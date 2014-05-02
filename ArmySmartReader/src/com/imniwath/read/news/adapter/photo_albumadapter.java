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
import com.imniwath.read.news.bean.news_bean;
import com.imniwath.read.news.bean.photoalbum_bean;
import com.imniwath.read.news.bean.photocat_bean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class photo_albumadapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private int mItemHeight = 0;
	private int mNumColumns = 0;
	private RelativeLayout.LayoutParams mImageViewLayoutParams;
	private Context context;
	private static ArrayList<photoalbum_bean> itemsalbum;
    
	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public photo_albumadapter(Context context) {
		itemsalbum = new ArrayList<photoalbum_bean>();
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageViewLayoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		options = new DisplayImageOptions.Builder()
				//.showImageOnLoading(R.drawable.loading)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}
    public void clearItemm(){
    	if(itemsalbum!=null){itemsalbum.clear();}
    }
    public void stopimageloader(){
    	imageLoader.stop();
    }
	public void additem(photoalbum_bean bean) {
		itemsalbum.add(bean);
		notifyDataSetChanged();
	}
	public int getCount() {
		return itemsalbum.size();
	}

	// set numcols
	public void setNumColumns(int numColumns) {
		mNumColumns = numColumns;
	}

	public int getNumColumns() {
		return mNumColumns;
	}

	// set photo item height
	public void setItemHeight(int height) {
		if (height == mItemHeight) {
			return;
		}
		mItemHeight = height;
		mImageViewLayoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, mItemHeight);
		notifyDataSetChanged();
	}

	public Object getItem(int position) {
		return itemsalbum.get(position);
	}

	public Object getItemobj() {
		return itemsalbum;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		View row = convertView;
		if (row == null) {
			row = mInflater.inflate(R.layout.photo_row, null);
			holder = new ViewHolder(row);
			assert row != null;
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		holder.imageView.setLayoutParams(mImageViewLayoutParams);
		// Check the height matches our calculated column width
		if (holder.imageView.getLayoutParams().height != mItemHeight) {
			holder.imageView.setLayoutParams(mImageViewLayoutParams);
		}
		 photocat_bean itemlist = itemsalbum.get(position); 
	imageLoader.displayImage(itemlist.getImages(), holder.imageView,
				options, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						holder.progressBar.setProgress(0);
						holder.progressBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						holder.progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
						holder.progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						 holder.progressBar.setVisibility(View.GONE);
					}
				});
	 
		// holder.imageView.setImageResource(R.drawable.cover_britney);
		String summary = itemlist.getTitle();
		if (summary.length() > 100) {
			summary = summary.substring(0, 100);
			summary = summary + "...";
		}
		holder.title.setText(summary);
		return row;
	}

	private class ViewHolder {
		public ImageView imageView;
		public ProgressBar progressBar;
		public TextView title;

		public ViewHolder(View v) {
			imageView = (ImageView) v.findViewById(R.id.cover);
			title = (TextView) v.findViewById(R.id.title);
			progressBar = (ProgressBar) v.findViewById(R.id.progressarticle);
		}
	}
}
