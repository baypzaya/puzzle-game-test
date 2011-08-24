package com.txyjssr.share;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

public class ImageResourcesActivity extends Activity {
	GLRender imageResourceGl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// super.onCreate(savedInstanceState);

		// setContentView(imageResourceGl);
		//
		super.onCreate(savedInstanceState);

		GLSurfaceView glView = new GLSurfaceView(this);
		imageResourceGl = new GLRender(this);
		glView.setRenderer(imageResourceGl);
		setContentView(glView);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			imageResourceGl.onKeyUp(0, null);
		}
		return super.onTouchEvent(event);
	}
}
