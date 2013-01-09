package com.gmail.txyjssr.mindmap;

import android.app.Activity;
import android.os.Bundle;

public class MindMapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MindMapView mmView = new MindMapView(this);
		setContentView(mmView);
	}

}
