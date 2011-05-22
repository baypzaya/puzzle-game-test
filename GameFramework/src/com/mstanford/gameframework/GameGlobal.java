package com.mstanford.gameframework;

import android.view.KeyEvent;

public class GameGlobal {
	public static final int	GAME_SPLASH		= 1;
	public static final int	GAME_MENU		= 2;
	public static final int	GAME_ABOUT		= 3;
	public static final int	GAME_HELP		= 4;
	public static final int	GAME_RUN		= 5;
	public static final int	GAME_CONTINUE	= 6;
	public static final int	KEY_DPAD_UP		= KeyEvent.KEYCODE_DPAD_UP;
	public static final int	KEY_DPAD_DOWN	= KeyEvent.KEYCODE_DPAD_DOWN;
	public static final int	KEY_DPAD_LEFT	= KeyEvent.KEYCODE_DPAD_LEFT;
	public static final int	KEY_DPAD_RIGHT	= KeyEvent.KEYCODE_DPAD_RIGHT;
	public static final int	KEY_DPAD_OK		= KeyEvent.KEYCODE_DPAD_CENTER;	// 23
	/* 右软键需要自己定义，否则右软键则是退出 */
	public static final int	KEY_SOFT_RIGHT	= KeyEvent.KEYCODE_BACK;			// 4

	/* 游戏循环时间 */
	public static final int	GAME_LOOP		= 100;
	/* 屏幕的宽高 */
	public static final int	SCREENW			= 320;
	public static final int	SCREENH			= 480;
	public static final int	BORDERW			= 320;
	public static final int	BORDERH			= 352;
	public static final int	BORDERX			= (SCREENW - BORDERW) / 2;
	public static final int	BORDERY			= (SCREENH - BORDERH) / 2;
	public static final int	MessageBoxH		= 70;

	/* 文字的尺寸 */
	public static final int	TextSize		= 16;
}
