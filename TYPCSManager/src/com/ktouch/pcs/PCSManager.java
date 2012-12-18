package com.ktouch.pcs;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.baidu.oauth.BaiduOAuth;
import com.baidu.oauth.BaiduOAuth.BaiduOAuthResponse;

public class PCSManager extends IPCSManager.Stub {

	private final static String mbApiKey = "L6g70tBRRIXLsY0Z3HwKqlRE"; // your
																		// app_key";

	private BaiduOAuth oauthClient = new BaiduOAuth();
	private String mbOauth;
	private Context mContext;

	public PCSManager() {
		mContext = PCSManangerApplication.getContext();
	}

	@Override
	public boolean upload(String srcPath) throws RemoteException {
		if (mbOauth == null) {
			Log.i("yujsh log", "mContext" + mContext);
			Intent intent = new Intent(mContext, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
			// login();
		}
		Log.i("yujsh log", "upload path:" + srcPath);
		return false;
	}

	@Override
	public boolean download(String desPath) throws RemoteException {
		Log.i("yujsh log", "download path:" + desPath);
		return false;
	}

	private void login() {
		oauthClient.startOAuth(mContext, mbApiKey, new String[] { "basic",
				"netdisk" }, new BaiduOAuth.OAuthListener() {

			@Override
			public void onException(String msg) {

			}

			@Override
			public void onComplete(BaiduOAuthResponse response) {
				if (null != response) {
					mbOauth = response.getAccessToken();
					Log.i("yujsh log", "mbOauth:" + mbOauth);
				}
			}

			@Override
			public void onCancel() {

			}
		});
	}

}
