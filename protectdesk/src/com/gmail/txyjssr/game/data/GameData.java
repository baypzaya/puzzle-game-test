package com.gmail.txyjssr.game.data;

import java.util.Collection;
import java.util.Hashtable;

import com.wiyun.engine.types.WYRect;

public class GameData {

	public static final int TILE_COUNT_X = 10;
	public static final int TILE_COUNT_Y = 6;
	public static GameData sGameData;

	private int currentGate = 0;
	private int[][] currentMap;
	private int[] currentPath;

	public Hashtable<Integer, Enemy> enemyMap = new Hashtable<Integer, Enemy>();
	public Hashtable<Integer, Tower> towerMap = new Hashtable<Integer, Tower>();

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
			currentPath = new int[] { 1, 0, 1, 1, 1, 2, 2, 2, 3, 2, 4, 2, 4, 3, 4, 4, 3, 4, 2, 4, 2, 5, 2, 6, 2, 7,
					3, 7, 4, 7, 4, 8, 4, 9 };

			currentGate = gate;
		}

	}
}
