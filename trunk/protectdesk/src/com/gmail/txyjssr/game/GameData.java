package com.gmail.txyjssr.game;

import java.util.ArrayList;
import java.util.List;

import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.types.WYRect;

public class GameData {
	public static GameData sGameData;

	List<Sprite> enemyList = new ArrayList<Sprite>();
	List<Sprite> towerList = new ArrayList<Sprite>();

	public static GameData getInstance() {
		if (sGameData == null) {
			sGameData = new GameData();
		}
		return sGameData;
	}
	
	public void addEnemy(Sprite enemy){
		enemyList.add(enemy);
	}
	
	public void addTower(Sprite tower){
		towerList.add(tower);
	}
	
	public Sprite getEnemyByScope(WYRect rect){
		Sprite enemy = null;
		for(Sprite sprite:enemyList){
			if(rect.containsPoint(sprite.getPositionX(),sprite.getPositionY())){
				enemy = sprite;
			}
		}
		return enemy;
	}
}
