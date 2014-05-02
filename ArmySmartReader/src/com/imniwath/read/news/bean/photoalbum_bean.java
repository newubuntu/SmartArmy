package com.imniwath.read.news.bean;

public class photoalbum_bean extends photocat_bean{
	private static final long serialVersionUID = 1L;
     private int reads;
     private String times;
 	private int id_cat;
 	public int getId_cat() {
 		return id_cat;
 	}
 	public void setId_cat(int id_cat) {
 		this.id_cat = id_cat;
 	} 
	public int getReads() {
		return reads;
	}
	public void setReads(int reads) {
		this.reads = reads;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
}
