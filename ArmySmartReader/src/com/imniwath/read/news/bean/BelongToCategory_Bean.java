package com.imniwath.read.news.bean;

import java.io.Serializable;

public class BelongToCategory_Bean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String unitname;
	private String logo;
	private String web;
	private String PersonIncharng;
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getWeb() {
		return web;
	}
	public void setWeb(String web) {
		this.web = web;
	}
	public String getPersonIncharng() {
		return PersonIncharng;
	}
	public void setPersonIncharng(String personIncharng) {
		PersonIncharng = personIncharng;
	}

}
