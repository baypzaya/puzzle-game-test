package com.shine.core;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

	// 游戏状态值:ready
	public final static int GS_READY = 0;

	// 游戏线程每执行一次需要睡眠的时间
	private final static int DELAY_TIME = 100;

	// 上下文,方便获取到应用的各项资源,如图片、音乐、字符串等
	private Context context;

	// 与Activity其他View交互用的handler
	private Handler handler;

	// 由SurfaceView提供的SurfaceHolder
	private SurfaceHolder surfaceHolder;

	// 游戏线程运行开关
	private boolean running = false;

	// 游戏状态
	private int gameState;

	// 当前surface/canvas的高度,在surfaceChanged方法中被设置
	private int mCanvasHeight = 1;

	// 当前surface/canvas的宽度,在surfaceChanged方法中被设置
	private int mCanvasWidth = 1;

	// 当前游戏界面
	private GameView mGameView = new PuzzleGameTest(context);

	/**
	 * 游戏是否暂停
	 */
	private boolean isPaused = false;

	public GameThread(SurfaceHolder holder, Context context, Handler handler) {
		this.surfaceHolder = holder;
		this.context = context;
		this.handler = handler;

	}

	/**
	 * 设置游戏状态
	 * 
	 * @param mode
	 *            游戏状态
	 */
	public void setState(int mode) {
		synchronized (surfaceHolder) {
			setState(mode, null);
		}
	}

	/**
	 * 设置游戏状态
	 * 
	 * @param mode
	 *            游戏状态
	 * @param message
	 *            设置游戏状态时的附加文字信息
	 */
	public void setState(int mode, CharSequence message) {
		synchronized (surfaceHolder) {
			if (message != null) {
				LogManager.d(message.toString());
			}
			if (mGameView != null) {
				synchronized (mGameView) {
					mGameView.reCycle();
					System.gc();
				}
			}

			if (mGameView == null) {
				this.pause();
			}
		}
	}

	/**
	 * 暂停游戏逻辑
	 */
	public void pause() {
		synchronized (surfaceHolder) {
			isPaused = true;
		}
	}

	/**
	 * 恢复运行游戏逻辑
	 */
	public void unpause() {
		// 如果游戏中有时间,别忘记应将其在这里调整到正常
		synchronized (surfaceHolder) {
			isPaused = false;
		}
	}

	/**
	 * 当Activity因销毁而被重新创建时,在这里恢复游戏上次运行的数据
	 * 
	 * @param saveState
	 *            Activity传来的保存游戏数据的容器
	 */
	public void restoreState(Bundle saveState) {
		// TODO
	}

	/**
	 * 在Activity切到后台时保存游戏的数据
	 * 
	 * @param outState
	 *            保存游戏数据的容器
	 */
	public void saveState(Bundle outState) {
		// TODO
	}

	/**
	 * 设置游戏线程运行开关
	 * 
	 * @param b
	 *            开/关
	 */
	public void setRunning(boolean b) {
		running = b;
	}

	/**
	 * 处理按下按键的事件
	 * 
	 * @param keyCode
	 *            按键事件动作值
	 * @param msg
	 *            按键事件对象
	 * @return 是否处理完
	 */
	public boolean doKeyDown(int keyCode, KeyEvent msg) {
		synchronized (surfaceHolder) {
			// TODO
			return false;
		}
	}

	/**
	 * 处理弹起按键的事件
	 * 
	 * @param keyCode
	 *            按键事件动作值
	 * @param msg
	 *            按键事件对象
	 * @return 是否处理完
	 */
	public boolean doKeyUp(int keyCode, KeyEvent msg) {

		synchronized (surfaceHolder) {
			// TODO
		}
		return false;
	}

	public boolean doTouch(MotionEvent event) {
		synchronized (surfaceHolder) {
			mGameView.onTouch(event);
		}
		return false;
	}

	/**
	 * 设置surface/canvas的宽度和高度
	 * 
	 * @param width
	 *            由SurfaceHolder传来的宽度
	 * @param height
	 *            由SurfaceHolder传来的高度
	 */
	public void setSurfaceSize(int width, int height) {
		// synchronized to make sure these all change atomically
		synchronized (surfaceHolder) {
			mCanvasWidth = width;
			mCanvasHeight = height;

			// 不要忘记每次画布的宽度和高度改变时, 在这里对图片等资源做缩放等相关适配屏幕的处理
			// TODO
			mGameView.onSurfaceSizeChanged(width, height);
		}
	}

	public void run() {
		while (running) {
			if (!isPaused) {
				Canvas c = null;
				try {
					c = surfaceHolder.lockCanvas(null);
					synchronized (surfaceHolder) {
						doDraw(c);
					}

					logic();

				} finally {
					if (c != null) {
						surfaceHolder.unlockCanvasAndPost(c);
					}
				}

				try {
					Thread.sleep(DELAY_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 游戏逻辑处理
	 */
	public void logic() {
		Log.v(this.getClass().getName(), "logic");
	}

	/**
	 * 初始化游戏开始时的参数
	 */
	public void doStart() {
	}

	/**
	 * 游戏绘画
	 */
	private void doDraw(Canvas canvas) {
		Log.v(this.getClass().getName(), "doDraw");
		if (canvas == null)
			return;

		if (mGameView != null) {
			synchronized (mGameView) {
				mGameView.onDraw(canvas);
			}
		}
	}

}
