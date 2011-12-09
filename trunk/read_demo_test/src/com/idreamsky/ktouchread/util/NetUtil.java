package com.idreamsky.ktouchread.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

	/**
	 * 判断网络的连接情况
	 * 
	 * 
	 */
	public static boolean checkNetwork(Context context) {
		boolean result;
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (manager == null || info == null || !info.isAvailable() 
				|| !info.isConnected()) {
			result = false;
		} else {
			result = true;
		}
		return result;
	}
	
}
