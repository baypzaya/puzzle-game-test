package com.gmail.txyjssr.game.data;

import com.gmail.txyjssr.R;
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
		texTower = Texture2D.makePNG(R.drawable.tower);
		spriteTower = Sprite.make(texTower);
		spriteTower.scale(0.3f);
		this.type = type;
		speed = 500;
		scope = 200;
		spend = 50;
	}
	
	public boolean canShot(){
		long currentTime = System.currentTimeMillis();
		if(currentTime - preShotTime > speed){
			preShotTime = currentTime;
			return true;
		}
		return false;
	}
}
