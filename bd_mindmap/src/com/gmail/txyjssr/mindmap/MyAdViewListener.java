package com.gmail.txyjssr.mindmap;

import org.json.JSONObject;

import android.util.Log;
import android.view.View;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;

public class MyAdViewListener implements AdViewListener {
	
	private AdView adView;
	
	public MyAdViewListener(AdView adView){
		this.adView = adView;
	}

	@Override
	public void onVideoStart() {
//		Log.i("yujsh log","onVideoStart");
	}

	@Override
	public void onVideoFinish() {
//		Log.i("yujsh log","onVideoFinish");
	}

	@Override
	public void onAdSwitch() {
//		Log.i("yujsh log","onAdSwitch");
	}

	@Override
	public void onAdShow(JSONObject arg0) {
//		Log.i("yujsh log","onAdShow");
	}

	@Override
	public void onAdReady(AdView arg0) {
//		Log.i("yujsh log","onAdReady");
	}

	@Override
	public void onAdFailed(String arg0) {
//		Log.i("yujsh log","onAdFailed");
//		adView.setVisibility(View.GONE);

	}

	@Override
	public void onAdClick(JSONObject arg0) {
//		Log.i("yujsh log","onAdClick");
	}
}
