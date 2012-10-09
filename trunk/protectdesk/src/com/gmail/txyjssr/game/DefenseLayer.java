package com.gmail.txyjssr.game;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;

import com.gmail.txyjssr.R;
import com.wiyun.engine.nodes.Director;
import com.wiyun.engine.nodes.Layer;
import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.opengl.Texture2D;
import com.wiyun.engine.types.WYPoint;

public class DefenseLayer extends Layer {
	GameScene gameScene;
	Texture2D texTower;
	Texture2D texPoint;
	
	
	WYPoint currentPointer;
	Sprite spritePoint;

	public DefenseLayer() {
		texTower = Texture2D.makePNG(R.drawable.tower);
		texPoint = Texture2D.makePNG(R.drawable.r1);
		spritePoint = Sprite.make(texPoint);
		setTouchEnabled(true);
	}

	@Override
	public boolean wyTouchesBegan(MotionEvent event) {
		currentPointer = Director.getInstance().convertToGL(event.getX(), event.getY());
		spritePoint.setPosition(currentPointer);
		addChild(spritePoint);
		return true;
	}

	@Override
	public boolean wyTouchesEnded(MotionEvent event) {
		removeChild(spritePoint, true);
		Sprite tower = creatTower();
		addChild(tower);
		tower.setPosition(currentPointer);
		GameData.getInstance().addTower(tower);
		return true;
	}

	@Override
	public boolean wyTouchesMoved(MotionEvent event) {
		currentPointer = Director.getInstance().convertToGL(event.getX(), event.getY());
		spritePoint.setPosition(currentPointer);
		return true;
	}
	
	public Sprite creatTower(){
		Sprite tower = Sprite.make(texTower);
		tower.scale(0.3f);
		return tower;
	}
}
