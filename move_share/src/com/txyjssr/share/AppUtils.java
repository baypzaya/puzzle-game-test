package com.txyjssr.share;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.view.Window;
import android.view.WindowManager;

public class AppUtils {
	
	// 设置屏幕不能锁屏
	private static void setUnlocked(Activity activity) {
		Window win = activity.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		winParams.flags |= (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		win.setAttributes(winParams);
	}

	// 设置屏幕可以锁屏，当然默认是也是可以锁屏的
	private static void setLocked(Activity activity) {
		Window win = activity.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		winParams.flags &= (~WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				& ~WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				& ~WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON & ~WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		win.setAttributes(winParams);
	}
	
	public static boolean isWiFiActive(Context inContext) {
		Context context = inContext.getApplicationContext();
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);		
		return wifiManager.isWifiEnabled();
	}
}
