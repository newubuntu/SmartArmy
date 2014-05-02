package com.imniwath.amy.utility;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

public class CountDown extends CountDownTimer  {
	private Activity _act;
	private Class _cls;
	private String data;
	private String menu;
	public CountDown(long millisInFuture, long countDownInterval,Activity act,Class cls, String data, String menu) {
	super(millisInFuture, countDownInterval);
	_act=act;
	_cls=cls;
	this.data=data;
	this.menu=menu;
	}
	@Override
	public void onFinish() {
		Intent in=new Intent();
		in.putExtra("feed", this.data);
		in.putExtra("menu", this.menu);
		in.setClass(_act,_cls);
		_act.startActivity(in);
		//_act.startActivity(new Intent(_act,_cls));
		_act.finish();
	}
	@Override
	public void onTick(long millisUntilFinished) {
		Log.e("niwath", "onTick"+millisUntilFinished);
	}
	public String getData() {
		return data;
	}
	public String getMenu() {
		return menu;
	}
}
