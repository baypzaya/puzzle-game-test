package com.gmail.txyjssr.game;

import android.graphics.Typeface;

import com.gmail.txyjssr.game.data.GameData;
import com.wiyun.engine.nodes.Director;
import com.wiyun.engine.nodes.Label;
import com.wiyun.engine.nodes.Layer;
import com.wiyun.engine.types.WYSize;

public class GameStatusLayer extends Layer {
	private Label label;
	
	
	public GameStatusLayer(){
		
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
		WYSize size = Director.getInstance().getWindowSize();
		label = Label.make(message, 60,Typeface.ITALIC);
		label.setPosition(size.width/2,size.height/2);
		addChild(label);
	}
	
	
}
