package com.idreamsky.ktouchread.bookshelf;

import android.app.Application;
import android.content.Context;

public class KTouchReadApplication extends Application {
	
	public static Context sContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = this.getApplicationContext();
	}
}
