package com.imniwath.read.news.amyreader;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imniwath.amy.utility.API;
import com.imniwath.bean.utility.web.web_bean;
import com.imniwath.read.news.bean.news_bean;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

public class News_Detailactivity extends Activity {
	private news_bean items = new news_bean();
	private web_bean data = new web_bean();
	private WebView nt_detail;
	private RequestHandle lastRequest;
	private Button mback;
	private Button mshared;
	private Button mweb;
    private FrameLayout frameload;
    private FrameLayout frameshared;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_details);
		items = (news_bean) this.getIntent().getSerializableExtra("feed");

		mback = (Button) findViewById(R.id.h_menu_bnt_back);
		mshared = (Button) findViewById(R.id.h_menu_shared);
		mweb = (Button) findViewById(R.id.h_menu_web);

		mback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
				finish ();
			}
		});
		mshared.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				shareContent();
			}
		});
		mweb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(News_Detailactivity.this,
						WebViewActivity.class);
				intent.putExtra("url", "http://plan.rta.mi.th/smartarmy");
				startActivity(intent);
			}
		});
		TextView titles = (TextView) findViewById(R.id.text_sub_h);
		frameload= (FrameLayout)findViewById(R.id.fram_detail_loading);
		frameshared= (FrameLayout)findViewById(R.id.fram_detail_shareds);
		//TextView title = (TextView) findViewById(R.id.title);
		//barloading = (ProgressBar) findViewById(R.id.progressBar_detail);
		//final RelativeLayout RLLoading = (RelativeLayout) findViewById(R.id.loadingPanel);
		nt_detail = (WebView) findViewById(R.id.d_detail);
		//pDialog = ProgressDialog.show(this, "กำลังโหลดข้อมูล...", "");
		if (items != null) {
			String summary = items.getTitle();
			if (summary.length() > 30) {
				summary = summary.substring(0, 30);
				summary = summary + "...";
			}
			titles.setText(summary);
			this.reloadList();
			/*
			 * imageLoader.displayImage(items.getImages(), thumb, options, new
			 * ImageLoadingListener() {
			 * 
			 * @Override public void onLoadingStarted(String arg0, View arg1) {
			 * barloading.setProgress(0);
			 * barloading.setVisibility(View.VISIBLE);
			 * RLLoading.setVisibility(View.VISIBLE); Log.e("niwath",
			 * "onLoadingStarted"); }
			 * 
			 * @Override public void onLoadingFailed(String arg0, View arg1,
			 * FailReason arg2) { //barloading.setVisibility(View.GONE);
			 * Log.e("niwath", "onLoadingFailed"); if (lastRequest != null) {
			 * lastRequest.cancel(true); } }
			 * 
			 * @Override public void onLoadingComplete(String arg0, View arg1,
			 * Bitmap arg2) { RLLoading.setVisibility(View.GONE);
			 * barloading.setVisibility(View.GONE); }
			 * 
			 * @Override public void onLoadingCancelled(String arg0, View arg1)
			 * { RLLoading.setVisibility(View.GONE);
			 * barloading.setVisibility(View.GONE); Log.e("niwath",
			 * "onLoadingCancelled"); } });
			 */
		}
	}

	public void onResume() {
		super.onResume();
		//this.reloadList();
	}

	protected void showdata() {
		WebSettings settings = nt_detail.getSettings();
		settings.setDefaultTextEncodingName("utf-8");
		settings.setDefaultFontSize(14);
		settings.setJavaScriptEnabled(true);
		// settings.setPluginsEnabled(true);
		nt_detail.getSettings().setLayoutAlgorithm(
				LayoutAlgorithm.SINGLE_COLUMN);
		// remove a weird white line on the right size
		nt_detail.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		nt_detail.loadDataWithBaseURL(null, data.getContent(), "text/html",
				"UTF-8", "UTF-8");

	}

	protected void reloadList() {
		RequestParams params = new RequestParams();
		params.put("category", "1");
		try {
			lastRequest = API.get("api/news/" + items.getCatergory() + "/"
					+ items.getId() + "", params,
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
											data.setId_news(getroot
													.getInt("id"));
											data.setId_catergory(getroot
													.getInt("catergory"));
											data.setTitle(getroot
													.getString("titles"));
											data.setImages(getroot
													.getString("images"));
											data.setContent(getroot
													.getString("centents"));
											data.setTimes(getroot
													.getString("times"));
											data.setRead(getroot.getInt("read"));
										}
									}	
								} else {
									Toast.makeText(getApplicationContext(),
											"ไม่พบข้อมูล", Toast.LENGTH_SHORT)
											.show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
						}
						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							frameload.setVisibility(View.GONE);
							frameshared.setVisibility(View.VISIBLE);
							//pDialog.dismiss();
							if (lastRequest != null) {
								lastRequest.cancel(true);
							}
						}

						@Override
						public void onFinish() {
							super.onFinish();
							frameload.setVisibility(View.GONE);
							frameshared.setVisibility(View.VISIBLE);
							//pDialog.dismiss();
							showdata();
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

	private void shareContent() {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT,
				items.getTitle() + "\n" + items.getImages());
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent, "Share using"));

	}
}
