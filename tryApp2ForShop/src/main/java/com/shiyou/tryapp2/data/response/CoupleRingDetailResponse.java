package com.shiyou.tryapp2.data.response;

import android.extend.data.BaseData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shiyou.tryapp2.data.FileInfo;
import com.shiyou.tryapp2.data.ImageInfo;
import com.shiyou.tryapp2.data.response.GoodsDetailResponse.ErpGoods;
import com.shiyou.tryapp2.data.response.GoodsDetailResponse.ParamItem;

public class CoupleRingDetailResponse extends BaseResponse
{
	public GoodsDetail data;

	public static class GoodsDetail extends BaseData
	{
		public int id;
		public int ccate;
		public String title;
//		public String goodssn;
//		public String sku;
//		public String m_sku;
//		public String w_sku;
//		public String marketprice;
		public String tagname;// 分类标记
		public int gcate;
		public int  customization;	//定制
		public int specialProcess;	//特殊工艺对戒
		public String thumb;// 主图
 		public String[] thumb_url;
		public ImageInfo[] thumb_url2;// 相册
//		public ParamItem[] param;// 商品属性
//		public ErpDetail erp;
		public int count;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getCcate() {
			return ccate;
		}

		public void setCcate(int ccate) {
			this.ccate = ccate;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getTagname() {
			return tagname;
		}

		public void setTagname(String tagname) {
			this.tagname = tagname;
		}

		public int getGcate() {
			return gcate;
		}

		public void setGcate(int gcate) {
			this.gcate = gcate;
		}



//		public String getThumb() {
//			return thumb;
//		}
//
//		public void setThumb(String thumb) {
//			this.thumb = thumb;
//		}

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

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}



		public String[] model_infos;
		public ModelInfos model_infos2;// 对戒模型文件
//		public boolean isShop;

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

	public static class ErpDetail extends BaseData
	{
		public ErpGoods[] men;// 男戒
		public ErpGoods[] wmen;// 女戒
	}

	public static class ModelInfos extends BaseData
	{
		public String androidMan;
		public String androidWoman;
		public FileInfo men;
		public FileInfo wmen;
	}
}
