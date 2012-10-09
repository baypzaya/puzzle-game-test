package com.gmail.txyjssr.game;

import com.gmail.txyjssr.R;
import com.wiyun.engine.actions.MoveTo;
import com.wiyun.engine.nodes.Layer;
import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.opengl.Texture2D;

public class BulletsLayer extends Layer{
	
	Texture2D texBullet;
	
	public BulletsLayer(){
		texBullet = Texture2D.makePNG(R.drawable.r1);
	}
	
	public Sprite creatBullet(){
		Sprite bullet = Sprite.make(texBullet);
//		bullet.scale(0.2f);
		return bullet;
	}

	public void addBullet(float startX, float startY, float endX, float endY) {
		Sprite bullet = creatBullet();
		addChild(bullet);
		
		MoveTo moveTo = MoveTo.make(1, startX, startY, endX, endY);
		bullet.runAction(moveTo);
	}
	
	
	
}
