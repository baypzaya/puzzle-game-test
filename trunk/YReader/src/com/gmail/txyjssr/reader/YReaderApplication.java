package com.gmail.txyjssr.reader;

import android.app.Application;
import android.content.Context;

public class YReaderApplication extends Application {
	
	public static Context sContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = this.getApplicationContext();
	}

}
