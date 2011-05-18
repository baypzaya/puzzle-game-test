package com.mstanford.gameframework;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.widget.TextView;

public class GameSurfaceView extends SurfaceView implements Callback {
	
	public static final int	GAME_SPLASH		= 1;
	public static final int	GAME_MENU		= 2;
	public static final int	GAME_ABOUT		= 3;
	public static final int	GAME_HELP		= 4;
	public static final int	GAME_RUN		= 5;
	public static final int	GAME_CONTINUE	= 6;
	
	private GameThread gameThread;

	private TextView textview;

	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);

		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		gameThread = new GameThread(holder, context, new Handler() {
			@Override
			public void handleMessage(Message m) {
				textview.setText(m.getData().getString("text"));
			}
		});

		// 设置可获得焦点,确保能捕获到KeyEvent
		setFocusable(true);
	}

	/**
	 * 获取一个Activity传来的View协助SurfaceView显示游戏视图,View的具体类型可以根据游戏需要来定
	 */
	public void setTextView(TextView view) {
		this.textview = view;
	}

	public GameThread getThread() {
		return gameThread;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return gameThread.doKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return gameThread.doKeyUp(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {		
		return gameThread.doTouch(event);
	}

	/**
	 * 当SurfaceView得到或失去焦点时调用,使游戏暂停/恢复运行,
	 */
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if (!hasWindowFocus) {
			gameThread.pause();
		}
		else {
			gameThread.unpause();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.v(this.getClass().getName(), "surfaceChanged()");
		Log.i("yujsh log","width:"+width);
		Log.i("yujsh log","height:"+height);
		gameThread.setSurfaceSize(width, height);
		
		gameThread.setRunning(true);
		if (gameThread.isAlive()) {
			Log.v(this.getClass().getName(), "unpause gameThread");
			gameThread.unpause();
		}
		else {
			Log.v(this.getClass().getName(), "start gameThread");
			gameThread.start();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.v(this.getClass().getName(), "surfaceCreated()");
	}

	/**
	 * 为防止surface还会被创建(比如来电)导致gameThread再次启动出现错误,且Activity的onPause方法中已做暂停处理,
	 * 这边不对gameThread做处理
	 * @param holder
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.v(this.getClass().getName(), "surfaceDestroyed");
	}
}
