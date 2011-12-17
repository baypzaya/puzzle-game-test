package com.idreamsky.ktouchread.util;


import com.idreamsky.ktouchread.bookshelf.KTouchReadApplication;
import com.idreamsky.ktouchread.bookshelf.R;

import android.content.Context;
import android.util.Log;

public class Configuration {

	public static final boolean DEBUG_VERSION = true;
	public static final boolean VERSION = getVersion();   //true : 阿里云的系统  false ： 普通的系统
	public static final boolean ReadBackGroudUseColor = true;   //阅读背景 ：true :背景色   false：背景图
	public static final String TAG = "idreamsky";

	public static final String TAG_THREAD = "DGCThread";

	private static boolean sDensityResolved = false;

	private static float sDensity = -1.0f;

	/**
	 * The logical density of the display.
	 */
	public static float getDensity(Context context) {
		float density = sDensity;
		if (!sDensityResolved) {
			density = context.getResources().getDisplayMetrics().density;
			density = density / 1.5f;
			sDensity = density;
			sDensityResolved = true;
		}
		return density;
	}

	/**
	 * Print the current thread log info.
	 */
	public static void trackThread(String methodName) {
		if (DEBUG_VERSION) {
			final Thread t = Thread.currentThread();
			final String tName = t.getName();
			if (null != tName && !tName.equals("")) {
				Log.v(TAG_THREAD, methodName + " run in thread: " + tName);
			} else {
				Log.v(TAG_THREAD,
						methodName + " run in thread: "
								+ Integer.toHexString(t.hashCode()));
			}
		}
	}
	
	public static boolean  getVersion(){
		return KTouchReadApplication.sContext.getResources().getBoolean(R.bool.VERSION);
	}
}
