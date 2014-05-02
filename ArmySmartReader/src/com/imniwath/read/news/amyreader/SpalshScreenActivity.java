package com.imniwath.read.news.amyreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.imniwath.amy.utility.API;
import com.imniwath.amy.utility.AppPreferences;
import com.imniwath.amy.utility.Connection_net;
import com.imniwath.amy.utility.CountDown;
import com.imniwath.amy.utility.SessionManager;
import com.imniwath.amy.utility.config.Config;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

public class SpalshScreenActivity extends Activity {
	private CountDown countdown;
	private String jdata_in = null;
	private String jmenu_in = null;
	private boolean status=false;
	int count = 0;
	private String times;
	private Calendar calendar;
	private SessionManager session;
	private AppPreferences apppreferences;
	private Connection_net connet;
	private RequestHandle lastRequest;
	private String armyfeeds; 
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                               WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splashscreen);

		apppreferences = new AppPreferences(getApplicationContext());
		connet = new Connection_net(getApplicationContext());
		// countdown=new CountDown(4000,4000,this,MainActivity.class);
		// YourNextActivity
		// countdown.start();
		Date date = new Date();
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		// int minutes = calendar.get(Calendar.MINUTE);
		// int seconds = calendar.get(Calendar.SECOND);
		int MILLISECOND = calendar.get(Calendar.MILLISECOND);
		//Log.e("niwath", "MILLISECOND 1" + MILLISECOND);
		StartAnimations();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();	 
		//saveshar();
		if (lastRequest != null) {
			lastRequest.cancel(true);
		}
	}
	@Override
	protected void onStop() {
	 super.onStop();
		if (lastRequest != null) {
			lastRequest.cancel(true);
		}
	}
	public void onResume() {
		super.onResume();
		if (!connet.isConnectingToInternet()) {
			if (!isemtrydata()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(
						"Unable to reach server, \nPlease check your Network.")
						.setTitle("Smart Army")
						.setCancelable(false)
						.setPositiveButton("Exit",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										finish();
									}
								});

				AlertDialog alert = builder.create();
				alert.show();
			} else {
				Toast.makeText(getApplicationContext(),
						"ขออภัยค่ะ ไม่สามารถเชื่อต่อ  Network ได้",
						Toast.LENGTH_SHORT).show();
			    	logicaction();
			}
		} else {
			this.loadmenu();
		}
	}

	private void StartAnimations() {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
		anim.reset();
		LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
		l.clearAnimation();
		l.startAnimation(anim);
		anim = AnimationUtils.loadAnimation(this, R.anim.translate);
		anim.reset();
		ImageView iv = (ImageView) findViewById(R.id.logo);
		iv.clearAnimation();
		iv.startAnimation(anim);
	}

	protected void loadmenu() {
		RequestParams params = new RequestParams();
		params.put("niwath", "ID");
		try {
			lastRequest = API.get("api/getmenu", params,
					new JsonHttpResponseHandler() {
						@Override
						public void onStart() {
							super.onStart();
						}
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								String responseBody) {
							JSONObject jdata;
							try {
								jdata = new JSONObject(responseBody);
								if (!jdata.getBoolean("error")) {
									Setstatus(true);
									WriteFeed(responseBody, Config.f_feeds_menu);
									//Setmenu_in(responseBody);
									reloadList();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onProgress(int bytesWritten, int totalSize) {
							Log.e("niwath",
									"Progress"
											+ (int) Math
													.ceil((bytesWritten / totalSize) * 10000));
							Log.v("niwath",
									String.format(
											"Progress 1 %d from %d (%d%%)",
											bytesWritten,
											totalSize,
											(totalSize > 0) ? (bytesWritten / totalSize) * 10
													: -1));
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							Setstatus(false);
							showmsg("ไม่สมารถเชื่อมต่อระบบได้ค่ะ");
							if (lastRequest != null) {
								lastRequest.cancel(true);
							}
							//finish();
							logicaction();					
						}

						@Override
						public void onFinish() {
							super.onFinish();
						}
					});
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"ERROR" + e.getMessage() + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
	}

	protected void reloadList() {
		RequestParams params = new RequestParams();
		params.put("niwath", "ID");
		try {
			lastRequest = API.get("api/newsall/0", params,
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
								if(!jdata.getBoolean("error")){
								 Setstatus(true);	
								 WriteFeed(responseBody,Config.f_feeds_content);
								// Setdata_in(responseBody);
								 
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
											(totalSize > 0) ? (bytesWritten / totalSize) * 100
													: -1));
						};

						@Override
						public void onFailure(int statusCode, Header[] headers,
								String responseString, Throwable throwable) {
							if (lastRequest != null) {
								Log.e("niwath", "cancel Request");
								lastRequest.cancel(true);
							}
								logicaction();	
						}

						@Override
						public void onFinish() {
							super.onFinish();
							logicaction();
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
			// Log.e("niwath", "j data="+jdata_in);
			// countdown=new
			// CountDown(4000,4000,this,MainActivity.class,getdata_in());
			// countdown.start();
		}
	}
	/*
	private void saveshar(){
		if (isdata()) {
		apppreferences.savemenu_pre(getmenu_in());
		apppreferences.savecontent_pre(getdata_in());	
		}

	}
	public Boolean isdata(){
		  if(getdata_in() != null && getmenu_in()!=null){
	         return true;
	        }
	        return false;
	}
	public Boolean isdatashares(){
		  if(apppreferences.getcontent_pre() != null && apppreferences.getmenu_pre()!=null){
	         return true;
	        }
	        return false;
	}*/
	public boolean isemtrydata(){
		File feedmenu = getBaseContext().getFileStreamPath(Config.f_feeds_menu);
		File feedFile = getBaseContext().getFileStreamPath(Config.f_feeds_content);
		if(feedFile.exists() && feedmenu.exists()){
		  return true;	
		}else{
		return false;	
		}
		
	}
    private void logicaction(){
    	 if(getstatus()){
    	  Log.e("niwath", "getstatus"+getstatus()); 
    	  showtimeout_new();
    	 }else if(isemtrydata() &&getstatus()==false){
    		 showtimeout_new();	
    	}else{
    		Toast.makeText(getApplicationContext(), "ไม่พบข้อมูลกรุณาเชื่อมต่ออินเตอร์เน็ตค่ะ !", 10000).show();  	
    	}	 
    }
	private void showtimeout_new() {
		int MILLISECOND = calendar.get(Calendar.MILLISECOND);
			// session = new SessionManager(getApplicationContext());
			 countdown = new CountDown(1000, 1000, this, Main_News_Activity.class,
			 ReadFeed(Config.f_feeds_content), ReadFeed(Config.f_feeds_menu));
			 countdown.start();
	}
	private void showtimeout_old() {
		int MILLISECOND = calendar.get(Calendar.MILLISECOND);
		Log.e("niwath", "MILLISECOND 2" + MILLISECOND);
	    Log.e("niwath", "showtimeout_old =" + apppreferences.getcontent_pre());
			// apppreferences.getpre_content());
			 countdown = new CountDown(1000, 1000, this, Main_News_Activity.class,
			 getdata_in(), getmenu_in());
			countdown.start();
	}
	private void Setdata_in(String responseString) {
		this.jdata_in = responseString;
	}

	private String getdata_in() {
		return this.jdata_in;
	}

	private void Setmenu_in(String responseString) {
		this.jmenu_in = responseString;
	}

	private String getmenu_in() {
		return this.jmenu_in;
	}

	private void Setstatus(boolean b) {
		this.status = b;
	}
	private boolean getstatus() {
		return this.status;
	}
   public void showmsg(String text){
	   Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
   }
	
	private void WriteFeed(String s,String file) {

		FileOutputStream fOut = null;
		ObjectOutputStream osw = null;
		try {
			fOut = openFileOutput(file, MODE_PRIVATE);
			osw = new ObjectOutputStream(fOut);
			osw.writeObject(s);
			osw.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
   private String ReadFeed(String fName){
		FileInputStream fIn = null;
		ObjectInputStream isr = null;
		armyfeeds=null;
		File feedFile = getBaseContext().getFileStreamPath(fName);
		if (!feedFile.exists())
			return null;

		try {
			fIn = openFileInput(fName);
			isr = new ObjectInputStream(fIn);
			armyfeeds = (String) isr.readObject();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				fIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return armyfeeds;
   }
}