package com.mymobkit.model;

import java.io.Serializable;

public final class Resolution implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4649401958252571368L;
	
	private int width;
	private int height;
	
	public Resolution(){
		
	}
	public Resolution(int width, int height){
		this.width = width;
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
}
