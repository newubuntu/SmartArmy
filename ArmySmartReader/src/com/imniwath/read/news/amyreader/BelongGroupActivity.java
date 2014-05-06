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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.imniwath.amy.utility.API;
import com.imniwath.read.news.adapter.BelongGroupadapter;
import com.imniwath.read.news.bean.BelongGroup_bean;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
public class BelongGroupActivity extends BaseActivity implements OnItemClickListener{
	private GridView BelongGroupGrid;
	//ActionBar mActionBar;
	private String data_in;
	private int mPhotoSize, mPhotoSpacing;
	private BelongGroupadapter BelongGroup_adapter;
	private ArrayList<BelongGroup_bean> itemsobj = new ArrayList<BelongGroup_bean>();
	private BelongGroup_bean new_Belong_items = new BelongGroup_bean();
	private String pathurl = null;
	private int pages = 0;
	private Button bntback;
	private String jmenu;
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
		 
		BelongGroup_adapter = new BelongGroupadapter(getApplicationContext());
		BelongGroupGrid = (GridView) findViewById(R.id.albumGrid);
		BelongGroupGrid.setHovered(true);
		// set image adapter to the GridView
		BelongGroupGrid.setAdapter(BelongGroup_adapter);
		BelongGroupGrid.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (BelongGroup_adapter.getNumColumns() == 0) {
							final int numColumns = (int) Math.floor(BelongGroupGrid
									.getWidth() / (mPhotoSize + mPhotoSpacing));
							if (numColumns > 0) {
								final int columnWidth = (BelongGroupGrid.getWidth() / numColumns)
										- mPhotoSpacing;
								BelongGroup_adapter.setNumColumns(numColumns);
								BelongGroup_adapter.setItemHeight(columnWidth);

							}
						}
					}
				});
		BelongGroupGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				itemsobj = (ArrayList<BelongGroup_bean>) BelongGroup_adapter.getItemobj();
				Log.e("debug send", ""+itemsobj.get(pos).getGroupid());
				new_Belong_items = itemsobj.get(pos);	
				if(new_Belong_items.getGroupid()==1){
					
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							BelongToCategoryActivity.class);
					intent.putExtra("send_belong", new_Belong_items);
					startActivity(intent);	
				}else{
			      Toast.makeText(getApplicationContext(),"ไม่พบข้อมูล !",1200).show();		
				}
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
			lastRequest = API.get("api/group.json", param,
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
										BelongGroup_adapter.clearItemm();
										for (int i = 0; i < loop; i++) {
											JSONObject getroot = (JSONObject) jdata
													.getJSONArray("datacontent")
													.get(i);
											BelongGroup_bean bean = new BelongGroup_bean();
											bean.setGroupid(getroot.getInt("groupid"));
											bean.setGroupname(getroot.isNull("groupname")?"":getroot.getString("groupname"));
											bean.setLogogroup(getroot.isNull("logogroup")?"":getroot.getString("logogroup"));
											BelongGroup_adapter.additem(bean);
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
