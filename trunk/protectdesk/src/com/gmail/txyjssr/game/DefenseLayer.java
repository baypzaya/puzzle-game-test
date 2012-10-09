package com.gmail.txyjssr.game;

import android.view.MotionEvent;

import com.gmail.txyjssr.R;
import com.wiyun.engine.nodes.Director;
import com.wiyun.engine.nodes.Layer;
import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.opengl.Texture2D;
import com.wiyun.engine.types.WYPoint;

public class DefenseLayer extends Layer {
	Sprite tower;

	public DefenseLayer() {
		Texture2D tex = Texture2D.makePNG(R.drawable.tower);
		tower = Sprite.make(tex);
		tower.scale(0.3f);
		setTouchEnabled(true);
	}

	@Override
	public boolean wyTouchesBegan(MotionEvent event) {
		WYPoint loc = Director.getInstance().convertToGL(event.getX(), event.getY());
		addChild(tower);
		tower.setPosition(loc);
		return true;
	}

	@Override
	public boolean wyTouchesEnded(MotionEvent event) {
		return true;
	}

	@Override
	public boolean wyTouchesMoved(MotionEvent event) {
		WYPoint loc = Director.getInstance().convertToGL(event.getX(), event.getY());
		tower.setPosition(loc);
		return true;
	}
}
