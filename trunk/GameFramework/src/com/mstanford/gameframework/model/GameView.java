package com.mstanford.gameframework.model;

import android.graphics.Canvas;

public interface GameView {

	/**
	 * 绘图
	 * 
	 * @param N
	 *            /A
	 * 
	 * @return null
	 */
	public abstract void onDraw(Canvas canvas);

	/**
	 * 按键按下
	 * 
	 * @param N
	 *            /A
	 * 
	 * @return null
	 */
	public abstract boolean onKeyDown(int keyCode);

	/**
	 * 按键弹起
	 * 
	 * @param N
	 *            /A
	 * 
	 * @return null
	 */
	public abstract boolean onKeyUp(int keyCode);
	
	/**
	 * 触屏
	 * 
	 * @param N
	 *            /A
	 * 
	 * @return null
	 */
	public abstract boolean onTouch(int keyCode);

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
}
