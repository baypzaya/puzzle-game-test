package com.shine.core;

import java.util.Hashtable;
import java.util.Map;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

public class SceneManager {
	public static final int SPLASH_SCENE_INDEX = 0;
	public static final int MENUE_SCENE_INDEX = 1;
	public static final int GAME_SCENE_INDEX = 2;
	public static final int HELP_SCENE_INDEX = 3;
	public static final int SETTING_SCENE_INDEX = 4;
	public static final int ABOUT_SCENE_INDEX = 5;

	public static SceneManager sceneManager;

	private Map<Integer, IScene> mSceneMap = new Hashtable<Integer, IScene>();
	private IScene mCurrenScene;
	private int defaultSceneId = 1;

	public void updateCurrenScene(FrameLayout mHostLayout, int sceneId) {
		if (mCurrenScene != null) {
			mHostLayout.removeAllViews();
			mCurrenScene.destroy();
		}

		IScene scene = mSceneMap.get(sceneId);
		if (scene != null) {			
			View contentView = scene.getContentView();
			mHostLayout.addView(contentView);
			mCurrenScene = scene;
		} else {

		}

	}

	public static SceneManager getInstance() {
		if (sceneManager == null) {
			sceneManager = new SceneManager();
			sceneManager.registScene();
		}
		return sceneManager;
	}	

	private void registScene() {
		mSceneMap.put(MENUE_SCENE_INDEX, new MenueScene());
		mSceneMap.put(GAME_SCENE_INDEX, new SurfaceBaseScene());
		mSceneMap.put(SPLASH_SCENE_INDEX, new SplashScene());

	}

	public void showSplashScene(FrameLayout hostLayout) {
		updateCurrenScene(hostLayout, SPLASH_SCENE_INDEX);
	}

	public void showDefaultScene(FrameLayout hostLayout) {
		updateCurrenScene(hostLayout, defaultSceneId);
	}

	public IScene getCurrentScene() {
		return mCurrenScene;
	}

	public void destroy() {
		if (mCurrenScene != null) {
			mCurrenScene.destroy();
			mCurrenScene = null;
		}
		mSceneMap.clear();
	}

}
