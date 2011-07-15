package com.shine.core;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenueScene implements IScene {
	private Context mContext = EngineProperties.sContext;
	private View mContentView;

	public MenueScene() {
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		mContentView = layoutInflater.inflate(R.layout.game_menu, null);
		create();
	}

	@Override
	public View getContentView() {
		return mContentView;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onTouch(MotionEvent event) {
		return true;

	}

	@Override
	public void create() {
		Button start = (Button) mContentView.findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShineGameEngineActivity activity = (ShineGameEngineActivity) mContext;
				Handler handler = activity.getEngineHandler();
				Message message = handler
						.obtainMessage(ShineGameEngineActivity.UPDATE_CURREN_SCENE);
				message.obj = SceneManager.GAME_SCENE_INDEX;
				message.sendToTarget();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
