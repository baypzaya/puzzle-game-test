package com.gmail.txyjssr.game;

import android.graphics.Typeface;

import com.wiyun.engine.nodes.Director;
import com.wiyun.engine.nodes.Label;
import com.wiyun.engine.nodes.Layer;
import com.wiyun.engine.types.WYSize;

public class GameStatusLayer extends Layer {
	private Label label;
	
	
	public GameStatusLayer(){
		WYSize size = Director.getInstance().getWindowSize();
		label = Label.make("game over", 60,Typeface.ITALIC);
		label.setPosition(size.width/2,size.height/2);
		addChild(label);
	}

	public void setStatus(int statusOver) {
		
	}
	
	
}
