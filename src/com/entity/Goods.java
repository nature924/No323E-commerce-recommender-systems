package com.entity;

import com.util.VeDate;

public class Goods {
	private String goodsid = "G" + VeDate.getStringId();// 生成主键编号
	private String goodsname;// 商品名称
	private String image;// 图片
	private String cateid;// 商品类型
	private String price;// 销售价格
	private String recommend;// 是否推荐
	private String special;// 是否特价
	private String storage;// 库存数量
	private String addtime;// 发布日期
	private String hits;// 点击数
	private String sellnum;// 销售数量
	private String contents;// 商品详情
	private String catename;// 映射数据

	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public String getGoodsname() {
		return this.goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCateid() {
		return this.cateid;
	}

	public void setCateid(String cateid) {
		this.cateid = cateid;
	}

	public String getPrice() {
		return this.price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRecommend() {
		return this.recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getSpecial() {
		return this.special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public String getStorage() {
		return this.storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getAddtime() {
		return this.addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getHits() {
		return this.hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}

	public String getSellnum() {
		return this.sellnum;
	}

	public void setSellnum(String sellnum) {
		this.sellnum = sellnum;
	}

	public String getContents() {
		return this.contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getCatename() {
		return this.catename;
	}

	public void setCatename(String catename) {
		this.catename = catename;
	}

	// 重载方法 生成JSON类型字符串
	@Override
	public String toString() {
		return "Goods [goodsid=" + this.goodsid + ", goodsname=" + this.goodsname + ", image=" + this.image
				+ ", cateid=" + this.cateid + ", price=" + this.price + ", recommend=" + this.recommend + ", special="
				+ this.special + ", storage=" + this.storage + ", addtime=" + this.addtime + ", hits=" + this.hits
				+ ", sellnum=" + this.sellnum + ", contents=" + this.contents + ", catename=" + this.catename + "]";
	}

}
