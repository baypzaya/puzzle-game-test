package com.shine.core;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public  interface IScene {
	
	public View getContentView();
	public void destroy();
	public void pause();
	public void resume();
	public boolean onTouch(MotionEvent event);
	public void create();
	public boolean onKeyDown(int keyCode, KeyEvent event);
	 
}
 
