package com.imniwath.bean.utility.web;

public class web_bean {
	private int id_news;
	private int id_catergory;
	private String content;
	private String title;
	private String images;
	private String times;
	private int read;
	public int getId_news() {
		return id_news;
	}
	public void setId_news(int id_news) {
		this.id_news = id_news;
	}
	public int getId_catergory() {
		return id_catergory;
	}
	public void setId_catergory(int id_catergory) {
		this.id_catergory = id_catergory;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
	}
}
