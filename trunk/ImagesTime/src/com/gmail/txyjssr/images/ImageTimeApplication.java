package com.gmail.txyjssr.images;

import android.app.Application;
import android.content.Context;

public class ImageTimeApplication extends Application {
	public static Context sContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = this.getApplicationContext();
	}
	
	
	
}
