package com.shine.core;

import java.io.IOException;

import com.shine.core.utils.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;

public class ResourceManager {

	private static ResourceManager resourceManager;	
	private Context mContext ;
	
	Bitmap paopao;

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
			paopao = BitmapUtils.getBitmapFromAsset(mContext, "paopao.png");
			
		} catch (IOException e) {
			e.printStackTrace();
			LogManager.d("init failed");
		}
	}

	public void destroy() {
		paopao.recycle();
		
	}

}
