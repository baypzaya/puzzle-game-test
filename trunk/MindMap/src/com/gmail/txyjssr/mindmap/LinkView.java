package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;

public class LinkView extends View{
	public float startX;
	public float startY;
	public float endX;
	public float endY;

	public LinkView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.YELLOW);
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(20);
		Log.i("yujsh log","getMeasuredWidth():"+getMeasuredWidth());
		Log.i("yujsh log","getMeasuredHeight():"+getMeasuredHeight());
		canvas.drawLine(getMeasuredWidth(),0,getMeasuredWidth(),getMeasuredHeight(), paint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension((int)Math.abs(endX-startX)+20, (int)Math.abs(endY-startY)+20);
	}

	public void setLink(float x, float y, float x2, float y2) {
		startX = x;
		startY = y;
		endX = x2;
		endY = y2;
	}
	
	
	
}
