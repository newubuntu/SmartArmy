package com.imniwath.read.news.bean;

import java.io.Serializable;

public class photocat_bean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;
	private String images;
//	private int id_cat;
	/*
	public int getId_cat() {
		return id_cat;
	}
	public void setId_cat(int id_cat) {
		this.id_cat = id_cat;
	} */
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
}
