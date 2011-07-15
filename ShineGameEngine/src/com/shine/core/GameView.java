package com.shine.core;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface GameView {

	/**
	 * 绘图
	 *
	 */
	public abstract void onDraw(Canvas canvas);

	/**
	 * 按键按下
	 *
	 */
	public abstract boolean onKeyDown(int keyCode);

	/**
	 * 按键弹起
	 * 
	 */
	public abstract boolean onKeyUp(int keyCode);
	
	/**
	 * 触屏
	 * 
	 */
	public abstract boolean onTouch(MotionEvent event);

	/**
	 * 回收资源
	 * 
	 */
	public abstract void reCycle();

	/**
	 * 刷新
	 * 
	 */
	public abstract void refurbish();
	
	/**
	 * 屏幕大小发生变化
	 * @param width
	 * @param height
	 */
	public abstract void onSurfaceSizeChanged(int width, int height);
}
