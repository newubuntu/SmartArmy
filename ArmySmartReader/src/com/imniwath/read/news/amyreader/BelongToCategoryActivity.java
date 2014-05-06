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
import com.imniwath.read.news.adapter.BelongToCategoryadapter;
import com.imniwath.read.news.adapter.photo_catadapter;
import com.imniwath.read.news.bean.BelongToCategory_Bean;
import com.imniwath.read.news.bean.photocat_bean;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
public class BelongToCategoryActivity extends BaseActivity implements OnItemClickListener{
	private GridView BelongToGrid;
	private String data_in;
	private int mPhotoSize, mPhotoSpacing;
	private BelongToCategoryadapter Belong_to_adapter;
	private ArrayList<BelongToCategory_Bean> itemsobj = new ArrayList<BelongToCategory_Bean>();
	private BelongToCategory_Bean new_Belong_items = new BelongToCategory_Bean();
	private String pathurl = null;
	private int pages = 0;
	private Button bntback;
	private String jmenu;
	private RequestHandle lastRequest;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_cat_layout);
		mPhotoSize = getResources().getDimensionPixelSize(R.dimen.photo_size);
		mPhotoSpacing = getResources().getDimensionPixelSize(
				R.dimen.photo_spacing);
		bntback=(Button) findViewById(R.id.h_menu_bnt_back);
		bntback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			 Intent in=new Intent(getApplicationContext(), BelongGroupActivity.class);
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
		
		Belong_to_adapter = new BelongToCategoryadapter(getApplicationContext());
		BelongToGrid = (GridView) findViewById(R.id.albumGrid);
		BelongToGrid.setHovered(true);
		// set image adapter to the GridView
		BelongToGrid.setAdapter(Belong_to_adapter);
		BelongToGrid.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (Belong_to_adapter.getNumColumns() == 0) {
							final int numColumns = (int) Math.floor(BelongToGrid
									.getWidth() / (mPhotoSize + mPhotoSpacing));
							if (numColumns > 0) {
								final int columnWidth = (BelongToGrid.getWidth() / numColumns)
										- mPhotoSpacing;
								Belong_to_adapter.setNumColumns(numColumns);
								Belong_to_adapter.setItemHeight(columnWidth);

							}
						}
					}
				});
		BelongToGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				itemsobj = (ArrayList<BelongToCategory_Bean>) Belong_to_adapter.getItemobj();
				Log.e("debug send", itemsobj.get(pos).getWeb());
				new_Belong_items = itemsobj.get(pos);
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),WebViewBelongActivity.class);
			//	intent.putExtra("photoalbum", new_Belong_items);
				intent.putExtra("url", itemsobj.get(pos).getWeb());
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
			lastRequest = API.get("api/category.json", param,
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
										Belong_to_adapter.clearItemm();
										for (int i = 0; i < loop; i++) {
											JSONObject getroot = (JSONObject) jdata
													.getJSONArray("datacontent")
													.get(i);
											BelongToCategory_Bean bean = new BelongToCategory_Bean();
											bean.setUnitname(getroot.isNull("unitname")? "ไม่พบข้อมูล":getroot.getString("unitname"));			
											bean.setLogo(getroot.isNull("logo") ? "":getroot.getString("logo"));
											bean.setWeb(getroot.isNull("web")?"":getroot.getString("web"));
										    bean.setPersonIncharng(getroot.isNull("PersonIncharng")?"ไม่พบข้อมูล":getroot.getString("PersonIncharng"));
											Belong_to_adapter.additem(bean);
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
