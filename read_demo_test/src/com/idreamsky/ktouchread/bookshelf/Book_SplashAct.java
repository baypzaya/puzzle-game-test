package com.idreamsky.ktouchread.bookshelf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.util.SettingUtils;

public class Book_SplashAct extends Activity {

	private boolean sign = true;//防止多次执行touch方法
	private static Object object = new Object();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		UrlUtil.Init(this);

		SettingUtils utils = new SettingUtils(this, "SaveParameter", Context.MODE_PRIVATE);
		String key = utils.getString("guide", "false");
		if (key.equals("true")) {
			// if (false) {
			Intent intent = new Intent(Book_SplashAct.this, Book_SplashAct1.class);
			startActivity(intent);
			finish();
		} else
			setContentView(R.layout.guide);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		synchronized (object) {
			if (sign) {
				sign = false;
				Intent intent = new Intent(Book_SplashAct.this, Book_SplashAct1.class);
				startActivity(intent);
				SettingUtils utils = new SettingUtils(this, "SaveParameter", Context.MODE_PRIVATE);
				utils.putString("guide", "true");
				utils.commitOperate();
			}
			this.finish();
		}
		return super.onTouchEvent(event);
	}

}
