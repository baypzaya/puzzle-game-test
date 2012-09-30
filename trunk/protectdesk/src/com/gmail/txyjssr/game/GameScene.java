package com.gmail.txyjssr.game;

import android.view.MotionEvent;

import com.wiyun.engine.nodes.Scene;

public class GameScene extends Scene {

	private GameBackGroundLayer bgLayer;

	public GameScene() {
		bgLayer = new GameBackGroundLayer();
		addChild(bgLayer);
		bgLayer.autoRelease();
	}
	
	@Override
	public boolean wyTouchesBegan(MotionEvent event) {
		return false;
	}

}
