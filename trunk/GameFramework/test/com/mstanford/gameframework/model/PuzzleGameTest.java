package com.mstanford.gameframework.model;

import javax.microedition.lcdui.game.Sprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;

import com.mstanford.gameframework.utils.BitmapUtils;
import com.mstanford.gameframework.utils.DrawUtils;

public class PuzzleGameTest implements GameView {

	private int row = 4;
	private int col = 6;
	private Bitmap bitmap;
	private int spriteWidth;
	private int spriteHeight;

	private Context mContext;
	private int screenWidth = 480;
	private int screenHeight = 320;

	private Sprite[] sprites;

	public PuzzleGameTest(Context context) {
		mContext = context;
		bitmap = getBitmap();
		creatSprites();

	}

	private void creatSprites() {
		int count = row * col;
		sprites = new Sprite[count];
		for (int i = 0; i < count; i++) {
			Sprite sprite = creatSrpites(i);
			sprites[i] = sprite;
		}
	}

	private Sprite creatSrpites(int index) {
		spriteWidth = screenWidth / col;
		spriteHeight = screenHeight / row;
		Sprite s = new Sprite(bitmap, spriteWidth, spriteHeight);
		int i = index % col;
		int x = i * spriteWidth;
		int j = index / col;
		int y = j * spriteHeight;
		Log.i("yujsh log","position x:"+x+" position y:"+y);
		s.setPosition(x, y);
		s.setFrame(index);
		return s;
	}

	private Bitmap getBitmap() {
		String uriStr = "file:///sdcard/aa.jpg";
		Uri uri = Uri.parse(uriStr);
		Bitmap bitmap = BitmapUtils.getBitmapFromUri(mContext, uri);		
		bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, screenHeight, true);
		return bitmap;
	}

	@Override
	public void onDraw(Canvas canvas) {
		//clear screen
		Paint mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);		
		canvas.drawRect(0, 0, screenWidth, screenHeight, mPaint);
		
		//draw sprites
		int count = sprites.length;
//		Log.i("yujsh log","count:"+count);
		for (int i = 0; i < count; i++) {
			sprites[i].paint(canvas);
//			Log.i("yujsh log","position x:"+sprites[i].getX()+" position y:"+sprites[i].getY());
			int x=sprites[i].getX();
			int y=sprites[i].getY();
			int w=sprites[i].getWidth();
			int h=sprites[i].getHeight();
			DrawUtils.drawRect(canvas, x, y, w, h, mPaint);
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
