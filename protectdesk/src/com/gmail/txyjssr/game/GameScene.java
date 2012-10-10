package com.gmail.txyjssr.game;

import java.util.Collection;
import java.util.List;

import android.util.Log;
import android.view.MotionEvent;

import com.gmail.txyjssr.R;
import com.gmail.txyjssr.game.data.Bullet;
import com.gmail.txyjssr.game.data.Enemy;
import com.gmail.txyjssr.game.data.GameData;
import com.gmail.txyjssr.game.data.OnLifeChangedListener;
import com.gmail.txyjssr.game.data.OnShotListener;
import com.gmail.txyjssr.game.data.Tower;
import com.wiyun.engine.nodes.Director;
import com.wiyun.engine.nodes.Node;
import com.wiyun.engine.nodes.Scene;
import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.opengl.Texture2D;
import com.wiyun.engine.types.WYPoint;
import com.wiyun.engine.types.WYRect;
import com.wiyun.engine.utils.ResolutionIndependent;
import com.wiyun.engine.utils.TargetSelector;
import com.wiyun.engine.utils.ZwoptexManager;

public class GameScene extends Scene implements OnLifeChangedListener, OnShotListener {

	static int TILE_WIDTH = 32;
	static int TILE_HEIGHT = 32;

	private GameData mGameData;

	float mTileWidth;
	float mTileHeight;
	int mTileXCount;
	int mTileYCount;

	private WYPoint currentPoint;

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
		ZwoptexManager.addZwoptex("astar", R.raw.astar, tex);

		bgLayer = new GameBackGroundLayer(mTileWidth, mTileHeight, mTileXCount, mTileYCount);
		addChild(bgLayer);
		bgLayer.autoRelease();

		enemiesLayer = new EnemiesLayer(mTileWidth, mTileHeight,this);
		addChild(enemiesLayer);
		enemiesLayer.autoRelease();

		defenseLayer = new DefenseLayer();
		addChild(defenseLayer);
		defenseLayer.autoRelease();

		bulletsLayer = new BulletsLayer();
		addChild(bulletsLayer);
		bulletsLayer.autoRelease();

		mGameData = GameData.getInstance();

		schedule(new TargetSelector(this, "shotEnemy", new Object[] {}), 0.2f);
		setTouchEnabled(true);
	}

	public void shotEnemy() {
		Log.i("yujsh log", "shot");
		Collection<Tower> towers = mGameData.towerMap.values();
		for (Tower tower : towers) {
			WYRect rect = WYRect.make(tower.getPositionX() - tower.scope / 2, tower.getPositionY() - tower.scope / 2,
					tower.scope, tower.scope);
			Enemy enemy = mGameData.getEnemyByScope(rect);
			if (enemy != null && tower.canShot()) {
				bulletsLayer.addBullet(tower, enemy,this);
			}
		}
	}

	@Override
	public boolean wyTouchesBegan(MotionEvent event) {
		currentPoint = Director.getInstance().convertToGL(event.getX(), event.getY());
		defenseLayer.createSpritePoint(currentPoint);
		return true;
	}

	@Override
	public boolean wyTouchesEnded(MotionEvent event) {
		Tower tower = defenseLayer.showTower(currentPoint);
		GameData.getInstance().addTower(tower.getPointer(),tower);
		return true;
	}

	@Override
	public boolean wyTouchesMoved(MotionEvent event) {
		currentPoint = Director.getInstance().convertToGL(event.getX(), event.getY());
		defenseLayer.moveSpritePoint(currentPoint);
		return true;
	}

	@Override
	public void onChanged(Node target, long life) {
		if (target instanceof Enemy) {
			enemiesLayer.updateEnemyState((Enemy)target, life);
		}
	}

	@Override
	public void onShot(Enemy enemy, Bullet bullet) {
		long life = enemy.getLife() - bullet.getPower();
		enemy.setLife(life);
	}

}
