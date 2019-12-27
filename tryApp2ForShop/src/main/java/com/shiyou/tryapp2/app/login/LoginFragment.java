package com.shiyou.tryapp2.app.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.extend.ErrorInfo;
import android.extend.app.BaseFragment;
import android.extend.loader.BaseParser.DataFrom;
import android.extend.loader.Loader.CacheMode;
import android.extend.util.AndroidUtils;
import android.extend.util.LogUtil;
import android.extend.util.ResourceUtil;
import android.extend.util.ViewTools;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebView;
import android.webkit.WebViewFragment;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shiyou.tryapp2.Config;
import com.shiyou.tryapp2.Define;
import com.shiyou.tryapp2.RequestManager;
import com.shiyou.tryapp2.RequestManager.RequestCallback;
import com.shiyou.tryapp2.ResourceHelper2;
import com.shiyou.tryapp2.ResourceHelper2.OnResourceDownloadCallback;
import com.shiyou.tryapp2.app.MainActivity;
import com.shiyou.tryapp2.app.MainFragment;
import com.shiyou.tryapp2.app.product.MainIndexFragment;
import com.shiyou.tryapp2.app.product.MainWebFragment;
import com.shiyou.tryapp2.data.response.BaseResponse;
import com.shiyou.tryapp2.data.response.GoodsListResponse;
import com.shiyou.tryapp2.data.response.GoodsListResponse.GoodsItem;
import com.shiyou.tryapp2.data.response.LoginResponse;

import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.os.ParcelFileDescriptor.MODE_APPEND;

public class LoginFragment extends BaseFragment {
	EditText edit_user;
	EditText edit_password;
	TextView bindHint;

	boolean mLoginFinished = false;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mLayoutResID = ResourceUtil.getLayoutId(getContext(), "login_layout");
		View view = super.onCreateView(inflater, container, savedInstanceState);
		ViewTools.adapterAllViewMarginInChildren(view, MainActivity.scaled);
		ViewTools.adapterAllViewPaddingInChildren(view, MainActivity.scaled);
		ViewTools.adapterAllTextViewTextSizeInChildren(view, MainActivity.fontScaled);

		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		// 屏幕宽度（像素）
		int width = metric.widthPixels;
		// 屏幕高度（像素）
		int height = metric.heightPixels;
		 int id = ResourceUtil.getId(getContext(), "login_box");
		 View login_box = view.findViewById(id);
		FrameLayout.LayoutParams params=new FrameLayout.LayoutParams((int)(width/2+0.5f),(int)(height*4/5+0.5f));
		params.setMargins((int)(width/4+0.5f),(int)(height/10+0.5f),(int)(width/4+0.5f),0);
		login_box.setPadding((int)(width/20+0.5f),(int)(height*4/50+0.5f),(int)(width/20+0.5f),(int)(height*4/50+0.5f));
		login_box.setLayoutParams(params);
		// ViewTools.adapterViewSize(login_box, MainActivity.scaled);

		id = ResourceUtil.getId(getContext(), "edit_user");
		edit_user = (EditText) view.findViewById(id);
		edit_user.setText(LoginHelper.getUserName(getContext()));

		id = ResourceUtil.getId(getContext(), "edit_password");
		edit_password = (EditText) view.findViewById(id);
		edit_password.setText(LoginHelper.getUserPassword(getContext()));

		id = ResourceUtil.getId(getContext(), "login");
		View login = view.findViewById(id);
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doLogin();
			}
		});

		id = ResourceUtil.getId(getContext(), "bindHint");
		bindHint = (TextView) view.findViewById(id);
		bindHint.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (bindHint.getWidth() == 0)
					return;
				bindHint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				int width = bindHint.getWidth();
				LayoutParams params = bindHint.getLayoutParams();
				params.width = width;
				bindHint.setLayoutParams(params);
			}
		});

		return view;
	}

	private void doLogin() {
		if (mLoginFinished)
			return;
		final String user = edit_user.getText().toString();
		if (TextUtils.isEmpty(user)) {
			showToast("请输入用户名");
			return;
		}
		final String password = edit_password.getText().toString();
		if (TextUtils.isEmpty(password)) {
			showToast("请输入密码");
			return;
		}
		showLoadingIndicator();
		RequestBody formBody = new FormBody.Builder()
				.add("username",user)
				.add("password",password)
				.build();

		 Request request = new Request.Builder().url("https://api.i888vip.com/login").addHeader("accept", "application/vnd.zltech.shop.v1+json").post(formBody).build();
		OkHttpClient okHttpClient = new OkHttpClient();
		okHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				final String json = response.body().string();
				int codenum = json.indexOf("code");
				String code = json.substring(codenum + 6, codenum + 7);
				if (code.equals("0")) {
					int i = json.indexOf("access_token");
					int j = json.indexOf("token_type");
					final String token = json.substring(i + 15, j - 3);
					AndroidUtils.MainHandler.post(new Runnable() {
						@Override
						public void run() {

							Config.token = token;

						}
					});
					LoginHelper. saveUserInfo(getContext(),user,password);
					onLoginFinished(user,"","");
				}else{
					hideLoadingIndicator();
					showToast("账号或密码错误 请重新输入");

				}
			}
		});

//		LoginHelper.login(getActivity(), user, password, new RequestCallback()
//		{
//			@Override
//			public void onRequestResult(int requestCode, long taskId, BaseResponse response, DataFrom from)
//			{
//				hideLoadingIndicator();
//				switch (response.resultCode)
//				{
//					case BaseResponse.RESULT_OK:
//						LoginResponse loginResponse = (LoginResponse)response;
//						onLoginFinished(user, loginResponse.datas.realname, loginResponse.datas.key);
//						break;
//					case LoginResponse.RESULT_UNBIND:
//						showToast(response.error);
//						showBindHint();
//						break;
//					default:
//						showToast(response.error);
//						break;
//				}
//
//			}
//
//			@Override
//			public void onRequestError(int requestCode, long taskId, ErrorInfo error)
//			{
//
//				showToast("很难受，网络异常: " + error.errorCode);
//			}
//		});
//		getToken();
//	}
	}

	private void showBindHint() {
		AndroidUtils.MainHandler.post(new Runnable() {
			@Override
			public void run() {
				StringBuffer sb = new StringBuffer();
				sb.append("您的设备还未授权，请电话联系客服告知设备号授权").append('\n');
				sb.append("设备号: ").append(android.os.Build.SERIAL).append('\n');
				sb.append("客服电话: ").append(Config.ServicePhone);
				bindHint.setText(sb.toString());
				bindHint.setVisibility(View.VISIBLE);
			}
		});
	}

	public void onLoginFinished(final String userName, final String realName, final String userKey) {

		Log.d(TAG, "onLoginFinished: userName=" + LoginHelper.getUserName(getContext()));
		Log.d(TAG, "onLoginFinished: userPassword=" + LoginHelper.getUserPassword(getContext()));
		LogUtil.v(TAG, "onLoginFinished: " + userName + "; " + realName + "; " + userKey + "; " + mLoginFinished);
		if (mLoginFinished)
			return;
		mLoginFinished = true;
		LoginHelper.onLoginFinished(getActivity(), userName);
		if (getActivity() != MainActivity.instance) {

			getActivity().finish();
			if (MainFragment.instance != null) {
				MainFragment.instance.backToHomepage();

				MainFragment.instance.doRefresh();
			}
			if (MainIndexFragment.instance != null)
				MainIndexFragment.instance.doRefresh();
		} else {


			RequestManager.loadGoodsList(getActivity(), userKey, true, new RequestCallback() {
				@Override
				public void onRequestResult(int requestCode, long taskId,
											BaseResponse response, DataFrom from) {
					hideLoadingIndicator();
					String id = "5";
					String tag = Define.TAG_RING;
					if (response != null && response.resultCode == BaseResponse.RESULT_OK) {
						GoodsListResponse glResponse = (GoodsListResponse) response;
						if (glResponse.data != null && glResponse.data.list != null
								&& glResponse.data.list.length > 0) {
							for (GoodsItem item : glResponse.data.list) {
								if (item.tag.equals(Define.TAG_RING) && item.model_info != null) {
									id = item.id;
									tag = item.tag;
									break;
								}
							}
						}
					}
					BaseFragment.replace(getActivity(), new MainFragment(id, tag), false);
					if (from != DataFrom.SERVER)
						RequestManager.loadGoodsList(getContext(), userKey, true, null,
								CacheMode.PERFER_NETWORK);
				}

				@Override
				public void onRequestError(int requestCode, long taskId, ErrorInfo error) {
					onRequestResult(requestCode, taskId, null, DataFrom.SERVER);
				}
			});
//								}


		}
		//获取toKen

	}
	public void getToken(){
		Request request = new Request.Builder().url("https://api.i888vip.com/login?username=" + edit_user + "password" + edit_password).addHeader("accept", "application/vnd.zltech.shop.v1+json").get().build();
		OkHttpClient okHttpClient = new OkHttpClient();
		okHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				final String json = response.body().string();

					int i = json.indexOf("access_token");
					int j = json.indexOf("token_type");
					final String token = json.substring(i + 15, j - 3);
					AndroidUtils.MainHandler.post(new Runnable() {
						@Override
						public void run() {

							Config.token = token;

						}
					});

			}
		});
	}
}
