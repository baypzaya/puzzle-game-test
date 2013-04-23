package com.gmail.txyjssr.game;

import android.graphics.Typeface;

import com.gmail.txyjssr.R;
import com.gmail.txyjssr.game.data.GameData;
import com.wiyun.engine.nodes.Button;
import com.wiyun.engine.nodes.Director;
import com.wiyun.engine.nodes.Label;
import com.wiyun.engine.nodes.Layer;
import com.wiyun.engine.nodes.Menu;
import com.wiyun.engine.nodes.MenuItemSprite;
import com.wiyun.engine.types.WYSize;
import com.wiyun.engine.utils.TargetSelector;

public class GameStatusLayer extends Menu {
	private Label label;
	
	
	
	public GameStatusLayer(){
		TargetSelector ts = new TargetSelector(this, "resetGame(int)", new Object[] { 1 });
		MenuItemSprite item = (MenuItemSprite.make(R.drawable.r1, 0, ts));
		addChild(item);
		item.setClickScale(1.5f);
		item.autoRelease();
		WYSize size = Director.getInstance().getWindowSize();
		item.setPosition(size.width/2,size.height/3);
		
		label = Label.make("", 60,Typeface.ITALIC);
		label.setPosition(size.width/2,size.height/2);
		addChild(label);
		
		setContentSize(size.width, size.height);
		setPosition(0,0);
	}

	public void setStatus(int status) {
		String message = null;
		switch(status){
		case GameData.STATUS_OVER:
			message="game over";
			break;
		case GameData.STATUS_PAUSE:
			message="game pause";
			break;
		case GameData.STATUS_SUCCESS:
			message="game success";
			break;
		}
		label.setText(message);
	}
	
	public void resetGame(int i){
		GameData.getInstance().resetData();
	}
	
	
}
