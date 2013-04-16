package com.gmail.txyjssr.mindmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.view.View;
import android.view.View.MeasureSpec;

public class BitmapUtils {
	private static final float MAX_BITMAP_WIDTH = 2048;
	private static final float MAX_BITMAP_HEIGHT = 2048;

	public static Bitmap convertViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();

		return bitmap;
	}

	public static Bitmap convertViewToBitmap(View view, int bitmapWidth, int bitmapHeight) {
		float scaleX = 1;
		float scaleY = 1;
		if (bitmapWidth > MAX_BITMAP_WIDTH) {
			scaleX = MAX_BITMAP_WIDTH / bitmapWidth;
		}

		if (bitmapHeight > MAX_BITMAP_HEIGHT) {
			scaleY = MAX_BITMAP_HEIGHT / bitmapHeight;
		}

		float scale = scaleX < scaleY ? scaleX : scaleY;
		Bitmap bitmap = Bitmap.createBitmap((int) (bitmapWidth * scale), (int) (bitmapHeight * scale),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);
		canvas.scale(scale, scale);
		view.draw(canvas);

		return bitmap;
	}
	
	public static String createMindMap(Context context,long mindMapId){
		MindMapManager mmManager = new MindMapManager();
		MindMap tempMindMap = mmManager.getMindMapBy(mindMapId);
		MindMapView tempMMView = new MindMapView(context);
		
		Node nodeTop = tempMindMap.getTopNode();
		Node nodeBottom = tempMindMap.getBottomNode();
		Node nodeLeft = tempMindMap.getLeftNode();
		Node nodeRight = tempMindMap.getRightNode();
		
		float mindMapTop = nodeTop.y;
		float mindMapBottom = nodeBottom.y;
		float mindMapLeft = nodeLeft.x;
		float mindMapRight = nodeRight.x;
		
		int width = (int)mindMapRight - (int)mindMapLeft;
		int height = (int)mindMapBottom - (int)mindMapTop;
		
		if(width <= 800){
			mindMapLeft = nodeLeft.x - (840-width)/2;
			width = 840;
		}else {
			width = (int)mindMapRight - (int)mindMapLeft+40;
			mindMapLeft = nodeLeft.x-20;
		}
		
		if(height <= 800){
			mindMapTop = nodeTop.y - (840-height)/2;
			height = 840;
		}else{
			height = (int)mindMapBottom - (int)mindMapTop+40;
			mindMapTop = nodeTop.y-20;
		}
		
		List<Node> nodeList = tempMindMap.getNodes();
		for (Node node : nodeList) {
			node.x = node.x - mindMapLeft;
			node.y = node.y - mindMapTop;
			EditTextNode nl = new EditTextNode(context);
			nl.setNode(node);
			tempMMView.addView(nl);
			
			if (!node.isRootNode) {
				LinkView lv = new LinkView(tempMMView, node);
				tempMMView.addView(lv, 0);
			}
		}
		
		tempMMView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		
		for (Node node : nodeList) {
			if (!node.isRootNode) {
				LinkView lv = new LinkView(tempMMView, node);
				tempMMView.addView(lv, 0);
			}
		}
		tempMMView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		
		int nodeWidth = tempMMView.findViewById((int)nodeRight._id).getMeasuredWidth();
		int nodeHeight = tempMMView.findViewById((int)nodeBottom._id).getMeasuredHeight();
		
		int realWidth = width+nodeWidth;
		int realHeight =  height+nodeHeight;
		
		tempMMView.layout(0, 0, realWidth, realHeight);
		
		Bitmap bitmap = BitmapUtils.convertViewToBitmap(tempMMView,realWidth,realHeight);
		
		
		String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.jpeg";
		File file = new File(path);		
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			OutputStream os = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, os);
			os.close();
			bitmap.recycle();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return path;
	}

}
