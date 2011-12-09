package com.idreamsky.ktouchread.bookshelf;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SaveNotifiSetting {
     
	// 保存通知设置
	public static void saveParam(Context con, int flag) {
		SharedPreferences sp = con.getSharedPreferences("notify",
				Activity.MODE_PRIVATE);
		sp.edit().putInt("flag", flag).commit();
	}

	// 获取通知设置
	public static boolean getParam(Context con) {
		SharedPreferences sp = con.getSharedPreferences("notify",
				Activity.MODE_PRIVATE);
		return sp.getBoolean("flag",false);
	}

}
