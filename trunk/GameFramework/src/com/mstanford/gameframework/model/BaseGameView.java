package com.mstanford.gameframework.model;

import android.content.Context;

public class BaseGameView {

	public Context mContext;
	public int mScreenWidth;
	public int mScreenHeight;

	public BaseGameView(Context context) {
		mContext = context;
	}

	public void setScreenSize(int width, int height) {
		mScreenWidth = width;
		mScreenHeight = height;
	}
}
