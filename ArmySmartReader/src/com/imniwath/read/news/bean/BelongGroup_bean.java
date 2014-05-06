package com.imniwath.read.news.bean;

import java.io.Serializable;

public class BelongGroup_bean implements Serializable{
	private static final long serialVersionUID = 1L;
	private int groupid;
    private String groupname;
    private String logogroup;
	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getLogogroup() {
		return logogroup;
	}
	public void setLogogroup(String logogroup) {
		this.logogroup = logogroup;
	}
    
}
