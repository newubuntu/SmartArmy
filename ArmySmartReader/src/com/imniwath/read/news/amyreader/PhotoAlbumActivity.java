package com.imniwath.read.news.amyreader;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.imniwath.amy.utility.API;
import com.imniwath.read.news.adapter.photo_albumadapter;
import com.imniwath.read.news.bean.photoalbum_bean;
import com.imniwath.read.news.bean.photocat_bean;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
public class PhotoAlbumActivity extends BaseActivity implements OnItemClickListener{
	private GridView photoGrid;
	private photocat_bean cat_resive;
	private photoalbum_bean album_send =new photoalbum_bean();
	private int mPhotoSize, mPhotoSpacing;
	private photo_albumadapter album_adapter;
	private ArrayList<photoalbum_bean>album_obj =new ArrayList<photoalbum_bean>();
	private String pathurl = null;
	private int pages = 0;
	private RequestHandle lastRequest;
	private Button bntback;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_album_layout);
		//menuList = new ListView(this);
		// get the photo size and spacing
		mPhotoSize = getResources().getDimensionPixelSize(R.dimen.photo_size);
		mPhotoSpacing = getResources().getDimensionPixelSize(
				R.dimen.photo_spacing);
		bntback=(Button) findViewById(R.id.h_menu_bnt_back);
		bntback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			 Intent in=new Intent(getApplicationContext(), PhotoCategoryActivity.class);
			 startActivity(in);
			 finish();
			}
		});
		if (getIntent().getExtras() != null) {
			cat_resive = (photocat_bean) this.getIntent().getSerializableExtra("photoalbum");
		}
		// initialize image adapter
		album_adapter = new photo_albumadapter(getApplicationContext());
		photoGrid = (GridView) findViewById(R.id.albumGrid);
		photoGrid.setHovered(true);
		// set image adapter to the GridView
		photoGrid.setAdapter(album_adapter);
		photoGrid.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (album_adapter.getNumColumns() == 0) {
							final int numColumns = (int) Math.floor(photoGrid
									.getWidth() / (mPhotoSize + mPhotoSpacing));
							if (numColumns > 0) {
								final int columnWidth = (photoGrid.getWidth() / numColumns)
										- mPhotoSpacing;
								album_adapter.setNumColumns(numColumns);
								album_adapter.setItemHeight(columnWidth);

							}
						}
					}
				});
		photoGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
			     album_obj = (ArrayList<photoalbum_bean>) album_adapter.getItemobj();
				//Log.e("debug send", itemsobj.get(pos).getTitle());
				album_send = album_obj.get(pos);
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						PhotoActivity.class);
				intent.putExtra ("album_id", album_send);
				startActivity(intent);
			}
		});

	}
	public void onResume() {
		super.onResume();
	    this.reloadList();
	}
	protected void reloadList() {
		Log.e("niwath", "new load"+getClass());
		RequestParams param = new RequestParams();
		param.put("category", "1");
		param.put("num", "0");
		try {
			lastRequest = API.get("api/photoalbum/"+cat_resive.getId()+"/0", param,
					new JsonHttpResponseHandler() {
						@Override
						public void onStart() {
							super.onStart();
							
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								String responseBody) {
							Log.e("debugonSuccess 1", responseBody);
							
							try {
								JSONObject jdata = new JSONObject(
										responseBody);
								if (!jdata.getBoolean("error")) {
									Log.e("niwath", "niwath");
									JSONArray jrootone = jdata
											.getJSONArray("datacontent");
									int loop = jrootone.length();
									if (jrootone != null) {
										album_adapter.clearItemm();
										for (int i = 0; i < loop; i++) {
											JSONObject getroot = (JSONObject) jdata
													.getJSONArray("datacontent")
													.get(i);
											photoalbum_bean bean = new photoalbum_bean();
											bean.setId(getroot.getInt("id"));
											bean.setTitle(getroot
													.getString("titles"));
											bean.setImages(getroot
															.getString("images"));
											bean.setId_cat(getroot.getInt("id_cat"));
											album_adapter.additem(bean);
										}
									}
									Log.e("debugonSuccess 1.1",
											Integer.toString(jrootone.length()));
								} else {
									showmsg();
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
							Log.e("niwath", "onFailure 2");
							Toast.makeText(getApplicationContext(), "ไม่พบข้อมูล !", 300).show();
							if (lastRequest != null) {
								Log.e("niwath", "cancel Request");
								lastRequest.cancel(true);
							}
						}

						@Override
						public void onFinish() {
							super.onFinish();	
							Log.e("niwath", "onFinish 2");
							if (lastRequest != null) {
								Log.e("niwath", "cancel Request");
								lastRequest.cancel(true);
							}
						}
					});
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"ERROR" + e.getMessage() + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		} finally {

		}
	}
 

	public String geturl() {
		return this.pathurl;
	}

	public void seturl(int i) {
		if (i == 123456789) {
			this.pathurl = "newsall/" + getpages();
		} else {
			this.pathurl = "newsbycat/" + i + "/" + getpages();
		}
	}

	public int getpages() {
		pages = 0;
		return pages;
	}
	@Override
	void clickAction() {
		super.clickAction();
		lv.setOnItemClickListener(this);
	}
	public void showmsg() {
		Toast.makeText(getApplicationContext(), "ไม่พบข้อมูล",
				Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		menuClickAction(position);
	}
	@Override
	void menuClickAction(int position) {
		super.menuClickAction(position);
		Mdata=Madapter.getItemobj();
		Mterm=Mdata.get(position);
		int id=Mterm.getId();
		Log.e("niwath",""+geturl());
		mMenuDrawer.closeMenu();
		if (Mterm.getMenutype()== 0) { 
			Intent in = new Intent(getApplicationContext(),
					Main_News_Activity.class);
			in.putExtra("menukey", Mterm.getId());
			in.putExtra("seturl", Mterm.getId());
			in.putExtra("settype", 0);
			startActivity(in);
		} else if(Mterm.getMenutype()== 1){
			Intent in=new Intent(getApplicationContext(), PhotoCategoryActivity.class);
			startActivity(in);
		}else {
			reloadList();
		}
	}
}
