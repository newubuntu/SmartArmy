package com.imniwath.read.news.adapter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.maxwin.view.XListView;


import com.dodowaterfall.widget.ScaleImageView;
import com.imniwath.android.bitmapfun.util.ImageFetcher;
import com.imniwath.read.news.amyreader.R;
import com.imniwath.read.news.bean.news_bean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;



import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class adapterNews extends BaseAdapter {
	private Context mContext;
    private ArrayList<news_bean> list_news_obj;
    private XListView mListView;
   // public  ImageFetcher mImageFetcher;
    DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    public adapterNews(Context context, XListView xListView) {
        mContext = context;
        list_news_obj = new ArrayList<news_bean>();
        mListView = xListView;
       // mImageFetcher=new ImageFetcher(mContext, 240);
       // mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.loading)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
		.cacheOnDisc(true).considerExifParams(true)
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(400)).build();
    }
    public void clearItemm(){
    	if(list_news_obj!=null){list_news_obj.clear();}
    }
    public void stopimageloader(){
    	imageLoader.stop();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        news_bean iterm_list = list_news_obj.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
            convertView = layoutInflator.inflate(R.layout.row_main_news, null);
            holder = new ViewHolder();
        //    holder.imageView = (ScaleImageView) convertView.findViewById(R.id.news_pic);
            holder.imageView=(ImageView) convertView.findViewById(R.id.news_pic);
            holder.contentView = (TextView) convertView.findViewById(R.id.news_title);
            holder.timesView = (TextView) convertView.findViewById(R.id.news_time);
            convertView.setTag(holder);
        }
		String summary = iterm_list.getTitle();
		if (summary.length() > 20) {
			summary = summary.substring(0, 20);
			summary = summary + "...";
		}
		
        holder = (ViewHolder) convertView.getTag();
      //  holder.imageView.setImageWidth(iterm_list.getWidth());
      //  holder.imageView.setImageHeight(iterm_list.getHeight());
        holder.contentView.setText(summary);
        holder.timesView.setText("อ่าน "+iterm_list.getRead()+" ครั้ง");
      //holder.contentView.setText(iterm_list.getTimes());
      //  mImageFetcher.loadImage(iterm_list.getImages(), holder.imageView);
 	   imageLoader.displayImage(iterm_list.getImages(), holder.imageView,
 				options, new ImageLoadingListener() {
 					@Override
 					public void onLoadingStarted(String arg0, View arg1) {
 					//	holder.progressBar.setProgress(0);
 					//	holder.progressBar.setVisibility(View.VISIBLE);
 					}

 					@Override
 					public void onLoadingFailed(String arg0, View arg1,
 							FailReason arg2) {
 						//holder.progressBar.setVisibility(View.GONE);
 						imageLoader.stop(); 
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
        /*
        convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                 
			}
		});*/
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView contentView;
        TextView timesView;
    }

    @Override
    public int getCount() {
        return list_news_obj.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list_news_obj.get(arg0);
    }
	public Object get_Itemobj() {
		return list_news_obj;
	}
    @Override
    public long getItemId(int arg0) {
        return 0;
    }
	public void getidnew() {
		int loop=list_news_obj.size();
		for(int i=0;i<loop;i++){
			news_bean iterm_list = list_news_obj.get(i);
			int id=iterm_list.getId();
			 
		}
	}
    public void addItemLast(news_bean datas) {
        list_news_obj.add(datas);
		notifyDataSetChanged();
    }
/*
    public void addItemTop(List<news_bean_item> datas) {
        for (news_bean_item info : datas) {
            list_news_obj.addFirst(info);
        }
    }*/
}
