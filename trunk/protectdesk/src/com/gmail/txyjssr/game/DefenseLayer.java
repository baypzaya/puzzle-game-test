package com.gmail.txyjssr.game;

import android.util.Log;

import com.gmail.txyjssr.R;
import com.gmail.txyjssr.game.data.GameData;
import com.gmail.txyjssr.game.data.Tower;
import com.wiyun.engine.nodes.Layer;
import com.wiyun.engine.nodes.MenuItemSprite;
import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.opengl.Texture2D;
import com.wiyun.engine.types.WYPoint;
import com.wiyun.engine.utils.TargetSelector;

public class DefenseLayer extends Layer {
	
	Texture2D texPoint;
	GameData mGameData;
//	Layer selectTowerLayer;
	

	// WYPoint currentPointer;
	Sprite spritePoint;

	public DefenseLayer() {
		
		texPoint = Texture2D.makePNG(R.drawable.r1);
		spritePoint = Sprite.make(texPoint);
		
		mGameData = GameData.getInstance();
		
		// setTouchEnabled(true);
	}

	// @Override
	// public boolean wyTouchesBegan(MotionEvent event) {
	// currentPointer = Director.getInstance().convertToGL(event.getX(),
	// event.getY());
	// spritePoint.setPosition(currentPointer);
	// addChild(spritePoint);
	// return true;
	// }
	//
	// @Override
	// public boolean wyTouchesEnded(MotionEvent event) {
	// removeChild(spritePoint, true);
	// Sprite tower = creatTower();
	// addChild(tower);
	// tower.setPosition(currentPointer);
	// GameData.getInstance().addTower(tower);
	// return true;
	// }
	//
	// @Override
	// public boolean wyTouchesMoved(MotionEvent event) {
	// currentPointer = Director.getInstance().convertToGL(event.getX(),
	// event.getY());
	// spritePoint.setPosition(currentPointer);
	// return true;
	// }

	public Tower creatTower(int type) {
		Tower tower = new Tower(type);
		return tower;
	}

	public void createSpritePoint(WYPoint point) {
		spritePoint.setPosition(point);
		addChild(spritePoint);
	}

	public void moveSpritePoint(WYPoint point) {
		spritePoint.setPosition(point);
	}

	public Tower showTower(int type, WYPoint point) {
		WYPoint tpoint = mGameData.getDefenseLocation(point);
		removeChild(spritePoint, true);
		Tower tower = creatTower(type);
		addChild(tower.spriteTower);
		tower.spriteTower.setPosition(tpoint);
		return tower;
	}
	
	
	
}
