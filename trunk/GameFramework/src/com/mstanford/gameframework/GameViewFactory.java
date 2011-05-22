package com.mstanford.gameframework;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.mstanford.gameframework.model.GameView;
import com.mstanford.gameframework.model.PuzzleGameTest;

public class GameViewFactory {
	private static GameViewFactory GAME_VIEW_FACTORY;
	private Context mContext;
	private Handler mHandler;

	private GameViewFactory(Context context, Handler handler) {
		mContext = context;
		mHandler = handler;

	}

	public static GameViewFactory getInstance(Context context, Handler handler) {
		if (GAME_VIEW_FACTORY == null) {
			GAME_VIEW_FACTORY = new GameViewFactory(context, handler);
		}
		return GAME_VIEW_FACTORY;
	}
	
	public boolean sendMessage(Message msg){
		return mHandler.sendMessage(msg);
	}

	public GameView getGameView(int mode) {
		GameView gv = null;
		switch (mode) {
		case GameGlobal.GAME_SPLASH:
			break;
		case GameGlobal.GAME_MENU:
			break;
		case GameGlobal.GAME_HELP:
			break;
		case GameGlobal.GAME_ABOUT:
			break;
		case GameGlobal.GAME_RUN:
			gv = new PuzzleGameTest(mContext);
			break;
		case GameGlobal.GAME_CONTINUE:
			break;
		}
		return gv;
	}
}
