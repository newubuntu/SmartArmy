package com.imniwath.read.news.amyreader;

import java.util.ArrayList;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.imniwath.amy.utility.config.Config;
import com.imniwath.amy.utility.file_util;
import com.imniwath.read.news.adapter.Menu_adapter;
import com.imniwath.read.news.bean.menu_bean;
import com.loopj.android.http.RequestHandle;

public class BaseActivity extends Activity {
	public MenuDrawer mMenuDrawer;	
	public RequestHandle lastRequest;
	public ListView lv;
	public Menu_adapter Madapter;
    public ArrayList<menu_bean> Mdata=new ArrayList<menu_bean>();	
    public menu_bean Mterm;
    public file_util filutil;
    public menu_bean bean;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		filutil = new file_util(getApplicationContext());
		setup_apadter();
		setUpMenu();
		setUpMainView();
		initNavView();
		setUpSlidingItems();
		setupmenu(filutil.ReadFeed(Config.f_feeds_menu));
		clickAction();
	}
	protected void setup_apadter() {
	Madapter=new Menu_adapter(getApplicationContext());
	}
	@Override
	protected void onStop() {
		super.onStop();
		if (lastRequest != null) {
			lastRequest.cancel(true);
			}
		Madapter.stopimageloader();
	}
	void setUpMenu(){
		mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND,Position.START, MenuDrawer.MENU_DRAG_CONTENT);
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		mMenuDrawer.setDrawerIndicatorEnabled(true);
		mMenuDrawer.setOffsetMenuEnabled(false);
        mMenuDrawer.setDropShadow(R.color.transparent);
	}
	void initNavView(){
		lv=(ListView)findViewById(R.id.navList);
	}
	void setUpSlidingItems(){
		if(Madapter.isnulldata()){
			 lv.setAdapter(null);	
		}else{
			lv.setAdapter(Madapter);	
		}
	}
	public void enableMenu(){
		mMenuDrawer.setEnabled(true);
	}
	public void disableMenu(){
		mMenuDrawer.setEnabled(false);
	}
	void setUpMainView(){
		mMenuDrawer.setMenuView(R.layout.sliding_layout);
	  
	}
	
	@Override
    public void setContentView(int layoutResID) {
        // This override is only needed when using MENU_DRAG_CONTENT.
		mMenuDrawer.setContentView(layoutResID);
		onContentChanged();
    }
	
	@Override
    public void onBackPressed() {
        final int drawerState = mMenuDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mMenuDrawer.closeMenu();
            return;
        }
        super.onBackPressed();
    }
	
	
	void clickAction(){
		 lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				menuClickAction(pos);
			}
		 });
	}
	void menuClickAction(int pos){
		Mdata=Madapter.getItemobj();
		Mterm=Mdata.get(pos);
		int id=Mterm.getId();
		Log.e("niwath","pos"+pos+"id"+id);
		mMenuDrawer.closeMenu();
	}
	private void setupmenu(String s) {
		JSONObject jdata;
		try {
			jdata = new JSONObject(s);
			if (!jdata.getBoolean("error")) {
				JSONArray jrootone = jdata.getJSONArray("datacontent");
				int loop = jrootone.length();
				if (jrootone != null) {
					Madapter.clearItemm();
					bean=new menu_bean();
					bean.setId(123456789);
					bean.setName("อ่านข่าวทั้งหมด");
					bean.setIcon(null);
					bean.setMenutype(0);
                    Madapter.additem(bean);   	
					for (int i = 0; i < loop; i++) {
						JSONObject getroot = (JSONObject) jdata.getJSONArray(
								"datacontent").get(i);
					bean=new menu_bean();
					bean.setId(getroot.getInt("id"));
					bean.setName(getroot.getString("name"));
					bean.setIcon(getroot.isNull("icon") ? null: getroot.getString("icon"));
					bean.setMenutype(getroot.getInt("menutype"));
                    Madapter.additem(bean);   
					}
				}
			} else {
				Toast.makeText(getApplicationContext(), "ไม่พบข้อมูล",
						Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	
	}
}
