package com.ktouch.pcs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.baidu.oauth.BaiduOAuth;
import com.baidu.oauth.BaiduOAuth.BaiduOAuthResponse;

public class LoginActivity extends Activity {
	private final static String mbApiKey = "L6g70tBRRIXLsY0Z3HwKqlRE"; // your
																		// app_key";

	private BaiduOAuth oauthClient = new BaiduOAuth();
	private String mbOauth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		oauthClient.startOAuth(this, mbApiKey, new String[] { "basic",
				"netdisk" }, new BaiduOAuth.OAuthListener() {

			@Override
			public void onException(String msg) {

			}

			@Override
			public void onComplete(BaiduOAuthResponse response) {
				if (null != response) {
					mbOauth = response.getAccessToken();
					Log.i("yujsh log","mbOauth:"+mbOauth);
				}
			}

			@Override
			public void onCancel() {

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

}
