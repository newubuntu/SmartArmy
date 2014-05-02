package com.imniwath.read.news.bean;

public class BlogItem {
	private String mId;
	private String mCaption;
	private String mSmallUrl;
	private String mOriginalUrl;
	
	public String toString() {
		return mId;
	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		mId = id;
	}

	public String getCaption() {
		return mCaption;
	}

	public void setCaption(String caption) {
		mCaption = caption;
	}

	public String getSmallUrl() {
		return mSmallUrl;
	}

	public void setSmallUrl(String smallUrl) {
		mSmallUrl = smallUrl;
	}

	public String getOriginalUrl() {
		return mOriginalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		mOriginalUrl = originalUrl;
	}
	
	
}
