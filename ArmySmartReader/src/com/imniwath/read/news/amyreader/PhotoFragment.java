package com.imniwath.read.news.amyreader;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.imniwath.amy.utility.API;
import com.imniwath.read.news.bean.photo_bean;
import com.imniwath.read.news.bean.photoalbum_bean;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class PhotoFragment extends Fragment implements OnItemClickListener {
	private RequestHandle lastRequest; 
	private GridView mGridView;
	private Context context;
	public Intent intent;
	public GalleryItemAdapter photo_adapter;
	public photoalbum_bean album;
	ArrayList<photo_bean> mItems;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		intent=getActivity().getIntent();
	    if (intent.getExtras() != null) {
	    	album = (photoalbum_bean) this.intent.getSerializableExtra("album_id");  
	    }
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tumblr_gallery, container, false);
		mGridView = (GridView) view.findViewById(R.id.gallery_gridView);
		mItems=new ArrayList<photo_bean>();
		loadList(mItems);
		setupAdapter();
		return view;
	}

	void setupAdapter() {
		if (getActivity() == null || mGridView == null) return;
		if (mItems != null) {
			photo_adapter=new GalleryItemAdapter(mItems);
			mGridView.setAdapter(photo_adapter);
			mGridView.setOnItemClickListener(this);
		} else {
			mGridView.setAdapter(null);
		}
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		photo_adapter.stoploadimg();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private class GalleryItemAdapter extends ArrayAdapter<photo_bean> {
		private DisplayImageOptions options;
		protected ImageLoader imageLoader = ImageLoader.getInstance();
		public GalleryItemAdapter(ArrayList<photo_bean> items) {
			super(getActivity(), 0, items);
			setupimageuption();
		}
		private void setupimageuption(){
			options = new DisplayImageOptions.Builder()
			//.showImageOnLoading(R.drawable.loading)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
			.cacheOnDisc(true).considerExifParams(true)
			.imageScaleType(ImageScaleType.NONE)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new FadeInBitmapDisplayer(300)).build();			
		}
		public void stoploadimg(){
			imageLoader.stop();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder=new ViewHolder();
				convertView = getActivity().getLayoutInflater().inflate(R.layout.gallery_item, parent, false);
				holder.imageView = (ImageView) convertView.findViewById(R.id.gallery_item_imageView);
				//holder.progressBar=(ProgressBar) convertView.findViewById(R.id.progressphoto);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			//ImageView imageView = (ImageView) convertView.findViewById(R.id.gallery_item_imageView);
			//imageView.setImageResource(R.drawable.sample);
			photo_bean item = getItem(position);

			//ImageSize targetSize = new ImageSize(250,250); // result Bitmap will be fit to this size
			//Bitmap bmp = imageLoader.loadImageSync(item.getPhotosmall(), targetSize, options);
			//holder.imageView.setImageBitmap(bmp);
		 
			imageLoader.displayImage(item.getPhotosmall(),holder.imageView,
						options, new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String arg0, View arg1) {
							//	holder.progressBar.setVisibility(View.VISIBLE);
							}

							@Override
							public void onLoadingFailed(String arg0, View arg1,
									FailReason arg2) {
							//	holder.progressBar.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingComplete(String arg0, View arg1,
									Bitmap arg2) {
							//	holder.progressBar.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingCancelled(String arg0, View arg1) {
							//	holder.progressBar.setVisibility(View.GONE);
							}
						});
			 
			/*
			 ImageSize imagesize=new ImageSize(420, 300);
			 imageLoader.loadImage(item.getPhoto(), imagesize, options, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					holder.progressBar.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					holder.progressBar.setVisibility(View.GONE);	
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap bm) {
					Log.e("niwath", "hieght"+bm.getHeight()+"width:"+bm.getWidth());
					holder.imageView.setImageBitmap(bm);
					holder.progressBar.setVisibility(View.GONE);
					 
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					holder.progressBar.setVisibility(View.GONE);
				}
			});*/
			return convertView;
		}
		private class ViewHolder {
			public ImageView imageView;
			//public ProgressBar progressBar;
		}	
	}
	protected void loadList(final ArrayList<photo_bean> dataItems) {
		RequestParams param = new RequestParams();
		param.put("category", "1");
		param.put("num", "0");
		try { // api/photo/33/64/0  "api/photo/"+album.getId_cat()+"/"+album.getId()+"/0"
			lastRequest = API.get("api/photo/"+album.getId_cat()+"/"+album.getId()+"/0", param,
					new JsonHttpResponseHandler() {
						@Override
						public void onStart() {
							super.onStart();
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								String responseBody) {
							try {
								JSONObject jdata = new JSONObject(responseBody);
								if (!jdata.getBoolean("error")) {
									JSONArray jrootone = jdata
											.getJSONArray("datacontent");
									int loop = jrootone.length();
									if (jrootone != null) {			
										for (int i = 0; i < loop; i++) {
											JSONObject getroot = (JSONObject) jdata
													.getJSONArray("datacontent")
													.get(i);
											photo_bean bean = new photo_bean();
											bean.setId(getroot.getInt("id"));
											bean.setPhoto(getroot.getString("images"));
											bean.setTimes(getroot.getString("times"));
											bean.setPhotosmall(getroot.getString("images_small"));
											dataItems.add(bean);
											setupAdapter();
									}
								} else {
								
								  }
							}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onProgress(int bytesWritten, int totalSize) {
							Log.v("niwath",
									String.format(
											"Progress 2 %d from %d (%d%%)",
											bytesWritten,
											totalSize,
											(totalSize > 0) ? (bytesWritten / totalSize) * 10
													: -1));
						};

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							Toast.makeText(context.getApplicationContext(), "ไม่พบข้อมูล !", 300).show();
							if (lastRequest != null) {
								lastRequest.cancel(true);
							}
						}

						@Override
						public void onFinish() {
							super.onFinish();
							if (lastRequest != null) {
								lastRequest.cancel(true);
							}
						}
					});
		} catch (Exception e) {
			Toast.makeText(getActivity(),
					"ERROR" + e.getMessage() + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		} finally {
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("album_data", mItems);
		bundle.putInt("album_position",position);
		Intent intent = new Intent(getActivity(),photoPagerActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
