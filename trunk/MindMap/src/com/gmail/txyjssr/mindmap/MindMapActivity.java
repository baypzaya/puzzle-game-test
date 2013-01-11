package com.gmail.txyjssr.mindmap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

public class MindMapActivity extends Activity implements OnClickListener {
	private MindMapView mmView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mmView = new MindMapView(this);
		mmView.setBackgroundColor(Color.GRAY);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mmView.setLayoutParams(params);
//		mmView.addView(new Node(this));
		
		setContentView(mmView);
		mmView.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Log.i("yujsh log", "v:"+v);
		mmView.addView(new Node(this));
		mmView.invalidate();
//		mmView.addNode();
	}

}
