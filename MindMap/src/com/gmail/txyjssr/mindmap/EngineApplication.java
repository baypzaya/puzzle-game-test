package com.gmail.txyjssr.mindmap;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class EngineApplication extends Application {
	private static Context sContext;
	private static Handler sHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = this.getApplicationContext();
	}
	
	public static Context getEngineCoreContextInstance() {
		return sContext;
	}

	public static Handler getsEngineCoreHandlerInstance() {
		return sHandler;
	}

}
