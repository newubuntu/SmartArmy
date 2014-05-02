package com.imniwath.read.news.adapter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.maxwin.view.XListView;


import com.dodowaterfall.widget.ScaleImageView;
import com.imniwath.android.bitmapfun.util.ImageFetcher;
import com.imniwath.read.news.amyreader.R;
import com.imniwath.read.news.bean.news_bean;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class adapterNews extends BaseAdapter {
	private Context mContext;
    private ArrayList<news_bean> list_news_obj;
    private XListView mListView;
    public  ImageFetcher mImageFetcher;
    
    public adapterNews(Context context, XListView xListView) {
        mContext = context;
        list_news_obj = new ArrayList<news_bean>();
        mListView = xListView;
        mImageFetcher=new ImageFetcher(mContext, 240);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
    }
    public void clearItemm(){
    	if(list_news_obj!=null){list_news_obj.clear();}
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        news_bean iterm_list = list_news_obj.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
            convertView = layoutInflator.inflate(R.layout.row_main_news, null);
            holder = new ViewHolder();
            holder.imageView = (ScaleImageView) convertView.findViewById(R.id.news_pic);
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
        holder.imageView.setImageWidth(iterm_list.getWidth());
        holder.imageView.setImageHeight(iterm_list.getHeight());
        holder.contentView.setText(summary);
        holder.timesView.setText("อ่าน "+iterm_list.getRead()+" ครั้ง");
      //holder.contentView.setText(iterm_list.getTimes());
        mImageFetcher.loadImage(iterm_list.getImages(), holder.imageView);
        
        /*
        convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                 
			}
		});*/
        return convertView;
    }

    class ViewHolder {
        ScaleImageView imageView;
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
