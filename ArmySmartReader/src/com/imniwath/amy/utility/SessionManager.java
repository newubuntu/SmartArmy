package com.imniwath.amy.utility;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	    SharedPreferences pref;
	    Editor editor; 
	    Context _context;  
	    int PRIVATE_MODE = 0;
	    // Sharedpref file name
	    private static final String PREF_NAME = "USER";
	    // All Shared Preferences Keys
	    private static final String IS_LOGIN = "IsLoggedIn";
	    // User name (make variable public to access from outside)
	    public static final String KEY_NAME = "name";
	    public static final String KEY_EMAIL = "email";
	    private Class login_cls;
	    private Class logout_cls;
	    // Constructor
	    public SessionManager(Context context){
	        this._context = context;
	        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
	        editor = pref.edit();
	    }
	    public void createLoginSession(String name, String email){
	        // Storing login value as TRUE
	        editor.putBoolean(IS_LOGIN, true);      
	        // Storing name in pref
	        editor.putString(KEY_NAME, name);     
	        // Storing email in pref
	        editor.putString(KEY_EMAIL, email);
	        // commit changes
	        editor.commit();
	    }   
	    public void checkLogin(){
	        // Check login status
	        if(!this.isLoggedIn()){
	            Intent i = new Intent(_context, login_cls);
	            // Closing all the Activities
	            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            // Add new Flag to start new Activity
	            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	            // Staring Login Activity
	            _context.startActivity(i);
	        }
	         
	    }
	    public HashMap<String, String> getUserDetails(){
	        HashMap<String, String> user = new HashMap<String, String>();
	        // user name
	        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
	        // user email id
	        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
	        // return user
	        return user;
	    } 
	    /**
	     * Clear session details
	     * */
	    public void logoutUser(){
	        // Clearing all data from Shared Preferences
	        editor.clear();
	        editor.commit(); 
	        // After logout redirect user to Loing Activity
	        Intent i = new Intent(_context, login_cls);
	        // Closing all the Activities
	        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	        // Add new Flag to start new Activity
	        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        // Staring Login Activity
	        _context.startActivity(i);
	    }
	    // Get Login State
	    public boolean isLoggedIn(){
	        return pref.getBoolean(IS_LOGIN, false);
	    }
   
}
