package com.gmail.txyjssr.mindmap;

import com.gmail.txyjssr.mindmap.db.Node;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.Log;
import android.view.View;
import android.widget.AbsoluteLayout.LayoutParams;

public class LinkView extends View {
	private final int strokeWidth = 3;

	public EditTextNode parentEtn;
	public EditTextNode childEtn;

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

		PathEffect effect = new DashPathEffect(new float[] { 2, 2 }, 1);
		paint.setPathEffect(effect);

	}

	public LinkView(MindMapView mindMapPad, Node node) {
		this(mindMapPad.getContext());
		this.setTag(node._id);
		parentEtn = (EditTextNode) mindMapPad.findViewById((int) node.parentNode._id);
		childEtn = (EditTextNode) mindMapPad.findViewById((int) node._id);
		updateLinkColor(node);
	}

	private void updateLinkColor(Node node) {
		int layerNum = node.getLayerNum();
		switch (layerNum) {
		case 5:
			paint.setColor(getContext().getResources().getColor(R.color.node_strock_red));
			break;
		case 1:
			paint.setColor(getContext().getResources().getColor(R.color.node_strock_green));
			break;
		case 2:
			paint.setColor(getContext().getResources().getColor(R.color.node_strock_orange));
			break;
		case 4:
			paint.setColor(getContext().getResources().getColor(R.color.node_strock_blue));
			break;
		case 3:
			paint.setColor(getContext().getResources().getColor(R.color.node_strock_purpel));
			break;
		default:
			paint.setColor(getContext().getResources().getColor(R.color.node_strock_yellow));
			break;
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		updateLinkColor((Node) childEtn.getTag());
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);

		int padding = strokeWidth / 2;
		if ((parentY < childY && parentX < childX) || (parentY > childY && parentX > childX)) {
			drawLineByPath(canvas, 0 + padding, 0 + padding, getMeasuredWidth() - padding, getMeasuredHeight()
					- padding, paint);
		} else {
			drawLineByPath(canvas, 0 + padding, getMeasuredHeight() - padding, getMeasuredWidth() - padding,
					0 + padding, paint);
		}
	}

	private void drawLineByPath(Canvas canvas, float startX, float startY, float stopX, float stopY, Paint paint) {
		// float controlX = (startX + stopX) / 2;
		// Path path = new Path();
		// path.moveTo(startX, startY);
		// path.cubicTo(controlX, startY, controlX, stopY, stopX, stopY);
		// canvas.drawPath(path, paint);
		canvas.drawLine(startX, startY, stopX, stopY, paint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int pWidth = parentEtn.getMeasuredWidth();
		int pHeight = parentEtn.getMeasuredHeight();

		int cWidth = childEtn.getMeasuredWidth();
		int cHeight = childEtn.getMeasuredHeight();

		setLink(parentEtn.getPointX() + pWidth / 2, parentEtn.getPointY() + pHeight / 2, childEtn.getPointX() + cWidth
				/ 2, childEtn.getPointY() + cHeight / 2);
		int width = (int) Math.abs((childEtn.getPointX() + cWidth / 2) - (parentEtn.getPointX() + pWidth / 2))
				+ strokeWidth;
		int height = (int) Math.abs((childEtn.getPointY() + cHeight / 2) - (parentEtn.getPointY() + pHeight / 2))
				+ strokeWidth;
		setMeasuredDimension(width, height);
	}

	private void setLink(float x, float y, float x2, float y2) {
		parentX = x;
		parentY = y;
		childX = x2;
		childY = y2;
		int lx = (int) (x < x2 ? x : x2);
		int ly = (int) (y < y2 ? y : y2);

		setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, lx, ly));
	}
}
