package com.shine.core;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

public class SplashScene implements IScene {
	
	private Context mContext = EngineProperties.sContext;
	private View mContentView;
	
	public SplashScene(){
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		mContentView = layoutInflater.inflate(R.layout.main,null);
	}

	@Override
	public View getContentView() {		
		return mContentView;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public boolean onTouch(MotionEvent event) {
		return true;
	}

	@Override
	public void create() {
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return false;
	}

}
