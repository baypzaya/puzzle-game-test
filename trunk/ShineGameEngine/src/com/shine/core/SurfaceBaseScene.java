package com.shine.core;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class SurfaceBaseScene implements IScene {
	private GameSurfaceView mSurfaceView;
	private Context mContext = EngineProperties.sContext;
	public SurfaceBaseScene(){
		create();
	}
	@Override
	public View getContentView() {
		return mSurfaceView;
	}
	
	@Override
	public void create() {
		mSurfaceView = new GameSurfaceView(mContext,null);		
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {		
		return false;
	}

	

}
