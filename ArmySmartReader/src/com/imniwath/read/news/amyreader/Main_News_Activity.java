package com.imniwath.read.news.amyreader;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;
import com.imniwath.amy.utility.API;
import com.imniwath.amy.utility.config.Config;
import com.imniwath.read.news.adapter.adapterNews;
import com.imniwath.read.news.bean.news_bean;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

public class Main_News_Activity extends BaseActivity implements IXListViewListener, android.widget.AdapterView.OnItemClickListener{
	private XListView mAdapterView = null;
	private adapterNews mAdapter = null;
	private int currentPage = 0;
	private String pathurl="newsall/0";
	private RequestHandle lastRequest;
	private static int pages = 0;
	private String data_in = null;
	private String jmenu = null;
	private String armyfeeds = null;
	private news_bean newitems;
	private int types;
	private int caterory = 123456789;
	private Button bntreflash;
	private Button bntopen_menu;
	private Button bnt_logout;
	private TextView text_sub_h;
	/**
	 * 
	 * @param pageindex
	 * @param type
	 *            1.เน€เธฅเธทเน�เธญเธ�เธ�เธถเน�เธ� 2.เน€เธฅเธทเน�เธญเธ�เธฅเธ�
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {   
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_news_layout);
		bnt_logout=(Button) findViewById(R.id.h_menu_logout);
		bntopen_menu=(Button) findViewById(R.id.h_menu_bnt_back);
		bntreflash=(Button) findViewById(R.id.h_menu_shared);
		text_sub_h=(TextView) findViewById(R.id.text_sub_h);
	    bntopen_menu.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if(mMenuDrawer.isActivated()){
				 mMenuDrawer.closeMenu();
				}else{
				 mMenuDrawer.openMenu(); 	
				}
			}
		});
		bntreflash.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reloadList();
			}
		});
		bnt_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Logout();
			  }
			});
		
		mAdapterView = (XListView) findViewById(R.id.list);
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setXListViewListener(this);
		mAdapter = new adapterNews(this, mAdapterView);
		mAdapterView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				newitems = (news_bean) mAdapter.getItem(position - 1);
				Bundle bundle = new Bundle();
				bundle.putSerializable("feed", newitems);
				Intent intent = new Intent(Main_News_Activity.this,
						News_Detailactivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				// kill this activity
				// finish();
			}
		});
	}
    public void Logout(){
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
        .setMessage("คุณต้องการออกจากระบบ ?")
        .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("ยกเลิก", null).show();	
    }
	@Override
    public void onBackPressed() {
		Logout();
    } 
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mAdapterView.stopRefresh();
	//	mAdapter.mImageFetcher.setExitTasksEarly(true);
		if (lastRequest != null) {
			lastRequest.cancel(true);
		}
		mAdapter.stopimageloader();
	}
	@Override
	protected void onResume() {
		super.onResume();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
		String formattedDate = dateFormat.format(new Date()).toString();
		mAdapterView.setRefreshTime(formattedDate);
	//	mAdapter.mImageFetcher.setExitTasksEarly(false);
		mAdapterView.setAdapter(mAdapter);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
		    if (extras.containsKey("menukey")){
		    	setcaterory(this.getIntent().getIntExtra("menukey", 123456789));
		        if (extras.containsKey("seturl")) {  
		    	seturl(this.getIntent().getIntExtra("seturl", 123456789));
			     if (extras.containsKey("settype")) {
			    	 settype(this.getIntent().getIntExtra("settype", 0)); 
					reloadList();
			     }
		       }
		    }else{  
				old_loadList(filutil.ReadFeed(Config.f_feeds_content));
		    }
		}else{
			old_loadList(filutil.ReadFeed(Config.f_feeds_content));
		}
	}

	@Override
	public void onRefresh() {
		seturl(getcaterory());
		settype(1);
		setpages(0);
		reloadList();// currentPage = 0, 1
	}
	@Override
	public void onLoadMore() {
		++currentPage;
		setpages(currentPage);
		seturl(getcaterory());
		settype(2);
		reloadList();// currentPage, 2
	}
	// int pageindex, final int type
	protected void reloadList() {
		Log.e("niwath", "new load"+geturl());
		RequestParams param = new RequestParams();
		param.put("category", "1");
		param.put("num", "0");
		try {
			lastRequest = API.get("api/" + geturl() + "", param,
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
										if (gettype() != 2) {
											mAdapter.clearItemm();
										}
										for (int i = 0; i < loop; i++) {
											JSONObject getroot = (JSONObject) jdata
													.getJSONArray("datacontent")
													.get(i);
											news_bean bean = new news_bean();
											bean.setId(getroot.getInt("id"));
											bean.setCatergory(getroot.getString("catergory"));
											bean.setTimes(getroot.getString("times"));
											bean.setRead(getroot.getInt("read"));
											bean.setImages(getroot.isNull("images") ? "" : getroot
													.getString("images"));
											bean.setTitle(getroot.isNull("titles") ? "" : getroot
													.getString("titles"));
											//bean.setHeight(240);
											mAdapter.addItemLast(bean);
										}
										if (gettype() == 2) {
											mAdapterView.stopLoadMore();
										} else {
											mAdapterView.stopRefresh();
										}
										filutil.WriteFeed(responseBody,
												Config.f_feeds_content);
									}
								} else {
									toastshow("ข้อมูลไม่ถูกต้อง !");
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
							Toast.makeText(getApplicationContext(),
									"ไม่พบข้อมูล !", 1200).show();
							if (gettype() == 2) {
								mAdapterView.stopLoadMore();
							} else {
								mAdapterView.stopRefresh();
							}
							if (lastRequest != null) {
								lastRequest.cancel(true);
							}
						}

						@Override
						public void onFinish() {
							super.onFinish();
							Log.e("niwath", "onFinish 2");
							if (lastRequest != null) {
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
	protected void old_loadList(String json) {
		try {
			JSONObject jdata = new JSONObject(json);
			if (!jdata.getBoolean("error")) {
				JSONArray jrootone = jdata.getJSONArray("datacontent");
				int loop = jrootone.length();
				if (jrootone != null) {
					mAdapter.clearItemm();
					for (int i = 0; i < loop; i++) {
						JSONObject getroot = (JSONObject) jdata.getJSONArray(
								"datacontent").get(i);
						news_bean bean = new news_bean();
						bean.setId(getroot.getInt("id"));
						bean.setCatergory(getroot.getString("catergory"));
						bean.setTimes(getroot.getString("times"));
						bean.setRead(getroot.getInt("read"));
						bean.setImages(getroot.isNull("images") ? "" : getroot
								.getString("images"));
						bean.setTitle(getroot.isNull("titles") ? "" : getroot
								.getString("titles"));
						//bean.setHeight(240);
						mAdapter.addItemLast(bean);
					}
				}
			} else {
				toastshow("ข้อมูลไม่ถูกต้อง !");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String geturl() {
		return this.pathurl;
	}

	public void seturl(int i) {
		if (i == 123456789) {
			this.pathurl = "newsall/" + getpages();
		} else if (i != -1) {
			this.pathurl = "newsbycat/" + getcaterory() + "/" + getpages();
		} else {
			this.pathurl = "newsall/" + getpages();
		}
	}

	public void setpages(int i) {
		pages = i;
		if (i != -1) {
			this.pages = i;
		} else {
			this.pages = 0;
		}
	}

	public int getpages() {
		return pages;
	}

	public void settype(int i) {
		this.types = i;
	}

	public int gettype() {
		return this.types;
	}

	public void setcaterory(int i) {
		this.caterory = i;
	}

	public int getcaterory() {
		return this.caterory;
	}
	public void toastshow(String msg){
		 Toast.makeText(getApplicationContext(), ""+msg,Toast.LENGTH_SHORT).show();
	}
	public void showsmsg(String msg, String title, String button) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
				.setTitle("Smart Army")
				.setCancelable(false)
				.setPositiveButton("Exit",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								finish();
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
	}
	@Override
	void clickAction() {
		super.clickAction();
		lv.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		menuClickAction(position);
	}
	@Override
	void menuClickAction(int pos) {
		super.menuClickAction(pos);
		Mdata=Madapter.getItemobj();
		Mterm=Mdata.get(pos);
		int id=Mterm.getId();
		setpages(0);
		setcaterory(Mterm.getId());
		seturl(Mterm.getId());
		settype(1);
		mMenuDrawer.closeMenu();
		if (Mterm.getMenutype()== 1) {
			Intent in = new Intent(getApplicationContext(),
					PhotoCategoryActivity.class);
			startActivity(in);
		}else if(Mterm.getMenutype()== 2){
			Intent in = new Intent(getApplicationContext(),
					BelongGroupActivity.class);
			startActivity(in);
		} else {
			String menu=Mterm.getName().isEmpty() ?"":Mterm.getName();
			text_sub_h.setText(menu);
			reloadList();
		}
	}
}// end of class
