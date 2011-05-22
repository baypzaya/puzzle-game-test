package com.puzzlegame.jigsaw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.mstanford.gameframework.GameGlobal;
import com.mstanford.gameframework.model.BaseGameView;
import com.mstanford.gameframework.model.GameView;

public class SplashGameView extends BaseGameView implements GameView {

	public SplashGameView(Context context) {
		super(context);
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (mScreenWidth > 0 && mScreenHeight > 0) {
			Paint mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setColor(Color.BLACK);
			mPaint.setTextSize(GameGlobal.TextSize);
			canvas.drawText("splash", mScreenWidth / 2, mScreenHeight / 2, mPaint);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onKeyUp(int keyCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reCycle() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refurbish() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSurfaceSizeChanged(int width, int height) {
		setScreenSize(width, height);
	}

}
