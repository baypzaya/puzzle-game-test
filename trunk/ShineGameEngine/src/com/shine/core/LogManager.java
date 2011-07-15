package com.shine.core;

import android.util.Log;

public class LogManager {
	private static final String TAG = "shine_engine ";
	private static final boolean SHOW_DEBUG = true;

	public static void d(String log) {
		if (SHOW_DEBUG) {
			Log.d(TAG, log);
		}
	}

}
