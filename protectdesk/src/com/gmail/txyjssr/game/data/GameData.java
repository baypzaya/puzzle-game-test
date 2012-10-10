package com.gmail.txyjssr.game.data;

import java.util.ArrayList;
import java.util.List;

import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.types.WYRect;

public class GameData {
	public static GameData sGameData;

	public List<Enemy> enemyList = new ArrayList<Enemy>();
	public List<Tower> towerList = new ArrayList<Tower>();

	public static GameData getInstance() {
		if (sGameData == null) {
			sGameData = new GameData();
		}
		return sGameData;
	}
	
	public void addEnemy(Enemy enemy){
		enemyList.add(enemy);
	}
	
	public void addTower(Tower tower){
		towerList.add(tower);
	}
	
	public Enemy getEnemyByScope(WYRect rect){
		Enemy enemy = null;
		for(Enemy temEnemy:enemyList){
			if(rect.containsPoint(temEnemy.getPositionX(),temEnemy.getPositionY())){
				enemy = temEnemy;
			}
		}
		return enemy;
	}

	public void removeEnemy(Enemy enemy) {
		if(enemy!=null){
			enemyList.remove(enemy);
		}
	}
}
