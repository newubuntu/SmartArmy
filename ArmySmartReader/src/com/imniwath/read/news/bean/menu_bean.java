package com.imniwath.read.news.bean;

import java.io.Serializable;
@SuppressWarnings("serial")
public class menu_bean implements Serializable {
	private int id;
	private String name;
	private String icon;
	private int menutype;
	public int getMenutype() {
		return menutype;
	}
	public void setMenutype(int menutype) {
		this.menutype = menutype;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
}
