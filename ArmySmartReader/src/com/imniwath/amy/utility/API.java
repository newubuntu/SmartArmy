package com.imniwath.amy.utility;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

public class API {
	 //http://plan.rta.mi.th/   http://192.168.1.3/project_one/android/
	protected static final String BASE_URL="http://plan.rta.mi.th/";
	private static AsyncHttpClient client = new AsyncHttpClient();
	private void API() {
		client.setTimeout(30000);
	}
	public static RequestHandle get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		return client.get(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public static RequestHandle get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		return client.get(context, getAbsoluteUrl(url), params, responseHandler);
	}

	public static RequestHandle post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		return client.post(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public static RequestHandle post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		return client.post(context, getAbsoluteUrl(url), params, responseHandler);
	}
	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	public static void cancel(Context context) {
		client.cancelRequests(context, true);
	}
	
}
