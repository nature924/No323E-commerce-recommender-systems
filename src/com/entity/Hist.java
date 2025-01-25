package com.entity;

import com.util.VeDate;

public class Hist {
	private String histid = "H" + VeDate.getStringId();
	private String usersid;
	private String goodsid;
	private String num;
	private String username;
	private String goodsname;

	public String getHistid() {
		return histid;
	}

	public void setHistid(String histid) {
		this.histid = histid;
	}

	public String getUsersid() {
		return this.usersid;
	}

	public void setUsersid(String usersid) {
		this.usersid = usersid;
	}

	public String getGoodsid() {
		return this.goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public String getNum() {
		return this.num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGoodsname() {
		return this.goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
}
/**
 * 
 */
