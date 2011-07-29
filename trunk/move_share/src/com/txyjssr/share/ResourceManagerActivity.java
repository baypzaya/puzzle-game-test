package com.txyjssr.share;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class ResourceManagerActivity extends Activity implements
		OnGestureListener {

	private GestureDetector detector;
	private ViewFlipper mFlipper;
	private Handler mHandler = new Handler();
	private ResourceItemAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.resource_manager_layout);

		detector = new GestureDetector(this);
		mFlipper = (ViewFlipper) findViewById(R.id.myFilpper);
		GridView myView = (GridView) View.inflate(this,
				R.layout.resource_grid_layout, null);
		adapter = new ResourceItemAdapter(this);
		myView.setAdapter(adapter);
		mFlipper.addView(myView);
	}

	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return detector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	
	

}
