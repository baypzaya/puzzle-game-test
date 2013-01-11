package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

public class Node extends View{

	private final int padding = 16;
	private final int roundStrokeWidth = 2;
	private final int titleSize = 40;

	public String title;
	public int type;
	private Paint tilePaint;
	private Paint roundPaint;

	public Node(Context context) {
		super(context);
		
		roundPaint = new Paint();
		roundPaint.setColor(Color.RED);
		roundPaint.setAntiAlias(true);
		roundPaint.setStyle(Paint.Style.STROKE);
		roundPaint.setStrokeWidth(roundStrokeWidth);

		tilePaint = new Paint();
		tilePaint.setAntiAlias(true);
		tilePaint.setStrokeCap(Paint.Cap.ROUND);
		tilePaint.setTextSize(titleSize);
		tilePaint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.ITALIC));

	}

	private Bitmap getNodeBitmap(String title) {

		Rect roundRect = new Rect();
		tilePaint.getTextBounds(title, 0, title.length(), roundRect);

		int titleWidth = roundRect.width();
		int titleHeight = roundRect.height();

		int roundWidth = titleWidth + 2*padding;
		int roundHeight = titleHeight + 2*padding;

		Bitmap bitmap = Bitmap.createBitmap(roundWidth + 2*roundStrokeWidth, roundHeight + 2*roundStrokeWidth, Bitmap.Config.ARGB_8888);
		int centerX = bitmap.getWidth()/2;
		int centerY = bitmap.getHeight()/2;
		
		Canvas canvas = new Canvas(bitmap);
		
		canvas.drawColor(Color.CYAN);
		
		
		RectF rectF = new RectF(roundStrokeWidth, roundStrokeWidth, roundWidth+roundStrokeWidth, roundHeight+roundStrokeWidth);
		canvas.drawRoundRect(rectF, 4, 4,roundPaint);
		
		canvas.translate(centerX - roundRect.centerX(), centerY-roundRect.centerY());
		canvas.drawText(title, 0, 0, tilePaint);
		return bitmap;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Log.i("yujsh log", "onDraw:");
		canvas.drawColor(Color.WHITE);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap bitmap = getNodeBitmap("中文测试京东分开就撒的");
		canvas.drawBitmap(bitmap, 100, 100, paint);
	}

}
