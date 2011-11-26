package com.gmail.txyjssr.images;

import android.util.Log;

public class ApplicationUtils {
	public static final String TAG = "ImagesTime";
	public static final boolean DEBUG = true;

	public static void log(Object o, String message) {
		if (!DEBUG)
			return;

		String className = o.getClass().getSimpleName();
		Log.i(TAG, className + "-->" + message);
	}

}
