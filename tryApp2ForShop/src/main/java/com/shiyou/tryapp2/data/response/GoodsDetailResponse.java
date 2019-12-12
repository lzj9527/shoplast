package com.shiyou.tryapp2.data.response;

import android.extend.data.BaseData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shiyou.tryapp2.data.FileInfo;
import com.shiyou.tryapp2.data.ImageInfo;

public class GoodsDetailResponse extends BaseResponse
{
	public GoodsDetail data;

	public static class GoodsDetail extends BaseData
	{
		@Override
		public String toString() {
			return "id="+id+"  title="+title+"  goodssn="+goodssn+"  tagname="+tagname+"  thumb="+"  thumb_url="+thumb_url+"  count="+count+"  gcate="+gcate;
		}
		public int id;
		public String title;
		public String goodssn;
		public int ccate;
		public String marketprice;
		public String sku;
		public String tagname;// 分类标记
		public String[] thumb_url;
		public ImageInfo[] thumb_url2;// 相册
		public String thumb;// 主图
//		public ImageInfo thumb2;
		public ParamItem[] param;// 商品属性
//		public ErpGoods[] erp;
		public int count;
		public String model_info;
		public FileInfo model_info2;// 模型文件
		public boolean isShop;
		public int issize;// 是否显示手寸
		public int gcate;
		public int customization;	//定制
		public String specialProcess;	//特殊工艺对戒
//		public int eightteenKPrice;
		public Care care;

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

		public String getGoodssn() {
			return goodssn;
		}

		public void setGoodssn(String goodssn) {
			this.goodssn = goodssn;
		}

		public int getCcate() {
			return ccate;
		}

		public void setCcate(int ccate) {
			this.ccate = ccate;
		}

		public String getMarketprice() {
			return marketprice;
		}

		public void setMarketprice(String marketprice) {
			this.marketprice = marketprice;
		}

		public String getSku() {
			return sku;
		}

		public void setSku(String sku) {
			this.sku = sku;
		}

		public String getTagname() {
			return tagname;
		}

		public void setTagname(String tagname) {
			this.tagname = tagname;
		}

		public String[] getThumb_url() {
			return thumb_url;
		}

		public void setThumb_url(String[] thumb_url) {
			this.thumb_url = thumb_url;
		}

		public ImageInfo[] getThumb_url2() {
			return thumb_url2;
		}

		public void setThumb_url2(ImageInfo[] thumb_url2) {
			this.thumb_url2 = thumb_url2;
		}

//		public String getThumb() {
//			return thumb;
//		}
//
//		public void setThumb(String thumb) {
//			this.thumb = thumb;
//		}

		public ParamItem[] getParam() {
			return param;
		}

		public void setParam(ParamItem[] param) {
			this.param = param;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String getModel_info() {
			return model_info;
		}

		public void setModel_info(String model_info) {
			this.model_info = model_info;
		}

		public FileInfo getModel_info2() {
			return model_info2;
		}

		public void setModel_info2(FileInfo model_info2) {
			this.model_info2 = model_info2;
		}

		public boolean isShop() {
			return isShop;
		}

		public void setShop(boolean shop) {
			isShop = shop;
		}

		public int getIssize() {
			return issize;
		}

		public void setIssize(int issize) {
			this.issize = issize;
		}

		public int getGcate() {
			return gcate;
		}

		public void setGcate(int gcate) {
			this.gcate = gcate;
		}

		public int getCustomization() {
			return customization;
		}

		public void setCustomization(int customization) {
			this.customization = customization;
		}

		public String getSpecialProcess() {
			return specialProcess;
		}

		public void setSpecialProcess(String specialProcess) {
			this.specialProcess = specialProcess;
		}

//		public int getEightteenKPrice() {
//			return eightteenKPrice;
//		}
//
//		public void setEightteenKPrice(int eightteenKPrice) {
//			this.eightteenKPrice = eightteenKPrice;
//		}

		public static String toJson(GoodsDetail info)
		{

			GsonBuilder gb = new GsonBuilder();
			Gson gson = gb.create();
			return gson.toJson(info);
		}

		public static GoodsDetail fromJson(String json)
		{
			GsonBuilder gb = new GsonBuilder();
			Gson gson = gb.create();
			return gson.fromJson(json, GoodsDetail.class);
		}


	}
	public static class Thumb extends  BaseData{
		public String url;
		public String height;
		public String width;
		public String filemtime;
	}

	public static class ParamItem extends BaseData
	{
		public String id;
		public String title;
		public String value;
		public String value2;
	}

	public static class ErpGoods extends BaseData
	{
		public String erpid;// 货号
		public String p1;// 金重
		public String p2;// 主石净度
		public String p3;// 主石颜色
		public String p4;// 材质
		public String C0115;// 戒托价格
		public String p5;// 价格
		public String p6;// 主石数量
		public String p7;// 主石重量
		public String p8;// 副石数量
		public String p9;// 副石重量
		public String p128;// 手寸
		public String zs;// 证书
	}
	public static class Care extends  BaseData{
		public int eighteenKPrice;
		public int ptPrice;
	}
}
