package com.gmail.txyjssr.mindmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

public class Node {

	private final int padding = 16;
	private final int roundStrokeWidth = 2;

	public String title;
	public int type;
	private Paint tilePaint;
	private Paint roundPaint;

	public Node() {
		roundPaint = new Paint();
		roundPaint.setColor(Color.RED);
		roundPaint.setAntiAlias(true);
		roundPaint.setStyle(Paint.Style.STROKE);
		roundPaint.setStrokeWidth(roundStrokeWidth);

		tilePaint = new Paint();
		tilePaint.setAntiAlias(true);
		// tilePaint.setStrokeWidth(5);
		tilePaint.setStrokeCap(Paint.Cap.ROUND);
		tilePaint.setTextSize(40);
//		tilePaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));

	}

	public Bitmap getNodeBitmap(String title) {

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

}
