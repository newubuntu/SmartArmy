package com.imniwath.amy.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
	public static final String KEY_PREFS_menu = null;
	private static final String APP_SHARED_PREFS = AppPreferences.class
			.getSimpleName(); 
	
	public static final String KEY_PREFS_content = null;
	private static final String APP_SHARED_content ="content"; 
	
	private static SharedPreferences sharedPrefs_menu;
	private Editor prefsEditor_menu;

	private static SharedPreferences sharedPrefs_content;
	private Editor prefsEditor_content;
	
	public AppPreferences(Context context) {
		this.sharedPrefs_menu = context.getSharedPreferences(APP_SHARED_PREFS,
				Activity.MODE_PRIVATE);
		this.sharedPrefs_content = context.getSharedPreferences(APP_SHARED_content,
				Activity.MODE_PRIVATE);
		this.prefsEditor_menu = sharedPrefs_menu.edit();
		this.prefsEditor_content=sharedPrefs_content.edit();
	}

	public static String getmenu_pre() {
		return sharedPrefs_menu.getString(KEY_PREFS_menu,null);
	}

	public void savemenu_pre(String text) {
		prefsEditor_menu.putString(KEY_PREFS_menu, text);
		prefsEditor_menu.commit();
	}
//  content
	public static String getcontent_pre() {
		return sharedPrefs_content.getString(KEY_PREFS_content, null);
	}
	public void savecontent_pre(String text) {
		prefsEditor_content.putString(KEY_PREFS_content, text);
		prefsEditor_content.commit();
	}
	
	
}
