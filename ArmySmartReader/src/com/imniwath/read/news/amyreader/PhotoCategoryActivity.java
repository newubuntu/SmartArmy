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
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.imniwath.amy.utility.API;
import com.imniwath.read.news.adapter.photo_catadapter;
import com.imniwath.read.news.bean.photocat_bean;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
public class PhotoCategoryActivity extends BaseActivity implements OnItemClickListener{
	private GridView photoGrid;
	//ActionBar mActionBar;
	private String data_in;
	private int mPhotoSize, mPhotoSpacing;
	private photo_catadapter photo_adapter;
	private ArrayList<photocat_bean> itemsobj = new ArrayList<photocat_bean>();
	private photocat_bean new_photo_items = new photocat_bean();
	private String pathurl = null;
	private int pages = 0;
	private Button bntback;
	//private MenuDrawer mMenuDrawer;
	//private MenuAdapter menuAdapter;
	//private ListView menuList;
	//private List<Object> menuitems = new ArrayList<Object>();
	//private int mActivePosition = -1;
	private String jmenu;
  //  private file_util readfile;
	private RequestHandle lastRequest;
	//private AppPreferences menu;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_cat_layout);
		//menuList = new ListView(this);
		// get the photo size and spacing
		mPhotoSize = getResources().getDimensionPixelSize(R.dimen.photo_size);
		mPhotoSpacing = getResources().getDimensionPixelSize(
				R.dimen.photo_spacing);
		bntback=(Button) findViewById(R.id.h_menu_bnt_back);
		bntback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			 Intent in=new Intent(getApplicationContext(), Main_News_Activity.class);
			 startActivity(in);
			 finish();
			}
		});
		if (getIntent().getExtras() != null) {
			data_in = (String) this.getIntent().getSerializableExtra("feed");
			jmenu = (String) this.getIntent().getSerializableExtra("menu");
		}
		//readfile=new file_util(getApplicationContext());
		
		//setupmenu(readfile.ReadFeed(Config.f_feeds_menu));
		
		//mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND,
		//		Position.START, MenuDrawer.MENU_DRAG_CONTENT);
		//mMenuDrawer.setContentView(R.layout.photocat_);
		//mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		//mMenuDrawer.setSlideDrawable(R.drawable.ic_home);
		//mMenuDrawer.setDrawerIndicatorEnabled(true);

		//menuAdapter = new MenuAdapter(this, menuitems);
		//menuAdapter.setListener(this);
		//menuAdapter.setActivePosition(mActivePosition);
		//menuList.setAdapter(menuAdapter);
		//menuList.setOnItemClickListener(mItemClickListener);

		//mMenuDrawer.setMenuView(menuList);
		//Log.e("niwath", "datain =" + data_in);
		// initialize image adapter
		photo_adapter = new photo_catadapter(getApplicationContext());
		photoGrid = (GridView) findViewById(R.id.albumGrid);
		photoGrid.setHovered(true);
		// set image adapter to the GridView
		photoGrid.setAdapter(photo_adapter);
		photoGrid.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (photo_adapter.getNumColumns() == 0) {
							final int numColumns = (int) Math.floor(photoGrid
									.getWidth() / (mPhotoSize + mPhotoSpacing));
							if (numColumns > 0) {
								final int columnWidth = (photoGrid.getWidth() / numColumns)
										- mPhotoSpacing;
								photo_adapter.setNumColumns(numColumns);
								photo_adapter.setItemHeight(columnWidth);

							}
						}
					}
				});
		photoGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				itemsobj = (ArrayList<photocat_bean>) photo_adapter.getItemobj();
				Log.e("debug send", itemsobj.get(pos).getTitle());
				new_photo_items = itemsobj.get(pos);
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						PhotoAlbumActivity.class);
				intent.putExtra("photoalbum", new_photo_items);
				startActivity(intent);
			}
		});

	}
/*
	private void setupmenu(String s) {
		JSONObject jdata;
		int idimages;
		try {
			jdata = new JSONObject(s);
			if (!jdata.getBoolean("error")) {
				JSONArray jrootone = jdata.getJSONArray("datacontent");
				int loop = jrootone.length();
				if (jrootone != null) {
					int[] Imagemenu = new int[] {
							R.drawable.ic_home };
					menuitems.add(new Item("อ่านข่าวดูทั้งหมด",
							R.drawable.ic_home, 123456789,0));
					for (int i = 0; i < loop; i++) {
						JSONObject getroot = (JSONObject) jdata.getJSONArray(
								"datacontent").get(i);
				
							idimages = Imagemenu[0];
						
						menuitems.add(new Item(getroot.getString("name"),
								idimages, getroot.getInt("id"),getroot.getInt("menutype")));
					}
				}
				Log.e("debugonSuccess 1.1", Integer.toString(jrootone.length()));
			} else {
				Toast.makeText(getApplicationContext(), "ไม่พบข้อมูล",
						Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
		//	mActivePosition = position;
			mMenuDrawer.setActiveView(view, position);
			Log.e("niwath", "id" + menuAdapter.getid(position));
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(),
					Main_News_Activity.class);
			intent.putExtra("menukey", menuAdapter.getid(position));
			startActivity(intent);
			//seturl(menuAdapter.getid(position));
			//reloadList();
			mMenuDrawer.closeMenu();
		}
	};
*/
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
			lastRequest = API.get("api/photocatall/0", param,
					new JsonHttpResponseHandler() {
						@Override
						public void onStart() {
							Log.e("niwath", "onStart 2");
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
										photo_adapter.clearItemm();
										for (int i = 0; i < loop; i++) {
											JSONObject getroot = (JSONObject) jdata
													.getJSONArray("datacontent")
													.get(i);
											photocat_bean bean = new photocat_bean();
											bean.setId(getroot.getInt("id"));
											bean.setTitle(getroot
													.getString("name"));
											bean.setImages(getroot
															.getString("photo"));
											//bean.setId_cat(getroot.getInt("id_cat"));
											photo_adapter.additem(bean);
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
/*
	@Override
	public void onActiveViewChanged(View v) {
		mMenuDrawer.setActiveView(v, mActivePosition);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			mMenuDrawer.toggleMenu();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		final int drawerState = mMenuDrawer.getDrawerState();
		if (drawerState == MenuDrawer.STATE_OPEN
				|| drawerState == MenuDrawer.STATE_OPENING) {
			mMenuDrawer.closeMenu();
			return;
		}

		super.onBackPressed();
	}
*/
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
		//setcaterory(Mterm.getId());
		//seturl(Mterm.getId());
	 	//settype(1);
		Log.e("niwath",""+geturl());
		mMenuDrawer.closeMenu();
		if (Mterm.getMenutype()!= 1) { 
			Toast.makeText(getApplicationContext(), "niwath"+Mterm.getMenutype(), 500).show(); 
			Intent in = new Intent(getApplicationContext(),
					Main_News_Activity.class);
			in.putExtra("menukey", Mterm.getId());
			in.putExtra("seturl", Mterm.getId());
			in.putExtra("settype", 0);
			startActivity(in);
		} else {
			reloadList();
		}
	}
}
