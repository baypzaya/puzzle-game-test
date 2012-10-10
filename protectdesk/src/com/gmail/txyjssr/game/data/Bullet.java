package com.gmail.txyjssr.game.data;

import com.wiyun.engine.actions.Action;
import com.wiyun.engine.actions.Action.Callback;
import com.wiyun.engine.nodes.Node;

public class Bullet implements Callback{
	private Enemy enemy;
	private long power; 
	private OnShotListener shotListener;

	public Bullet(Enemy enemy) {
		this.enemy = enemy;
		power = 300;
	}

	public long getPower() {
		return power;
	}

	public void setPower(long power) {
		this.power = power;
	}

	public Enemy getEnemy() {
		return enemy;
	}

	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}

	public void setShotListener(OnShotListener shotListener) {
		this.shotListener = shotListener;
	}

	public void shotEnemy() {
		if(shotListener != null){
			shotListener.onShot(enemy, this);
		}
	}

	@Override
	public void onStart(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop(int pointer) {
		Action action = Action.from(pointer);
		Node bullet = action.getTarget();
		bullet.stopAllActions();
		bullet.getParent().removeChild(bullet, true);
		shotEnemy();
	}

	@Override
	public void onUpdate(int arg0, float arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
