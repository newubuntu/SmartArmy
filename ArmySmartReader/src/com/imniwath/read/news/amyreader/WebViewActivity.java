package com.imniwath.read.news.amyreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class WebViewActivity extends Activity {
	private WebView webView;
	private Button mback;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                               WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		webView = (WebView) findViewById(R.id.webView);
		webView.setWebViewClient(new MyWebViewClient());
		mback=(Button) findViewById(R.id.h_menu_bnt_back);
		mback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	        	Intent in=new Intent(WebViewActivity.this, News_Detailactivity.class);
				startActivity(in);
			}
		});
		Bundle bundle = this.getIntent().getExtras();
		String url = bundle.getString("url");

		if (null != url) {
			webView.getSettings().setJavaScriptEnabled(true);
			webView.loadUrl(url);
		}
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

}
