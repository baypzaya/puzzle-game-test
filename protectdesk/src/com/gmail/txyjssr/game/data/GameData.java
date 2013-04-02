package com.gmail.txyjssr.game.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import com.wiyun.engine.nodes.SpriteEx;
import com.wiyun.engine.types.WYPoint;
import com.wiyun.engine.types.WYRect;

public class GameData {
	public static final int STATUS_OVER = 1;
	public static final int STATUS_PAUSE = 2;
	public static final int STATUS_SUCCESS = 3;

	public static final int TILE_COUNT_X = 10;
	public static final int TILE_COUNT_Y = 6;

	public static GameData sGameData;

	private int gameSafeValue = 100;
	private int currentGate = 0;
	private int[][] currentMap;
	private int[] currentPath;
	private Integer[] defenseLocation;
	private boolean[][] defenseLocationMap = new boolean[TILE_COUNT_Y][TILE_COUNT_X];

	public float mTileWidth;
	public float mTileHeight;

	private Hashtable<Integer, Enemy> enemyMap = new Hashtable<Integer, Enemy>();
	private Hashtable<Integer, Tower> towerMap = new Hashtable<Integer, Tower>();

	private int currentMoney;
	private OnGameStatusChangedListener onMoneyChangedListener;

	private int[] enemies;
	private int currentEnemyIndex = 0;
	private boolean startNextTime = true;

	public void setOnGameStatusChangedListener(OnGameStatusChangedListener onGameStatusChangedListener) {
		this.onMoneyChangedListener = onGameStatusChangedListener;
	}

	public Hashtable<Integer, Enemy> getEnemyMap() {
		return enemyMap;
	}

	public Hashtable<Integer, Tower> getTowerMap() {
		return towerMap;
	}

	public static GameData getInstance() {
		if (sGameData == null) {
			sGameData = new GameData();
		}
		return sGameData;
	}

	public void addEnemy(int pointer, Enemy enemy) {
		enemyMap.put(pointer, enemy);
	}

	public void addTower(int pointer, Tower tower) {
		int lx = (int) (tower.spriteTower.getPositionX() / mTileWidth);
		int ly = (int) (tower.spriteTower.getPositionY() / mTileHeight);
		defenseLocationMap[ly][lx] = true;
		towerMap.put(pointer, tower);
	}

	public Enemy getEnemyByScope(WYRect rect) {
		Enemy enemy = null;
		Collection<Enemy> enemis = enemyMap.values();
		for (Enemy temEnemy : enemis) {
			if (rect.containsPoint(temEnemy.getPositionX(), temEnemy.getPositionY())) {
				enemy = temEnemy;
			}
		}
		return enemy;
	}

	public void removeEnemy(int pointer) {
		enemyMap.remove(pointer);
		if(enemyMap.isEmpty()){
			if(currentEnemyIndex >= enemies.length){
				setGameStatus(STATUS_SUCCESS);
			}else{
				startNextTime();
			}
		}
	}

	public int[][] getCurrentMap() {
		return currentMap;
	}

	public int[] getCurrentPath() {
		return currentPath;
	}

	public void setCurrentMap(int gate) {
		if (currentGate != gate) {
			currentMap = new int[][] { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, { 0, 0, 0, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 0, 1, 0, 0, 0, 0, 1, 1 }, { 1, 1, 0, 1, 0, 1, 1, 0, 1, 1 },
					{ 1, 1, 0, 0, 0, 1, 1, 0, 0, 0 }, { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };
			currentPath = new int[] { 1, 0, 1, 1, 1, 2, 2, 2, 3, 2, 4, 2, 4, 3, 4, 4, 3, 4, 2, 4, 2, 5, 2, 6, 2, 7, 3,
					7, 4, 7, 4, 8, 4, 9 };
			// defenseLocation();
			currentGate = gate;

			currentMoney = 100;

			enemies = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
					1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
					1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		}
	}

	private void defenseLocation() {
		ArrayList<Integer> intList = new ArrayList<Integer>();
		for (int x = 0; x < TILE_COUNT_X; x++) {
			for (int y = 0; y < TILE_COUNT_Y; y++) {
				if (currentMap[y][x] == 0) {
				} else if (currentMap[y][x] == 1) {
					intList.add(x);
					intList.add(y);
				} else if (currentMap[y][x] == 2) {
				}
			}
		}
		defenseLocation = (Integer[]) intList.toArray();
	}

	public WYPoint getDefenseLocation(WYPoint point) {
		int lx = (int) (point.x / mTileWidth);
		int ly = (int) (point.y / mTileHeight);

		WYPoint p = WYPoint.make(mTileWidth * lx + mTileWidth / 2, mTileHeight * ly + mTileHeight / 2);
		return p;
	}

	public boolean canLocationTower(WYPoint point) {
		int lx = (int) (point.x / mTileWidth);
		int ly = (int) (point.y / mTileHeight);
		boolean result = (currentMap[ly][lx] == 1) && (!defenseLocationMap[ly][lx]);
		return result;
	}

	public void clear() {
		enemyMap.clear();
		towerMap.clear();
		GameData.sGameData = null;
	}

	public void destroyGame(int destroyValue) {
		gameSafeValue = gameSafeValue - destroyValue;
		if(gameSafeValue <= 0){
			setGameStatus(STATUS_OVER);
		}
	}

	public int[] getCurrentUseableTower() {
		return new int[] { 1, 2, 3, 4 };
	}

	public int getMoney() {
		return currentMoney;
	}

	public void addMoney(int addMoney) {
		currentMoney += addMoney;
		if (onMoneyChangedListener != null) {
			onMoneyChangedListener.onMoneyChangedListener(currentMoney);
		}
	}

	public void subMoney(int subMoney) {
		currentMoney = currentMoney - subMoney;
		currentMoney = currentMoney < 0 ? 0 : currentMoney;
		if (onMoneyChangedListener != null) {
			onMoneyChangedListener.onMoneyChangedListener(currentMoney);
		}
	}

	public int getNextEnemy() {
		if (currentEnemyIndex >= enemies.length) {
			return -1;
		}

		int type = enemies[currentEnemyIndex];
		currentEnemyIndex++;
		return type;
	}

	public boolean hasNextEnemy() {
		if (currentEnemyIndex >= enemies.length) {
			return false;
		}
		boolean result = (currentEnemyIndex % 15) != 0 || startNextTime;
		if (startNextTime) {
			startNextTime = false;
		}
		return result;
	}

	public void startNextTime() {
		startNextTime = true;
	}
	
	public void setGameStatus(int status){
		onMoneyChangedListener.onGameStatusChanged(status);
	}
}
