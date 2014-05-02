package com.imniwath.read.news.bean;

import java.io.Serializable;
public class news_bean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String catergory;
	private String title;
	private String images;
	private String times;
	private int read;
	private int height;

	public int getHeight() {
		return 60;
	}
	public int getWidth(){
		return 200;
	}/*
	public void setHeight(int height) {
		this.height = height;
	}*/

	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCatergory() {
		return catergory;
	}

	public void setCatergory(String catergory) {
		this.catergory = catergory;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
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

	public void setImages(String string) {
		this.images = string;
	}
}
