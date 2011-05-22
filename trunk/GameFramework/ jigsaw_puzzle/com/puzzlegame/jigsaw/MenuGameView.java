package com.puzzlegame.jigsaw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.mstanford.gameframework.GameGlobal;
import com.mstanford.gameframework.model.BaseGameView;
import com.mstanford.gameframework.model.GameView;
import com.mstanford.gameframework.utils.DrawUtils;

public class MenuGameView extends BaseGameView implements GameView {

	public MenuGameView(Context context) {
		super(context);
	}

	@Override
	public void onDraw(Canvas canvas) {
		if(mScreenWidth>0&&mScreenHeight>0){
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setTextSize(GameGlobal.TextSize);
			DrawUtils.drawString(canvas, "menu", mScreenWidth,mScreenHeight, paint);
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
