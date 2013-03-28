package com.gmail.txyjssr.game.data;

import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.opengl.Texture2D;
import com.wiyun.engine.types.WYRect;

public class Enemy extends Sprite {
	private long life; // 1k - 100k
	private float speed;// px/s
	private OnEnemyStateChangedListener lifeChangedListener;
	private int pathIndex = 0;
	private int destroyValue = 10;

	protected Enemy(Texture2D tex) {
		super(tex);
		life = 1000;
		speed = 100;
	}
	
	public Enemy(Texture2D tex,WYRect rect) {
		super(tex,rect);
		setAnchorX(0);
		life = 1000;
		speed = 100;
	}

	public long getLife() {
		return life;
	}

	public void setLife(long life) {
		if(this.life != life){
			this.life = life;
			if(lifeChangedListener != null){
				lifeChangedListener.onLifeChanged(this, life);
			}
		}
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public OnEnemyStateChangedListener getLifeChangedListener() {
		return lifeChangedListener;
	}

	public void setLifeChangedListener(OnEnemyStateChangedListener lifeChangedListener) {
		this.lifeChangedListener = lifeChangedListener;
	}
	
	public void setPathIndex(int index){
		this.pathIndex = index;
	}
	
	public int getPathIndex(){
		return this.pathIndex;
	}

	public int getDestroyValue() {
		return destroyValue;
	}
	
}
