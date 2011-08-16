package com.txyjssr.gl;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.view.MotionEvent;

public abstract class BaseScene {

	protected ArrayList<Layer> layers = new ArrayList<Layer>();
	int mWidth;
	int mHeight;

	public void setSize(int width, int height) {
		mWidth = width;
		mHeight = height;
	}	

	public abstract void onCreate();

	public abstract void onSizeChanged(int width, int height);
	
	public abstract void generate(RenderView view);

	public void render(GL10 gl) {
		for (Layer layer : layers) {
			if (!layer.isHidden()) {
				layer.render(gl);
			}
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		for (Layer layer : layers) {
			if (layer.containsPoint(x, y)) {
				layer.onTouchEvent(event);
				break;
			}
		}
		return false;
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	public void onSensorChanged(SensorEvent arg0) {
	}

}
