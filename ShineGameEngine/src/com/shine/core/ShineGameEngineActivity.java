package com.shine.core;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class ShineGameEngineActivity extends Activity {

	public static final int UPDATE_CURREN_SCENE = 1;
	public static final int INIT_RESOURCES = 2;
	public static final int SHOW_DEFAULT_SCENE = 3;

	private Handler mHandler;
	private ResourceManager mResourceManager;
	private SceneManager mSceneManager;
	private FrameLayout mHostLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EngineProperties.sContext = this;
		mHandler = new EngineHandler();
		mHostLayout = new FrameLayout(this);
		this.setContentView(mHostLayout);
		create();

		Runnable runnable = new Runnable() {
			public void run() {
				initResources();
				onInitResourcesCompleted();
			}
		};
		new Thread(runnable).start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mResourceManager.destroy();
		mSceneManager.destroy();

		System.exit(0);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSceneManager.getCurrentScene().pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSceneManager.getCurrentScene().resume();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mSceneManager.getCurrentScene().onTouch(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mSceneManager.getCurrentScene().onKeyDown(keyCode, event)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public Handler getEngineHandler() {
		return mHandler;
	}

	private class EngineHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			ShineGameEngineActivity activity = ShineGameEngineActivity.this;
			int what = msg.what;
			switch (what) {
			case UPDATE_CURREN_SCENE:
				int sceneId = (Integer) msg.obj;
				mSceneManager.updateCurrenScene(mHostLayout, sceneId);
				break;
			case INIT_RESOURCES:

				break;
			case SHOW_DEFAULT_SCENE:
				mSceneManager.showDefaultScene(mHostLayout);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onAttachedToWindow() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
	}

	private void create() {
		mSceneManager = SceneManager.getInstance();
		mSceneManager.showSplashScene(mHostLayout);
	}

	public void initResources() {
		mResourceManager = ResourceManager.getInstance(this);
	}

	public void onInitResourcesCompleted() {
		Message message = mHandler.obtainMessage(SHOW_DEFAULT_SCENE);
		message.sendToTarget();
	}

}