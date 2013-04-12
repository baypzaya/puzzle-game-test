package com.gmail.txyjssr.mindmap;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;

public class EngineApplication extends Application {
	private static Context sContext;
	private static Handler sHandler;
	public static float sDensity = 1;

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
	
	public static float transformDP2PX(float value) {
		return value * EngineApplication.sDensity;
	}

}
