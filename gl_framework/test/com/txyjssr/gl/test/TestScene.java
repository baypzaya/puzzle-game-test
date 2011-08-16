package com.txyjssr.gl.test;

import com.txyjssr.gl.BaseScene;
import com.txyjssr.gl.RenderView;

public class TestScene extends BaseScene {
	

	@Override
	public void onCreate() {
		TestTragleLayer testLayer = new TestTragleLayer();
		this.layers.add(testLayer);
	}

	@Override
	public void onSizeChanged(int width, int height) {
		setSize(width, height);
	}

	@Override
	public void generate(RenderView view) {
		view.SCENE_MAP.put(1, this);		
	}

}
