package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.view.View;

public class LinkView extends View {
	private final int strokeWidth = 2;

	public float startX;
	public float startY;
	public float endX;
	public float endY;

	private Paint paint;

	public LinkView(Context context) {
		super(context);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Style.STROKE); 
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);
		

		int padding = strokeWidth / 2;
		if((startY<endY&&startX<endX)||(startY>endY&&startX>endX)){
			drawLineByPath(canvas,0 + padding, 0 + padding, getMeasuredWidth() - padding, getMeasuredHeight() - padding, paint);
		}else{
			drawLineByPath(canvas,0 + padding, getMeasuredHeight() - padding, getMeasuredWidth() - padding, 0 + padding, paint);
		}
	}
	
	private void drawLineByPath(Canvas canvas,float startX, float startY, float stopX, float stopY, Paint paint){
		float controlX = (startX+stopX)/2;
		 Path path = new Path();  
         path.moveTo(startX,startY);  
         path.cubicTo(controlX, startY,   
        		 controlX, stopY,   
                 stopX, stopY);  
         canvas.drawPath(path, paint);  
//         canvas.drawLine(mPoints[C_START].x,mPoints[C_START].y,   
//                 mPoints[C_CONTROL_1].x, mPoints[C_CONTROL_1].y, paint);  
//         canvas.drawLine(mPoints[C_END].x, mPoints[C_END].y,   
//                 mPoints[C_CONTROL_2].x, mPoints[C_CONTROL_2].y, paint); 
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension((int) Math.abs(endX - startX) + strokeWidth, (int) Math.abs(endY - startY) + strokeWidth);
	}

	public void setLink(float x, float y, float x2, float y2) {
		startX = x;
		startY = y;
		endX = x2;
		endY = y2;
	}

}
