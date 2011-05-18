package com.mstanford.gameframework.model;

import javax.microedition.lcdui.game.Sprite;

import android.graphics.Bitmap;

public class BlockSprite extends Sprite {
	
	int mIndex;
	boolean mIsRight; 

	public BlockSprite(Bitmap image, int frameWidth, int frameHeight) {
		super(image, frameWidth, frameHeight);		
	}
	
	

}
