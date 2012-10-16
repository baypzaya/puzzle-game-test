package com.gmail.txyjssr.game.data;

import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.opengl.Texture2D;

public class Tower extends Sprite{
	public int type;
	public float speed;
	public float scope;
	public Sprite spriteTower;
	
	public long preShotTime = 0;
	
	public Tower(Texture2D tex){
		super(tex);
		
		type = 0;
		speed = 500;
		scope = 200;
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
