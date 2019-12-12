package com.shiyou.tryapp2.data.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.extend.data.BaseDBHelper;
import android.extend.data.BaseData;
import android.extend.util.AndroidUtils;
import android.extend.util.LogUtil;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shiyou.tryapp2.Config;
import com.shiyou.tryapp2.data.response.CoupleRingDetailResponse;
import com.shiyou.tryapp2.data.response.GoodsDetailResponse.GoodsDetail;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BrowseHistoryDBHelper extends BaseDBHelper
{
	public static class HistoryItem extends BaseData
	{
		public String dateString;
		public List<Object> goodsList = new ArrayList<Object>();
	}

	public static class HistoryList extends BaseData
	{
		public List<HistoryItem> list = new ArrayList<HistoryItem>();

		public HistoryItem getHistoryItem(String dateString)
		{
			for (HistoryItem item : list)
			{
				if (item.dateString.equals(dateString))
				{
					return item;
				}
			}
			return null;
		}
	}

	public static final String NAME_ID = "id";
	public static final String NAME_JSON = "json";
	public static final String NAME_TIME = "time";

	private static final String TABLE_NAME = "history";
	private static final int VERSION = Config.HistoryDB_Version;

	private static BrowseHistoryDBHelper mInstance = null;

	public static BrowseHistoryDBHelper getInstance()
	{
		if (mInstance == null)
			mInstance = new BrowseHistoryDBHelper();
		return mInstance;
	}

	private BrowseHistoryDBHelper()
	{
	}

	public synchronized boolean hasRecord(Context context, String id)
	{
		Cursor cursor = null;
		try
		{
			cursor = query(context, NAME_ID + '=' + id, null, null);
			if (cursor.getCount() > 0)
			{
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return false;
	}

	public synchronized long insert(Context context, String id, String json)
	{
		ContentValues values = new ContentValues();
		values.put(NAME_ID, id);
		// values.put(NAME_TYPE, type);
		values.put(NAME_JSON, json);
		values.put(NAME_TIME, System.currentTimeMillis());
		return insert(context, values);
	}

	public synchronized long insert(Context context, GoodsDetail item, boolean isShop)
	{
		item.isShop = isShop;
		return insert(context, String.valueOf(item.id), GoodsDetail.toJson(item));
	}

	public synchronized long insert(Context context, CoupleRingDetailResponse.GoodsDetail item, boolean isShop)
	{
//		item.isShop = isShop;
		return insert(context, String.valueOf(item.id), CoupleRingDetailResponse.GoodsDetail.toJson(item));
	}

	public synchronized int update(Context context, String id, String json)
	{
		ContentValues values = new ContentValues();
		values.put(NAME_JSON, json);
		values.put(NAME_TIME, System.currentTimeMillis());
		return update(context, values, NAME_ID + '=' + id, null);
	}

	public synchronized int update(Context context, GoodsDetail item, boolean isShop)
	{
		item.isShop = isShop;
		return update(context, String.valueOf(item.id), GoodsDetail.toJson(item));
	}

	public synchronized int update(Context context, CoupleRingDetailResponse.GoodsDetail item, boolean isShop)
	{
//		item.isShop = isShop;
		return update(context, String.valueOf(item.id), CoupleRingDetailResponse.GoodsDetail.toJson(item));
	}

	public synchronized long put(Context context, GoodsDetail item, boolean isShop)
	{
 		if (hasRecord(context, String.valueOf(item.id)))
			return update(context, item, isShop);
		else
			return insert(context, item, isShop);
	}

	public synchronized long put(Context context, CoupleRingDetailResponse.GoodsDetail item, boolean isShop)
	{
		if (hasRecord(context,  String.valueOf(item.id)))
			return update(context, item, isShop);
		else
			return insert(context, item, isShop);
	}
	String json2;
	private Object getGoodsDetail(Cursor cursor)
	{

		String json = cursor.getString(cursor.getColumnIndex(NAME_JSON));
		int first=json.indexOf("id");
		int last=json.indexOf("specialProcess");
		int id=Integer.parseInt(json.substring(first+4,last-2));
		Request request=new Request.Builder().url("https://api.i888vip.com/goods/detail?id="+id+"&token="+Config.token).addHeader("accept","application/vnd.zltech.shop.v1+json").get().build();
//        Request request=new Request.Builder().url("http://www.zsmtvip.com/app/index.php?i=2&c=entry&do=goods_detail&m=test&id=2843"+"&key=").addHeader("accept","application/vnd.zsmt.shop.v1+json").get().build();
		OkHttpClient okHttpClient=new OkHttpClient();
		okHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String json =response.body().string();
//						int index=json.lastIndexOf("}");
				Log.d("ProductDetailsFragment", "onResponse: json="+json);
//						json="{\"data\":{\"id\":2780,\"ccate\":12,\"gcate\":1,\"title\":\"\\u7efd\\u653e\",\"thumb_url\":[\"http:\\/\\/images.zsa888.cn\\/zY7aq2HA4qEewz2w423jGHg7h2hDBh.jpg\",\"http:\\/\\/images.zsa888.cn\\/Dqo3kEEf8e8Hf1ejk6jFfH68Zf3eZ9.jpg\",\"http:\\/\\/images.zsa888.cn\\/R8KG8SF5kay8s92gkZ8128DkP18vK9.jpg\",\"http:\\/\\/images.zsa888.cn\\/P75P4HHepKxhUUFZ9XUo9Rux22HE4b.jpg\"],\"model_info\":null,\"ios_model_info\":null,\"sku1\":\"A1-132\",\"sku2\":\"\",\"skus\":\"\",\"mcost\":null,\"wcost\":null,\"mgold\":null,\"wgold\":null,\"goldWeight\":\"2.50\",\"customization\":1,\"tagname\":\"\\u5973\\u6212\",\"care\":{\"eighteenKPrice\":\"2000.00\",\"ptPrice\":\"2300.00\"},\"is_like\":false,\"specialProcess\":0,\"erp\":[]}}";
//				GsonBuilder gb = new GsonBuilder();
//				Gson gson = gb.create();
				// if (response.resultCode == BaseResponse.RESULT_OK && mCacheMap != null)
				// {
				// if (!TextUtils.isEmpty(mCacheKey))
				// {
				// mCacheMap.put(mCacheKey, response);
				// }
				// }
				BrowseHistoryDBHelper.getInstance().json2=json;

			}
		});
		LogUtil.v(TAG, "getGoodsDetail: " + json2);
		int f=json2.indexOf("gcate");
		String gcate=json2.substring(f+7,f+8);
		if (gcate.equals("2"))
			return CoupleRingDetailResponse.GoodsDetail.fromJson(json2);
		else
			return GoodsDetail.fromJson(json2);
	}

	private String getDateString(Cursor cursor)
	{
		long time = cursor.getLong(cursor.getColumnIndex(NAME_TIME));
		CharSequence DATEFORMAT = "yyyy.MM.dd";
		String dateString = DateFormat.format(DATEFORMAT, time).toString();
		LogUtil.v(TAG, "getDateString: " + dateString);
		return dateString;
	}

	public synchronized Object query(Context context, String id)
	{
		Cursor cursor = null;
		try
		{
			cursor = query(context, NAME_ID + '=' + id, null, null);
			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				return getGoodsDetail(cursor);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return null;
	}

	public synchronized List<Object> queryAll(Context context, boolean ascOrder)
	{
		List<Object> list = new ArrayList<Object>();
		Cursor cursor = null;
		try
		{
			String orderBy = NAME_TIME + " " + (ascOrder ? "asc" : "desc");
			cursor = query(context, null, null, orderBy);
			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do
				{
					list.add(getGoodsDetail(cursor));
				}
				while (cursor.moveToNext());
				return list;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return list;
	}

	public synchronized HistoryList getHistoryList(Context context)
	{
		HistoryList list = new HistoryList();
		Cursor cursor = null;
		try
		{
			boolean ascOrder = false;
			String orderBy = NAME_TIME + " " + (ascOrder ? "asc" : "desc");
			cursor = query(context, null, null, orderBy);
			if (cursor.getCount() > 0)
			{
				cursor.moveToFirst();
				do
				{
					Object goods = getGoodsDetail(cursor);
					String dateString = getDateString(cursor);
					HistoryItem item = list.getHistoryItem(dateString);
					if (item != null)
					{
						item.goodsList.add(goods);
					}
					else
					{
						item = new HistoryItem();
						item.dateString = dateString;
						item.goodsList.add(goods);
						list.list.add(item);
					}
				}
				while (cursor.moveToNext());
				return list;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return list;
	}

	public synchronized int delete(Context context, String id)
	{
		return delete(context, NAME_ID + '=' + id, null);
	}

	public synchronized int deleteAll(Context context)
	{
		return delete(context, null, null);
	}

	@Override
	protected String getTableName()
	{
		return TABLE_NAME;
	}

	@Override
	protected int getTableVersion()
	{
		return VERSION;
	}

	@Override
	protected String getSQLCreateContent()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("(_id INTEGER PRIMARY KEY,");
		sb.append(NAME_ID).append(" TEXT").append(',');
		// sb.append(NAME_TYPE).append(" INTEGER").append(',');
		sb.append(NAME_JSON).append(" TEXT").append(',');
		sb.append(NAME_TIME).append(" LONG");
		sb.append(')');
		return sb.toString();
	}
}
