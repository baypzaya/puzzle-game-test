package com.txyjssr.gl;

import com.txyjssr.gl.test.TestScene;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

public class GLMainActivity extends Activity {
	
	RenderView mRenderView;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AppUtils.sContext = this;

		GLSurfaceView glView = new GLSurfaceView(this);
		mRenderView = new RenderView();
		
		TestScene scene = new TestScene();
		scene.onCreate();
		RenderView.SCENE_MAP.put(1, scene);
		mRenderView.setState(1);
		
		
		glView.setRenderer(mRenderView);
		setContentView(glView);
    }
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mRenderView.getCurrentScene().onTouchEvent(event);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {		
		super.onDestroy();
		System.exit(0);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}    
}