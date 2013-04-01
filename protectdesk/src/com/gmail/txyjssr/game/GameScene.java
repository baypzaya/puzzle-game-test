package com.gmail.txyjssr.game;

import java.util.Collection;

import android.view.MotionEvent;

import com.gmail.txyjssr.game.data.Bullet;
import com.gmail.txyjssr.game.data.Enemy;
import com.gmail.txyjssr.game.data.GameData;
import com.gmail.txyjssr.game.data.OnEnemyStateChangedListener;
import com.gmail.txyjssr.game.data.OnShotListener;
import com.gmail.txyjssr.game.data.Tower;
import com.wiyun.engine.nodes.Director;
import com.wiyun.engine.nodes.Scene;
import com.wiyun.engine.types.WYColor3B;
import com.wiyun.engine.types.WYPoint;
import com.wiyun.engine.types.WYRect;
import com.wiyun.engine.types.WYSize;
import com.wiyun.engine.utils.TargetSelector;

public class GameScene extends Scene implements OnEnemyStateChangedListener, OnShotListener {

	private GameData mGameData;

	int mTileXCount;
	int mTileYCount;

	private WYPoint currentPoint;

	private GameBackGroundLayer bgLayer;
	private EnemiesLayer enemiesLayer;
	private DefenseLayer defenseLayer;
	private BulletsLayer bulletsLayer;
	private GameStatusLayer gameStatusLayer;
	private TowerSelectLayer towerSelectLayer;

	private TargetSelector shotTS;

	public GameScene() {

		mGameData = GameData.getInstance();
		mGameData.setCurrentMap(1);

		mTileXCount = GameData.TILE_COUNT_X;
		mTileYCount = GameData.TILE_COUNT_Y;

		WYSize s = Director.getInstance().getWindowSize();
		mGameData.mTileWidth = s.width / mTileXCount;
		mGameData.mTileHeight = s.height / mTileYCount;

		bgLayer = new GameBackGroundLayer(mGameData.mTileWidth, mGameData.mTileHeight, mTileXCount, mTileYCount);
		addChild(bgLayer);
		bgLayer.autoRelease();

		enemiesLayer = new EnemiesLayer(mGameData.mTileWidth, mGameData.mTileHeight, this);
		addChild(enemiesLayer);
		enemiesLayer.autoRelease();

		defenseLayer = new DefenseLayer();
		addChild(defenseLayer);
		defenseLayer.autoRelease();

		bulletsLayer = new BulletsLayer();
		addChild(bulletsLayer);
		bulletsLayer.autoRelease();

		gameStatusLayer = new GameStatusLayer();
		gameStatusLayer.setVisible(false);
		addChild(gameStatusLayer);
		gameStatusLayer.autoRelease();

		towerSelectLayer = new TowerSelectLayer();
		towerSelectLayer.setColor(WYColor3B.make(255, 255, 0));
		towerSelectLayer.setVisible(false);
		addChild(towerSelectLayer);
		towerSelectLayer.autoRelease();

		shotTS = new TargetSelector(this, "shotEnemy", null);
		schedule(shotTS, 0.2f);
		setTouchEnabled(true);

	}

	public void shotEnemy() {
		Collection<Tower> towers = mGameData.getTowerMap().values();
		for (Tower tower : towers) {
			WYRect rect = WYRect.make(tower.spriteTower.getPositionX() - tower.scope / 2,
					tower.spriteTower.getPositionY() - tower.scope / 2, tower.scope, tower.scope);
			Enemy enemy = mGameData.getEnemyByScope(rect);
			if (enemy != null && tower.canShot()) {
				bulletsLayer.addBullet(tower, enemy, this);
			}
		}
	}

	@Override
	public boolean wyTouchesBegan(MotionEvent event) {
		currentPoint = Director.getInstance().convertToGL(event.getX(), event.getY());
		// defenseLayer.createSpritePoint(currentPoint);
		return true;
	}

	@Override
	public boolean wyTouchesEnded(MotionEvent event) {
		if (towerSelectLayer.isVisible()) {
			towerSelectLayer.setVisible(false);
		} else {
			towerSelectLayer.setVisible(true);
			towerSelectLayer.showSeleteTower(currentPoint);
		}
		// if (mGameData.canLocationTower(currentPoint)&&haveEnoughMoney()) {
		// Tower tower = defenseLayer.showTower(currentPoint);
		// GameData.getInstance().addTower(tower.spriteTower.getPointer(),
		// tower);
		// }
		return true;
	}

	@Override
	public boolean wyTouchesMoved(MotionEvent event) {
		currentPoint = Director.getInstance().convertToGL(event.getX(), event.getY());
		// defenseLayer.moveSpritePoint(currentPoint);
		return true;
	}

	@Override
	public void onLifeChanged(Enemy target, long life) {
		enemiesLayer.updateEnemyState(target, life);
	}

	@Override
	public void onShot(Enemy enemy, Bullet bullet) {
		long life = enemy.getLife() - bullet.getPower();
		enemy.setLife(life);
	}

	@Override
	public void onCrossed(Enemy target) {
		boolean isOver = mGameData.destroyGame(target.getDestroyValue());
		if (isOver) {
			unschedule(shotTS);
			Director.getInstance().pauseUI();

			gameStatusLayer.setStatus(GameData.STATUS_OVER);
			gameStatusLayer.setVisible(true);
		}
	}

	private boolean haveEnoughMoney() {
		// next to do
		return true;
	}

}
