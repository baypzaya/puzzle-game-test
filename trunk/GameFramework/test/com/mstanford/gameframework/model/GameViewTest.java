package com.mstanford.gameframework.model;

import javax.microedition.lcdui.game.Sprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;

import com.mstanford.gameframework.utils.BitmapUtils;

public class GameViewTest implements GameView {
	
	private int row;
	private int col;
	private Bitmap bitmap;
	private int sWidth;
	private int sHeight;
	
	private Context mContext;
	
	private Sprite[] sprites;	
	public GameViewTest(Context context){
		mContext=context;
		bitmap=getBitmap();
		creatSprites();		

	}
	private void creatSprites() {
		int count = row*col;
		sprites=new Sprite[count];		
		for(int i=0;i<count;i++){
			Sprite sprite=creatSrpites(i);
			sprites[i]=sprite;
		}		
	}
	private Sprite creatSrpites(int index) {
		int sWidth=800/col;
		int sHeight=480/row;
		Sprite s = new Sprite(bitmap, 800/col, 480/row);
		int i=index%col;
		int y=i*sHeight;
		int j=index/col;
		int x=j*sWidth;
		s.setPosition(x, y);
		return s;
	}
	private Bitmap getBitmap() {
		String uriStr="";
		Uri uri = Uri.parse(uriStr);
		Bitmap bitmap=BitmapUtils.getBitmapFromUri(mContext, uri);
		bitmap=Bitmap.createScaledBitmap(bitmap, 800, 480, true);
		return bitmap;
	}	
	
	@Override
	public void onDraw(Canvas canvas) {
		int count=sprites.length;
		for(int i=0;i<count;i++){
			sprites[i].paint(canvas);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode) {
		return false;
	}

	@Override
	public boolean onKeyUp(int keyCode) {
		return false;
	}

	@Override
	public void reCycle() {

	}

	@Override
	public void refurbish() {

	}

	@Override
	public boolean onTouch(int keyCode) {
		return false;
	}

}
