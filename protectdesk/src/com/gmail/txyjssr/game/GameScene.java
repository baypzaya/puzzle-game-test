package com.gmail.txyjssr.game;

import java.util.List;

import android.util.Log;

import com.gmail.txyjssr.R;
import com.wiyun.engine.nodes.Director;
import com.wiyun.engine.nodes.Node;
import com.wiyun.engine.nodes.Scene;
import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.opengl.Texture2D;
import com.wiyun.engine.types.WYRect;
import com.wiyun.engine.utils.ResolutionIndependent;
import com.wiyun.engine.utils.TargetSelector;
import com.wiyun.engine.utils.ZwoptexManager;

public class GameScene extends Scene {
	
	static int TILE_WIDTH = 32;
	static int TILE_HEIGHT = 32;
	
	private GameData mGameData;
	
	float mTileWidth;
	float mTileHeight;
	int mTileXCount;
	int mTileYCount;

	private GameBackGroundLayer bgLayer;
	private EnemiesLayer enemiesLayer;
	private DefenseLayer defenseLayer;
	private BulletsLayer bulletsLayer;
	
	

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
		
		bulletsLayer = new BulletsLayer();
		addChild(bulletsLayer);
		bulletsLayer.autoRelease();
		
		mGameData = GameData.getInstance();
		
		schedule(new TargetSelector(this, "shotEnemy", new Object[]{}));
	}
	
	public void shotEnemy() {
		Log.i("yujsh log","shot");
		List<Node> childrenDefense = defenseLayer.getChildren();
		for(Node node:childrenDefense){
			WYRect rect = WYRect.make(node.getPositionX()-50, node.getPositionY()-50, 100, 100);
			Sprite enemy = mGameData.getEnemyByScope(rect);
			if(enemy!=null){
				bulletsLayer.addBullet(node.getPositionX(),node.getPositionY(),enemy.getPositionX(),enemy.getPositionY());
			}
		}
	}

}
