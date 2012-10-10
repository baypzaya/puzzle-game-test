package com.gmail.txyjssr.game.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.types.WYRect;

public class GameData {
	public static GameData sGameData;
	
	public Hashtable<Integer,Enemy> enemyMap = new Hashtable<Integer,Enemy>();
	public  Hashtable<Integer,Tower> towerMap = new Hashtable<Integer,Tower>();

	public static GameData getInstance() {
		if (sGameData == null) {
			sGameData = new GameData();
		}
		return sGameData;
	}
	
	public void addEnemy(int pointer,Enemy enemy){
		enemyMap.put(pointer,enemy);
	}
	
	public void addTower(int pointer,Tower tower){
		towerMap.put(pointer,tower);
	}
	
	public Enemy getEnemyByScope(WYRect rect){
		Enemy enemy = null;
		Collection<Enemy> enemis= enemyMap.values();
		for(Enemy temEnemy:enemis){
			if(rect.containsPoint(temEnemy.getPositionX(),temEnemy.getPositionY())){
				enemy = temEnemy;
			}
		}
		return enemy;
	}

	public void removeEnemy(int pointer) {
		enemyMap.remove(pointer);
	}
}
