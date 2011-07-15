package com.shine.core;

import android.content.Context;

public class ResourceManager {

	private static ResourceManager resourceManager;	
	private Context mContext ;
	
	
	

	public ResourceManager(Context context) {
		mContext = context;
	}

	public static ResourceManager getInstance(Context context) {		
		if (resourceManager == null) {
			resourceManager = new ResourceManager(context);
			resourceManager.initResource();
		}
		return resourceManager;
	}

	private void initResource() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void destroy() {
		
		
	}

}
