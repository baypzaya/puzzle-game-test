package com.gmail.txyjssr.game;

import android.view.MotionEvent;

import com.gmail.txyjssr.R;
import com.wiyun.engine.nodes.Director;
import com.wiyun.engine.nodes.Scene;
import com.wiyun.engine.opengl.Texture2D;
import com.wiyun.engine.utils.ResolutionIndependent;
import com.wiyun.engine.utils.ZwoptexManager;

public class GameScene extends Scene {
	
	static int TILE_WIDTH = 32;
	static int TILE_HEIGHT = 32;
	
	float mTileWidth;
	float mTileHeight;
	int mTileXCount;
	int mTileYCount;

	private GameBackGroundLayer bgLayer;
	private EnemiesLayer enemiesLayer;
	private DefenseLayer defenseLayer;

	public GameScene() {
		mTileWidth = ResolutionIndependent.resolveDp(TILE_WIDTH);
		mTileHeight = ResolutionIndependent.resolveDp(TILE_HEIGHT);
		mTileXCount = (int) (Director.getInstance().getWindowSize().width / mTileWidth);
		mTileYCount = (int) (Director.getInstance().getWindowSize().height / mTileHeight);
		
		Texture2D tex = Texture2D.makePNG(R.drawable.astar);
		ZwoptexManager.addZwoptex("astar", R.raw.astar,tex);
		
		bgLayer = new GameBackGroundLayer(mTileWidth,mTileHeight,mTileXCount,mTileYCount);
		addChild(bgLayer);
		bgLayer.autoRelease();
		
		enemiesLayer = new EnemiesLayer(mTileWidth,mTileHeight);
		addChild(enemiesLayer);
		enemiesLayer.autoRelease();
		
		defenseLayer = new DefenseLayer();
		addChild(defenseLayer);
		defenseLayer.autoRelease();
		
//		setTouchEnabled(true);
		
	}
	
	@Override
	public boolean wyTouchesBegan(MotionEvent event) {
		return false;
	}

}
