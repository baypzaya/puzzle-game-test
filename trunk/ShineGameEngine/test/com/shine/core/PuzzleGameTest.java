package com.shine.core;

import java.util.Random;

import javax.microedition.lcdui.game.Sprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;

import com.shine.core.GameView;
import com.shine.core.utils.BitmapUtils;
import com.shine.core.utils.DrawUtils;

public class PuzzleGameTest implements GameView {

	private int row = 3;
	private int col = 4;
	private Bitmap bitmap;
	private int spriteWidth;
	private int spriteHeight;

	private Context mContext;
	private int screenWidth;
	private int screenHeight;

	private Sprite[] sprites;
	private int mSeletedSpriteIndex = -1;
	private Boolean mIsSuccess = false;
	private boolean mIsStart = true;
	private Uri mBitmapUri;

	public PuzzleGameTest(Context context) {
		mContext = context;
		String uriStr = "file:///mnt/sdcard/PNG_477K.png";
		mBitmapUri = Uri.parse(uriStr);
	}

	private void creatSprites() {
		int count = row * col;
		sprites = new Sprite[count];
		int[] frames = getRandomArray();
		for (int i = 0; i < count; i++) {
			Sprite sprite = creatSrpites(i);
			sprite.setFrame(frames[i]);
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
		Log.i("yujsh log", "position x:" + x + " position y:" + y);
		s.setPosition(x, y);
		s.setFrame(index);
		return s;
	}

	private Bitmap getBitmap(Uri uri) {
		Bitmap bitmap = BitmapUtils.getBitmapFromUri(mContext, uri);
		bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, screenHeight, true);
		return bitmap;
	}

	@Override
	public void onDraw(Canvas canvas) {

		// clear screen
		Paint mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);
		canvas.drawRect(0, 0, screenWidth, screenHeight, mPaint);

		if (mIsStart) {
			canvas.drawBitmap(bitmap, 0, 0, null);
			return;
		}

		if (mIsSuccess) {
			showSuccesAnimal(canvas);
			return;
		}

		// draw sprites
		int count = sprites.length;
		// Log.i("yujsh log","count:"+count);
		for (int i = 0; i < count; i++) {
			sprites[i].paint(canvas);
			// Log.i("yujsh log","position x:"+sprites[i].getX()+" position y:"+sprites[i].getY());
			int x = sprites[i].getX();
			int y = sprites[i].getY();
			int w = sprites[i].getWidth();
			int h = sprites[i].getHeight();
			mPaint.setColor(Color.BLACK);
			DrawUtils.drawRect(canvas, x, y, w, h, mPaint);
		}
		if (mSeletedSpriteIndex >= 0) {
			int x = sprites[mSeletedSpriteIndex].getX();
			int y = sprites[mSeletedSpriteIndex].getY();
			int w = sprites[mSeletedSpriteIndex].getWidth();
			int h = sprites[mSeletedSpriteIndex].getHeight();
			mPaint.setColor(Color.RED);
			DrawUtils.drawRect(canvas, x, y, w, h, mPaint);
		}

	}

	private void showSuccesAnimal(Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, 0, null);
		mIsStart = true;
		mIsSuccess = false;
		this.creatSprites();
		// Paint mPaint = new Paint();
		// mPaint.setAntiAlias(true);
		// mPaint.setColor(Color.BLACK);
		// mPaint.setTextSize(15);
		// canvas.drawText("成 功",screenWidth/2, screenHeight/2, mPaint);
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
	public boolean onTouch(MotionEvent event) {
		if (mIsSuccess) {
			return false;
		}

		if (mIsStart) {
			mIsStart = false;
			return false;
		}

		int preSelectedIndex = mSeletedSpriteIndex;
		float x = event.getX();
		float y = event.getY();
		int spriteRow = (int) (y / spriteHeight);
		int spriteCol = (int) (x / spriteWidth);
		mSeletedSpriteIndex = spriteRow * col + spriteCol;
		if (mSeletedSpriteIndex > sprites.length - 1) {
			mSeletedSpriteIndex = -1;
		}

		if (preSelectedIndex != -1 && mSeletedSpriteIndex != -1) {
			int preFrameIndex = sprites[preSelectedIndex].getFrame();
			int curFrameIndex = sprites[mSeletedSpriteIndex].getFrame();
			sprites[preSelectedIndex].setFrame(curFrameIndex);
			sprites[mSeletedSpriteIndex].setFrame(preFrameIndex);

			mSeletedSpriteIndex = -1;
		}

		isSuccess();
		return false;
	}

	@Override
	public void onSurfaceSizeChanged(int width, int height) {
		Log.i("yujsh log", "test width:" + width);
		Log.i("yujsh log", "test height:" + height);
		screenWidth = width;
		screenHeight = height;
		bitmap = getBitmap(mBitmapUri);
		creatSprites();
	}

	private void isSuccess() {
		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				synchronized (mIsSuccess) {
					int count = sprites.length;
					for (int i = 0; i < count; i++) {
						int frameIndex = sprites[i].getFrame();
						if (i != frameIndex) {
							return;
						}
					}
					mIsSuccess = true;
				}
			}
		});

		th.start();

	}

	private int[] getRandomArray() {
		int length = row * col;
		int[] array = new int[length];
		Random r = new Random(length);
		for (int i = 0; i < length; i++) {
			array[i] = i;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 50; i++) {
			int a = r.nextInt() % length;
			int b = r.nextInt() % length;
			if (a < 0) {
				a = -a;
			}
			if (b < 0) {
				b = -b;
			}
			int temp = array[a];
			array[a] = array[b];
			array[b] = temp;
			sb.append(a + " ");
			sb.append(b + " ");
		}
		Log.i("yujsh log", "random:" + sb.toString());
		return array;
	}

}
