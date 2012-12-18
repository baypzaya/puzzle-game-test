package com.ktouch.pcs;

import android.app.Application;
import android.content.Context;

public class PCSManangerApplication extends Application {

	private static Context sContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = this.getApplicationContext();
	}

	public static Context getContext() {
		return sContext;
	}

}
