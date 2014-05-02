package com.imniwath.read.news.bean;

import java.io.Serializable;

public class photo_bean implements Serializable{
	private static final long serialVersionUID = 1L;
    private int id;
    private String photo;
    private String photosmall;
    private String times;
    public String getPhotosmall() {
		return photosmall;
	}
	public void setPhotosmall(String photosmall) {
		this.photosmall = photosmall;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
    
}
