package com.gmail.txyjssr.game;

import android.util.Log;
import android.view.MotionEvent;

import com.gmail.txyjssr.R;
import com.gmail.txyjssr.game.data.GameData;
import com.wiyun.engine.nodes.ColorLayer;
import com.wiyun.engine.nodes.Director;
import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.types.WYColor3B;
import com.wiyun.engine.types.WYPoint;
import com.wiyun.engine.utils.TargetSelector;

public class TowerSelectLayer extends ColorLayer {
	GameData mGameData = GameData.getInstance();
	WYPoint currentPoint;
	private float itemWidth;
	private float itemHeight;
	
	public TowerSelectLayer(){
		Sprite item = Sprite.make(R.drawable.tower);
		itemWidth = item.getWidth()*0.3f;
		itemHeight = item.getHeight()*0.3f;
	}
	
	public void showSeleteTower(WYPoint point){
		removeAllChildren(true);
		int[] towerTypes = mGameData.getCurrentUseableTower();
		
		for(int type : towerTypes){
			switch(type){
				case 1:break;
				case 2:break;
				case 3:break;
				case 4:break;
			}
			Sprite item = Sprite.make(R.drawable.tower);
			item.setScale(0.3f);
			item.setAnchor(0, 0);
			int childCount = getChildCount();
			if(childCount > 0 ){
				item.setPosition(childCount*itemWidth, 0);
			}
			addChild(item);
			
		}
		float contentWidth = getChildCount()*itemWidth;
		setContentSize(contentWidth, itemHeight);
		WYPoint tpoint = mGameData.getDefenseLocation(point);
		setPosition(tpoint.x - contentWidth/2,tpoint.y-itemHeight/2);
		setTouchEnabled(true);
	}
	
	@Override
	public boolean wyTouchesBegan(MotionEvent event) {
		currentPoint = Director.getInstance().convertToGL(event.getX(), event.getY());
		return true;
	}

	@Override
	public boolean wyTouchesEnded(MotionEvent event) {
		
		return true;
	}

	@Override
	public boolean wyTouchesMoved(MotionEvent event) {
		currentPoint = Director.getInstance().convertToGL(event.getX(), event.getY());
		return true;
	}
	
	public void selectedTower(int type){
		Log.i("yujsh log","selectType:"+type);
	}
}
