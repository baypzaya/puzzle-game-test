package com.mstanford.gameframework;

import android.content.Context;

import com.mstanford.gameframework.model.GameView;
import com.mstanford.gameframework.model.PuzzleGameTest;

public class GameViewFactory {
	private Context m_Context;

	public GameViewFactory(Context context) {
		m_Context = context;
	}

	public GameView getGameView(int mode) {
		GameView gv = null;
		switch (mode) {
		case GameSurfaceView.GAME_SPLASH:
			break;
		case GameSurfaceView.GAME_MENU:
			break;
		case GameSurfaceView.GAME_HELP:
			break;
		case GameSurfaceView.GAME_ABOUT:
			break;
		case GameSurfaceView.GAME_RUN:
			gv = new PuzzleGameTest(m_Context);
			break;
		case GameSurfaceView.GAME_CONTINUE:
			break;
		}
		return gv;
	}
}
