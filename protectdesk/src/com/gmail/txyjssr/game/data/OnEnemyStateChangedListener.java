package com.gmail.txyjssr.game.data;


public interface OnEnemyStateChangedListener {
	public void onLifeChanged(Enemy target,long life);
	public void onCrossed(Enemy target);
}
