package com.shiyou.tryapp2.app.product;

import android.extend.ErrorInfo;
import android.extend.app.BaseFragment;
import android.extend.loader.BaseParser.DataFrom;
import android.extend.loader.BitmapLoader.DecodeMode;
import android.extend.util.AndroidUtils;
import android.extend.util.LogUtil;
import android.extend.util.ResourceUtil;
import android.extend.util.ViewTools;
import android.extend.util.ViewTools.FitMode;
import android.extend.widget.ExtendImageView;
import android.extend.widget.FragmentContainer;
import android.extend.widget.adapter.AbsAdapterItem;
import android.extend.widget.adapter.BaseAdapter;
import android.extend.widget.adapter.BaseGridAdapter;
import android.extend.widget.adapter.BasePagerAdapter;
import android.extend.widget.adapter.HorizontalScrollListView;
import android.extend.widget.adapter.ScrollGridView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.shiyou.tryapp2.Config;
import com.shiyou.tryapp2.Define;
import com.shiyou.tryapp2.RequestManager;
import com.shiyou.tryapp2.RequestManager.RequestCallback;
import com.shiyou.tryapp2.app.MainActivity;
import com.shiyou.tryapp2.app.MainFragment;
import com.shiyou.tryapp2.app.WebViewFragment;
import com.shiyou.tryapp2.app.login.LoginHelper;
import com.shiyou.tryapp2.data.ImageInfo;
import com.shiyou.tryapp2.data.response.BannerADListResponse;
import com.shiyou.tryapp2.data.response.BaseResponse;
import com.shiyou.tryapp2.data.response.GoodsCategorysResponse;
import com.shiyou.tryapp2.data.response.GoodsCategorysResponse.CategoryItem;
import com.shiyou.tryapp2.data.response.ShopLogoAndADResponse;
import com.shiyou.tryapp2.shop.zsa.R;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OtherIndexFragment extends BaseFragment
{
	private BaseGridAdapter<AbsAdapterItem> mAdapter;
	private Button product_search;
	private TextView product_all_number;
	private EditText product_search_number;
	private FragmentContainer search_number;
	private BasePagerAdapter<AbsAdapterItem> mPagerAdapter;
	private ViewPager mViewPager;
	private LinearLayout mDotContainer;
	private ImageView nvjie;
	private ImageView nanjie;
	private ImageView diaozhui;
	private ImageView shoulian;
	private ImageView kelajie;
	private ImageView duijie;
	private ImageView erding;
	private ImageView taoxi;
	private ExtendImageView background;
	ShopLogoAndADResponse mShopLogoAndADResponse;
	BannerADListResponse mBannerADListResponse;

	HorizontalScrollListView indexProduct;
	BaseAdapter<AbsAdapterItem> indexProductAdapter;
	private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener()
	{
		@Override
		public void onPageSelected(int position)
		{
			setSelectdDot(position);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
		{
		}

		@Override
		public void onPageScrollStateChanged(int state)
		{
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		mLayoutResID = ResourceUtil.getLayoutId(getActivity(), "other_index_layout");
		View view = super.onCreateView(inflater, container, savedInstanceState);
		((android.extend.widget.ExtendLinearLayout)view).setInterceptTouchEventToDownward(true);

		int id = ResourceUtil.getId(getContext(), "index_container");
		ScrollGridView index_container = (ScrollGridView)view.findViewById(id);
		index_container.setNumColumns(3);
		int space = AndroidUtils.dp2px(getContext(), 40);
		index_container.setVerticalDividerWidth(space);
		index_container.setHorizontalDividerHeight(space);


		mAdapter = new BaseGridAdapter<AbsAdapterItem>();
		index_container.setAdapter(mAdapter);
		id=ResourceUtil.getId(getContext(),"product_search");
		product_search= (Button)view.findViewById(id);

		id=ResourceUtil.getId(getContext(),"background");
		background= (ExtendImageView) view.findViewById(id);

		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		// 屏幕宽度（像素）
		int width = metric.widthPixels;
		// 屏幕高度（像素）
		int height = metric.heightPixels;
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width/5,LayoutParams.MATCH_PARENT);
		id=ResourceUtil.getId(getContext(),"nvjie");
		nvjie= (ImageView) view.findViewById(id);
		nvjie.setLayoutParams(params);
		id=ResourceUtil.getId(getContext(),"nanjie");
		nanjie= (ImageView) view.findViewById(id);
        nanjie.setLayoutParams(params);
		id=ResourceUtil.getId(getContext(),"erding");
		erding= (ImageView) view.findViewById(id);
		erding.setLayoutParams(params);
		id=ResourceUtil.getId(getContext(),"shoulian");
		shoulian= (ImageView) view.findViewById(id);
		shoulian.setLayoutParams(params);
		id=ResourceUtil.getId(getContext(),"kelajie");
		kelajie= (ImageView) view.findViewById(id);
		kelajie.setLayoutParams(params);
		id=ResourceUtil.getId(getContext(),"diaozhui");
		diaozhui= (ImageView) view.findViewById(id);
		diaozhui.setLayoutParams(params);
		id=ResourceUtil.getId(getContext(),"taoxi");
		taoxi= (ImageView) view.findViewById(id);
		taoxi.setLayoutParams(params);
		id=ResourceUtil.getId(getContext(),"duijie");
		duijie= (ImageView) view.findViewById(id);
		duijie.setLayoutParams(params);
		shoulian.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainFragment.instance.addFragmentToCurrent(new ProductListFragment(String.valueOf(29), false,"spot",String.valueOf(29),1), false);
			}
		});
		nvjie.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainFragment.instance.addFragmentToCurrent(new ProductListFragment(String.valueOf(12), false,"spot",String.valueOf(12),1), false);
			}
		});
		kelajie.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainFragment.instance.addFragmentToCurrent(new ProductListFragment(String.valueOf(25), false,"spot",String.valueOf(25),1), false);
			}
		});
		duijie.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainFragment.instance.addFragmentToCurrent(new ProductListFragment(String.valueOf(16), false,"spot",String.valueOf(16),1), false);
			}
		});
		erding.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainFragment.instance.addFragmentToCurrent(new ProductListFragment(String.valueOf(19), false,"spot",String.valueOf(19),1), false);
			}
		});
		nanjie.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainFragment.instance.addFragmentToCurrent(new ProductListFragment(String.valueOf(15), false,"spot",String.valueOf(15),1), false);
			}
		});
		diaozhui.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainFragment.instance.addFragmentToCurrent(new ProductListFragment(String.valueOf(21), false,"spot",String.valueOf(29),1), false);
			}
		});
		taoxi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainFragment.instance.addFragmentToCurrent(new ProductListFragment(String.valueOf(29), false,"spot",String.valueOf(34),1), false);
			}
		});

		id= ResourceUtil.getId(getActivity(), "viewpager2");
		mViewPager = (ViewPager)view.findViewById(id);
		mPagerAdapter = new BasePagerAdapter<AbsAdapterItem>();
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.addOnPageChangeListener(mPageChangeListener);
		mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
		{
			@Override
			public void onGlobalLayout()
			{
				int width = mViewPager.getWidth();
				int height = mViewPager.getHeight();
				if (width == 0 || height == 0)
					return;
				LogUtil.v(TAG, "mViewPager size: " + width + "x" + height);
				mViewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				LayoutParams params = mViewPager.getLayoutParams();
				params.width = width;
				params.height = height;
				mViewPager.setLayoutParams(params);
			}
		});
		id=ResourceUtil.getId(getContext(),"product_search_number");
		product_search_number= (EditText)view.findViewById(id);
		id=ResourceUtil.getId(getContext(),"search_number");
		search_number=(FragmentContainer)view.findViewById(id);
		search_number.setVisibility(View.GONE);
		product_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					String goodsId = product_search_number.getText().toString();
//					int id = ResourceUtil.getId(getContext(), "token");
//					TextView textView = (TextView) getView().findViewById(id);
//					MainFragment.instance.addProductDetailFragmentToCurrent(goodsId, Define.TAG_RING, true, true, false, "https://api.zsa888.cn/goods/search?isSearch=true&sku=" +goodsId );
					search_number.setVisibility(View.VISIBLE);
					replace(getActivity(), ResourceUtil.getId(getContext(), "search_number"), new MainWebFragment(Config.BasePrefix+"/goods/search?isSearch=true&goodsType=spot&sku=" +goodsId,0),false);
				}catch (Exception e){
					showToast("输入的款号有误");
				}
				}

		});
		id=ResourceUtil.getId(getContext(),"product_all_number");
		product_all_number=(TextView) view.findViewById(id);
		changeNumber();
		loadGoodsCategory();
		ensureBackground();
		return view;
	}

	private void ensureBackground(){
		Request request=new Request.Builder().url("https://api.i888vip.com/swipers/pics?token="+ Config.token).addHeader("accept","application/vnd.zltech.shop.v1+json").get().build();
		OkHttpClient okHttpClient=new OkHttpClient();
		okHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				showToast("没有背景");
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				final  String json=response.body().string();
				AndroidUtils.MainHandler.post(new Runnable() {
					@Override
					public void run() {
						int start=json.indexOf("spot");
						int end=json.lastIndexOf("quick");
						String jpg=json.substring(start+7,end-3).replace("\\/","/");
						background.setImageDataSource(jpg,
								0, DecodeMode.FIX_XY);
						background.startImageLoad(false);
//						Request request1=new Request.Builder().url(url).build();
//						OkHttpClient okHttpClient1=new OkHttpClient();
//						okHttpClient.newCall(request).enqueue(new Callback() {
//							@Override
//							public void onFailure(Call call, IOException e) {
//
//							}
//
//							@Override
//							public void onResponse(Call call, Response response) throws IOException {
//								byte[] Picture = response.body().bytes();
//								//通过imageview，设置图片
//                                Bitmap bitmap=BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
//								mLogoImageView.setImageBitmap(BitmapFactory.decodeByteArray(Picture, 0, Picture.length));
//							}
//						});
					}
				});
			}
		});
	}

	public void doRefresh()
	{
		AndroidUtils.MainHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				if (isResumed())
				{
					LogUtil.d(TAG, "doRefresh...");
					loadShopLogoAndAD();
					loadGoodsCategorys();
				}
				else
					AndroidUtils.MainHandler.postDelayed(this, 50L);
			}
		});
	}

	private void loadGoodsCategorys()
	{
		RequestManager.loadGoodsCategorys(getActivity(), new RequestCallback()
		{
			@Override
			public void onRequestResult(int requestCode, long taskId, BaseResponse response, DataFrom from)
			{
				if (response.resultCode == BaseResponse.RESULT_OK)
				{
					GoodsCategorysResponse mGoodsCategoryResponse = (GoodsCategorysResponse)response;
					indexProductAdapter.clear();
					indexProductAdapter.addItem(new CategoryAdapterItem(0));
					for (CategoryItem mCategoryItem : mGoodsCategoryResponse.findZuanShiCategoryList())
					{

						indexProductAdapter.addItem(new CategoryAdapterItem(mCategoryItem));
					}
					indexProductAdapter.addItem(new CategoryAdapterItem(1));
				}

				else
				{
					showToast(response.error);
				}
			}

			@Override
			public void onRequestError(int requestCode, long taskId, ErrorInfo error)
			{
				showToast("网络错误: " + error.errorCode);
			}
		});
	}

	private JsonArray jsonArray;
	private ArrayList<ShopLogoAndADResponse> beans;
	private void loadShopLogoAndAD()
	{
//		RequestManager.loadShopLogoAndAD(getContext(), LoginHelper.getUserKey(getContext()), new RequestCallback()
//		{
//			@Override
//			public void onRequestResult(int requestCode, long taskId, BaseResponse response, DataFrom from)
//			{
//				if (response.resultCode == BaseResponse.RESULT_OK)
//				{
//					mShopLogoAndADResponse = (ShopLogoAndADResponse)response;
//				}
//				else
//				{
//					mShopLogoAndADResponse = null;
//					showToast(response.error);
//				}
//				loadBannerADList();
//			}
//
//			@Override
//			public void onRequestError(int requestCode, long taskId, ErrorInfo error)
//			{
//				showToast("网络错误: " + error.errorCode);
//				mShopLogoAndADResponse = null;
//				loadBannerADList();
//			}
//		});

		final Request request=new Request.Builder().url(Config.LoadShopLogoAndADUrl).addHeader("Authorization","Bearer "+Config.token).addHeader("accept","application/vnd.zltech.shop.v1+json").get().build();
		OkHttpClient okHttpClient=new OkHttpClient();
		okHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
//				loadBannerADList();
				ensureAdvertisementPager();
			}


			@Override
			public void onResponse(Call call, Response response) throws IOException {
//					String json=response.body().string().replaceAll("\"","\'");
				String json=response.body().string().replace("\\/","/");
//					String json="[{'id':0,'name':'\\u7528\\u6237\\u5e7f\\u544a','thumb':'http:\\/\\/images.i888vip.com\\/WZ38S4xC9cw9x83rtZRe747eE48U3z.jpg','goodsid':'','displayorder':0},{'id':14,'name':'\\u5341\\u7bad\\u5341\\u5fc3','thumb':'http:\\/\\/images.i888vip.com\\/F17B3KCM2CkF17AUXxIM1UZcamkiA1.jpg','goodsid':'2189','displayorder':9,'link':'https:\\/\\/api.i888vip.com\\/goods\\/detail?id=2189','gcate':1,'customization':0},{'id':7,'name':'\\u53cc\\u9762\\u6234','thumb':'http:\\/\\/images.i888vip.com\\/F94K94t94m9oW45mtt99BKTVT3HhZK.jpg','goodsid':'2699','displayorder':0,'link':'https:\\/\\/api.i888vip.com\\/goods\\/detail?id=2699','gcate':1,'customization':0},{'id':15,'name':'\\u5341\\u4e8c\\u661f\\u5ea7','thumb':'http:\\/\\/images.i888vip.com\\/VoA0kd0SD4sBA07spC2KK0M72k0k20.jpg','goodsid':'2716','displayorder':0,'link':'https:\\/\\/api.i888vip.com\\/goods\\/detail?id=2716','gcate':1,'customization':0}]".replaceAll("\"","\'");
//				if(json.equals(json2)){
//					showToast("nb");
//				}
				GsonBuilder gb = new GsonBuilder();
				Gson gson = gb.create();
//		mShopLogoAndADResponse=gson.fromJson(json,ShopLogoAndADResponse.class);
				JsonParser jsonParser=new JsonParser();
				beans=new ArrayList<>();
				jsonArray=new JsonArray();
				jsonArray = jsonParser.parse(json).getAsJsonArray();
				for (JsonElement user : jsonArray) {
					//使用GSON，直接转成Bean对象
					ShopLogoAndADResponse shopLogoAndADResponse = gson.fromJson(user, ShopLogoAndADResponse.class);
					beans.add(shopLogoAndADResponse);
				}
//				loadBannerADList();
				ensureAdvertisementPager();
			}
		});
	}
	private void ensureAdvertisementPager()
	{
		mPagerAdapter.clear();
		int length = 0;
//		String json="[{ \"id\": 0, \"name\": \"用户广告\", \"thumb\": \"http://images.i888vip.com/WZ38S4xC9cw9x83rtZRe747eE48U3z.jpg\", \"goods_id\": \"\", \"displayorder\": 0 }, { \"id\": 14, \"name\": \"十箭十心\", \"thumb\": \"http://images.i888vip.com/F17B3KCM2CkF17AUXxIM1UZcamkiA1.jpg\", \"goods_id\": \"2191\", \"displayorder\": 9, \"link\": \"https://api.i888vip.com/goods/detail?id=2191\", \"gcate\": 1, \"customization\": 0 },{ \"id\": 7, \"name\": \"双面戴\", \"thumb\": \"http://images.i888vip.com/F94K94t94m9oW45mtt99BKTVT3HhZK.jpg\", \"goods_id\": \"2699\", \"displayorder\": 0, \"link\": \"https://api.i888vip.com/goods/detail?id=2699\", \"gcate\": 1, \"customization\": 0 },{ \"id\": 15, \"name\": \"十二星座\", \"thumb\": \"http://images.i888vip.com/VoA0kd0SD4sBA07spC2KK0M72k0k20.jpg\", \"goods_id\": \"2716\", \"displayorder\": 0, \"link\": \"https://api.i888vip.com/goods/detail?id=2716\", \"gcate\": 1, \"customization\": 0 }]".replaceAll("\"","\'");
//		GsonBuilder gb = new GsonBuilder();
//		Gson gson = gb.create();
////		mShopLogoAndADResponse=gson.fromJson(json,ShopLogoAndADResponse.class);
//		JsonParser jsonParser=new JsonParser();
//		ArrayList<ShopLogoAndADResponse> beans=new ArrayList<>();
//		JsonArray jsonArray = jsonParser.parse(json).getAsJsonArray();
//		for (JsonElement user : jsonArray) {
//			//使用GSON，直接转成Bean对象
//			ShopLogoAndADResponse shopLogoAndADResponse = gson.fromJson(user, ShopLogoAndADResponse.class);
//			beans.add(shopLogoAndADResponse);
//		}
//		if (mShopLogoAndADResponse != null && mShopLogoAndADResponse.data != null
//				 && jsonArray.get(i).thumb != null) {
		for (int i=0;i<jsonArray.size();i++) {
//				if (!TextUtils.isEmpty(beans.get(i).goodsid)) {
//				if (!mShopLogoAndADResponse.data.goodsid.equals("0"))
//					mPagerAdapter.addItem(new AdvertisementPagerAdapterItem(mShopLogoAndADResponse.data.,
//							mShopLogoAndADResponse.data.goodsid, mShopLogoAndADResponse.data.tag, true,mShopLogoAndADResponse.data.link));
//				else

			ImageInfo imageInfo = new ImageInfo();
			imageInfo.url =beans.get(i).thumb;
			if(beans.get(i).goodsid!=null){
				mPagerAdapter.addItem(new OtherIndexFragment.AdvertisementPagerAdapterItem(imageInfo,
						beans.get(i).goodsid, beans.get(i).gcate, beans.get(i).customization,beans.get(i).link));
			}else{
				mPagerAdapter.addItem(new OtherIndexFragment.AdvertisementPagerAdapterItem(imageInfo,
						beans.get(i).goodsid, beans.get(i).gcate, beans.get(i).customization));
			}


			length++;
		}
//		}

//		if (mBannerADListResponse != null && mBannerADListResponse.data!= null
//				 && mBannerADListResponse.data.list.length > 0)
//		{
//			for (BannerADItem item : mBannerADListResponse.data.list)
//			{
//				if (!TextUtils.isEmpty(item.goodsid)) {
////					if (item.shopsee == 1)
//					ImageInfo imageInfo = new ImageInfo();
//					imageInfo.url = mShopLogoAndADResponse.data.thumb;
//					mPagerAdapter.addItem(new AdvertisementPagerAdapterItem(imageInfo,
//							mShopLogoAndADResponse.data.goodsid, mShopLogoAndADResponse.data.gcate, mShopLogoAndADResponse.data.customization));
////					else
////						mPagerAdapter.addItem(new AdvertisementPagerAdapterItem(item.thumb, item.goodsid, item.tag,
////								false));
//				}else {
//					ImageInfo imageInfo = new ImageInfo();
//					imageInfo.url = mShopLogoAndADResponse.data.thumb;
//					mPagerAdapter.addItem(new AdvertisementPagerAdapterItem(imageInfo, item.link));
//				}
//				length++;
//			}
//		}
		ensureDots(length);
	}
	private void ensureDots(final int length)
	{
		// if (mAdvertisementResponse != null && mAdvertisementResponse.datas !=
		// null
		// && mAdvertisementResponse.datas.adv_list != null)
		// {
		AndroidUtils.MainHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				mDotContainer.removeAllViews();
				for (int i = 0; i < length; i++)
				{
					ImageView view = new ImageView(getActivity());
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					params.topMargin = AndroidUtils.dp2px(getActivity(), 2);
					params.bottomMargin = AndroidUtils.dp2px(getActivity(), 2);
					params.leftMargin = AndroidUtils.dp2px(getActivity(), 20);
					params.rightMargin = AndroidUtils.dp2px(getActivity(), 20);
					view.setLayoutParams(params);
					int dotUnfocusId = ResourceUtil.getDrawableId(getContext(), "dot_container_bg1");
					view.setImageResource(dotUnfocusId);
					view.setScaleType(ScaleType.CENTER);
					mDotContainer.addView(view);
				}
				setSelectdDot(0);
			}
		});
		// }
	}

	private void setSelectdDot(final int index)
	{
		AndroidUtils.MainHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				LogUtil.d(TAG, "setSelectdDot: " + index);
				int dotFocusId = ResourceUtil.getDrawableId(getContext(), "dot_container_bg");
				int dotUnfocusId = ResourceUtil.getDrawableId(getContext(), "dot_container_bg1");
				int count = mDotContainer.getChildCount();
				for (int i = 0; i < count; i++)
				{
					ImageView child = (ImageView)mDotContainer.getChildAt(i);
					if (i == index)
					{
						child.setImageResource(dotFocusId);
					}
					else
					{
						child.setImageResource(dotUnfocusId);
					}
				}
			}
		});
	}
	private void changeNumber(){
		Request request=new Request.Builder().url(Config.BaseInterface+"/goods/stock").addHeader("accept","application/vnd.zltech.shop.v1+json").addHeader("Authorization","Bearer"+Config.token).get().build();
		OkHttpClient okHttpClient=new OkHttpClient();
		okHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				final  String json=response.body().string();
				final  int start=json.indexOf("stockNum");
				final  int end=json.lastIndexOf("}");
				AndroidUtils.MainHandler.post(new Runnable() {
					@Override
					public void run() {
						product_all_number.setText(json.substring(start+10,end-1));
					}
				});

			}
		});
	}
	private void loadGoodsCategory()
	{
		RequestManager.loadGoodsCategorys(getActivity(), new RequestCallback()
		{
			@Override
			public void onRequestResult(int requestCode, long taskId, BaseResponse response, DataFrom from)
			{
				if (response.resultCode == BaseResponse.RESULT_OK)
				{
					GoodsCategorysResponse mGoodsCategoryResponse = (GoodsCategorysResponse)response;
					mAdapter.clear();
					for (CategoryItem mCategoryItem : mGoodsCategoryResponse.findZuanShiCategoryList())
					{
						mAdapter.addItem(new CategoryAdapterItem(mCategoryItem));
					}
					// mAdapter.addItem(new CategoryAdapterItem(null));
				}
				else
				{
					showToast(response.error);
				}
			}

			@Override
			public void onRequestError(int requestCode, long taskId, ErrorInfo error)
			{
				showToast("网络错误: " + error.errorCode);
			}
		});
	}

	private class AdvertisementPagerAdapterItem extends AbsAdapterItem
	{
		private ImageInfo mImageInfo;
		private String mGoodsId;
		private String mTag;
		private String mLink;
		private boolean mIsShop;
		private String murl;
		private int mGcate;
		private int mCustomization;

		public AdvertisementPagerAdapterItem(ImageInfo imageInfo, String goodsId, String tag, boolean isShop)
		{
			mImageInfo = imageInfo;
			mGoodsId = goodsId;
			mTag = tag;
			mIsShop = isShop;

		}
		public AdvertisementPagerAdapterItem(ImageInfo imageInfo,String goodsId,int gcate,int customization){
			mImageInfo=imageInfo;
			mGoodsId=goodsId;
			mGcate=gcate;
			mCustomization=customization;

		}

		public AdvertisementPagerAdapterItem(ImageInfo imageInfo,String goodsId,int gcate,int customization,String url){
			mImageInfo=imageInfo;
			mGoodsId=goodsId;
			mGcate=gcate;
			mCustomization=customization;
			murl=url;

		}


		public AdvertisementPagerAdapterItem(ImageInfo imageInfo, String link)
		{
			mImageInfo = imageInfo;
			mLink = link;
		}

		@Override
		public View onCreateView(int position, ViewGroup parent)
		{
			ExtendImageView view = new ExtendImageView(getActivity());
			view.setBackgroundColor(getResources().getColor(android.R.color.white));
			ViewPager.LayoutParams params = new ViewPager.LayoutParams();
			view.setLayoutParams(params);
			view.setScaleType(ScaleType.CENTER_CROP);
			// view.setAutoRecyleOnDetachedFromWindow(true);
			view.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (AndroidUtils.isFastClick())
						return;
					LogUtil.d(TAG, "onClick: " + mGoodsId + "; " + murl);
					if (mGoodsId!=null)
					{
						// if (TextUtils.isEmpty(mTag) || mTag.equals(GoodsItem.TAG_RING))
						// add(MainFragment.instance, MainFragment.instance.fragmentC1ID, new
						// ProductDetailsFragment(mTag,
						// mGoodsId, true), true);
						if(mGcate==1) {
							MainFragment.instance.addProductDetailFragmentToCurrent(mGoodsId, "one", mIsShop, true, false,"http://www.i888vip.com/addons/ewei_shop/template/pad/default/shop/new-singleGoodsDetail.html?goodsId="+mGoodsId);
						}else if(mGcate==2){
							MainFragment.instance.addProductDetailFragmentToCurrent(mGoodsId, "two", mIsShop, true, false, "http://www.i888vip.com/addons/ewei_shop/template/pad/default/shop/new-doubleRingDetail.html?goodsId="+mGoodsId);
						}else if(mGcate==3){
							MainFragment.instance.addProductDetailFragmentToCurrent(mGoodsId, "two", mIsShop, true, false, "http://www.i888vip.com/addons/ewei_shop/template/pad/default/shop/new-specialDoubleRingDetail.html?goodsId="+mGoodsId);
						}
						// else
						// add(MainFragment.instance, MainFragment.instance.fragmentC1ID, new ProductDetailsFragment(
						// 1, mGoodsId), true);
					}
//					else if (!TextUtils.isEmpty(mLink))
//					{
//						String actualUrl = mLink;
//						if (mLink.contains("/pad/default"))
//						{
//							actualUrl = Config.BaseWebUrl + mLink.substring(mLink.indexOf("/pad/default"));
//						}
//						LogUtil.d(TAG, "openWindow: actualUrl=" + actualUrl);
//						// add(MainFragment.instance, MainFragment.instance.fragmentC1ID, new MainRecommendWebFragment(
//						// actualUrl, 1), true);
//						MainFragment.instance.addWebFragmentToCurrent(actualUrl, false);
//					}
				}
			});
			return view;
		}

		@Override
		public void onUpdateView(View view, int position, ViewGroup parent)
		{

		}

		@Override
		public void onLoadViewResource(View view, int position, ViewGroup parent)
		{
			ExtendImageView imageView = (ExtendImageView)view;
			if (mImageInfo != null)
				imageView.setImageDataSource(mImageInfo.url, mImageInfo.filemtime, DecodeMode.FIT_WIDTH);
			imageView.startImageLoad();
		}

		@Override
		public void onRecycleViewResource(View view, int position, ViewGroup parent)
		{
			ExtendImageView imageView = (ExtendImageView)view;
			imageView.recyleBitmapImage();
		}
	}



	private class CategoryAdapterItem extends AbsAdapterItem
	{
		CategoryItem mCategoryItem;
		int mIndex;

		// GoodsCategoryResponse mGoodsCategoryResponse;

		public CategoryAdapterItem(CategoryItem categoryItem)
		{
			// this.mGoodsCategoryResponse = mGoodsCategoryResponse;
			this.mCategoryItem = categoryItem;
		}

		public CategoryAdapterItem(int index)
		{
			this.mIndex = index;
		}

		@Override
		public View onCreateView(int position, ViewGroup parent)
		{
			// int layout = ResourceUtil.getLayoutId(getActivity(), "main_index_item");
			// View view = View.inflate(getActivity(), layout, null);
			// int id = ResourceUtil.getId(getActivity(), "image");
			// ExtendImageView image = (ExtendImageView) view.findViewById(id);
			// image.setAutoRecyleBitmap(true);

			ExtendImageView view = new ExtendImageView(getContext());
			view.setScaleType(ScaleType.FIT_CENTER);
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			int padding = 40;
			view.setPadding(padding, padding, padding, padding);
			ViewTools.autoFitViewDimension(view, parent, FitMode.FIT_IN_HEIGHT, 1);
			ViewTools.adapterViewPadding(view, MainActivity.scaled);

			return view;
		}

		@Override
		public void onUpdateView(View view, int position, ViewGroup parent)
		{
			// int id = ResourceUtil.getId(getActivity(), "image");
			// ExtendImageView image = (ExtendImageView)view.findViewById(id);
			ExtendImageView image = (ExtendImageView)view;
			if (mCategoryItem != null && mCategoryItem.thumb != null)
			{
				image.setImageDataSource(mCategoryItem.thumb.url, mCategoryItem.thumb.filemtime, DecodeMode.FIT_HEIGHT);
			}
			else
			{
				switch (mIndex)
				{
					// case 1:
					// image.setImageResource(R.drawable.gem);
					// break;
					case 0:
						image.setImageResource(R.drawable.gia);
						break;

					case 1:
						image.setImageResource(R.drawable.designer);
						break;
				}
				ViewTools.autoFitViewDimension(image, parent, FitMode.FIT_IN_HEIGHT, 1);
			}
			image.startImageLoad(false);
		}

		@Override
		public void onLoadViewResource(View view, int position, ViewGroup parent)
		{

		}

		@Override
		public void onRecycleViewResource(View view, int position, ViewGroup parent)
		{
			// int id = ResourceUtil.getId(getActivity(), "image");
			// ExtendImageView image = (ExtendImageView) view.findViewById(id);
			// image.recyleBitmapImage();
		}

		public void onItemClick(View adapterView, ViewGroup parent, View view, int position, long id)
		{
			if (mCategoryItem != null)
			{
				// MainFragment.instance.setCurrentFragmentToProductList(mCategoryItem.id, true);
				MainFragment.instance.addFragmentToCurrent(new ProductListFragment(mCategoryItem.id, true,"order",mCategoryItem.id,0), false);
			}
			else
			{
				String url="";
				switch (mIndex)
				{
					// case 1:
					// url = Config.WebGems + "?key=" + LoginHelper.getUserKey(getContext());
					// break;
					case 0:
//						url = Config.WebGIADiamonds + "?key=" + LoginHelper.getUserKey(getContext());
						url=Config.BasePrefix+"/addons/ewei_shop/template/pad/default/api/new-gia.html?isFromOrder=false";
						break;


					case 1:
						url = "http://119.23.150.98/web/index.php?c=article&a=notice-show&do=detail&id=1";
						break;
				}
				MainFragment.instance.addWebFragmentToCurrent(url, false);
			}
		}
	}
}
