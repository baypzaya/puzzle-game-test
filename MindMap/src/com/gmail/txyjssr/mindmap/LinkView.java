package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.view.View;

public class LinkView extends View {
	private final int strokeWidth = 3;

	public float parentX;
	public float parentY;
	public float childX;
	public float childY;

	private Paint paint;

	public LinkView(Context context) {
		super(context);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(context.getResources().getColor(R.color.line_color_blue));
		paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Style.STROKE); 
        
        PathEffect effect = new DashPathEffect(new float[] { 2, 2}, 1);
        paint.setPathEffect(effect);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);
		

		int padding = strokeWidth / 2;
		if((parentY<childY&&parentX<childX)||(parentY>childY&&parentX>childX)){
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
		setMeasuredDimension((int) Math.abs(childX - parentX) + strokeWidth, (int) Math.abs(childY - parentY) + strokeWidth);
	}

	public void setLink(float x, float y, float x2, float y2) {
		parentX = x;
		parentY = y;
		childX = x2;
		childY = y2;
	}

}
