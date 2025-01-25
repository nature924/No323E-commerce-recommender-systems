package com.entity;

import com.util.VeDate;

public class Details {
	private String detailsid = "D" + VeDate.getStringId();// 生成主键编号
	private String ordercode;// 订单号
	private String goodsid;// 商品
	private String price;// 单价
	private String num;// 数量
	private String goodsname;// 映射数据

	public String getDetailsid() {
		return detailsid;
	}

	public void setDetailsid(String detailsid) {
		this.detailsid = detailsid;
	}

	public String getOrdercode() {
		return this.ordercode;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}

	public String getGoodsid() {
		return this.goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public String getPrice() {
		return this.price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getNum() {
		return this.num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getGoodsname() {
		return this.goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	// 重载方法 生成JSON类型字符串
	@Override
	public String toString() {
		return "Details [detailsid=" + this.detailsid + ", ordercode=" + this.ordercode + ", goodsid=" + this.goodsid
				+ ", price=" + this.price + ", num=" + this.num + ", goodsname=" + this.goodsname + "]";
	}

}
