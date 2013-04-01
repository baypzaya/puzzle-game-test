package com.gmail.txyjssr.game;

import com.gmail.txyjssr.R;
import com.gmail.txyjssr.game.data.Bullet;
import com.gmail.txyjssr.game.data.Enemy;
import com.gmail.txyjssr.game.data.OnShotListener;
import com.gmail.txyjssr.game.data.Tower;
import com.wiyun.engine.actions.MoveTo;
import com.wiyun.engine.nodes.Layer;
import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.opengl.Texture2D;

public class BulletsLayer extends Layer {

	Texture2D texBullet;

	public BulletsLayer() {
		texBullet = Texture2D.makePNG(R.drawable.r1);
	}

	public void addBullet(Tower tower, Enemy enemy, OnShotListener listener) {
		Sprite spriteBullet = Sprite.make(texBullet);
		spriteBullet.scale(0.2f);
		addChild(spriteBullet);

		MoveTo moveTo = MoveTo.make(0.2f, tower.spriteTower.getPositionX(), tower.spriteTower.getPositionY(), enemy.getPositionX(),
				enemy.getPositionY());
		spriteBullet.runAction(moveTo);

		Bullet bullet = new Bullet(enemy);
		bullet.setShotListener(listener);

		moveTo.setCallback(bullet);
	}

}
