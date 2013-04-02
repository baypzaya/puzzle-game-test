package com.gmail.txyjssr.game.data;

import com.gmail.txyjssr.R;
import com.wiyun.engine.nodes.MenuItemSprite;
import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.opengl.Texture2D;

public class Tower {
	public int type;
	public float speed;
	public float scope;
	public Sprite spriteTower;
	public int spend;
	
	Texture2D texTower;
	
	public long preShotTime = 0;
	
	public Tower(int type){
		switch (type) {
		case 1:
			texTower = Texture2D.makePNG(R.drawable.tower1);
			break;
		case 2:
			texTower = Texture2D.makePNG(R.drawable.tower2);
			break;
		case 3:
			texTower = Texture2D.makePNG(R.drawable.tower3);
			break;
		case 4:
			texTower = Texture2D.makePNG(R.drawable.tower4);
			break;
		}
		
		spriteTower = Sprite.make(texTower);
		this.type = type;
		speed = 500;
		scope = 200;
		spend = getTowerSpend(type);
	}
	
	public boolean canShot(){
		long currentTime = System.currentTimeMillis();
		if(currentTime - preShotTime > speed){
			preShotTime = currentTime;
			return true;
		}
		return false;
	}
	
	public static int getTowerSpend(int type){
		int spend = 0;
		switch (type) {
		case 1:
			spend = 50;
			break;
		case 2:
			spend = 100;
			break;
		case 3:
			spend = 200;
			break;
		case 4:
			spend = 500;
			break;
		}
		return spend;
	}
}
