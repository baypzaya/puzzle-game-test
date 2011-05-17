package com.mstanford.gameframework;

import com.mstanford.gameframe.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class GameActivity extends Activity {

	private GameSurfaceView gameSurfaceView;

	private GameThread gameThread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		gameSurfaceView = (GameSurfaceView) this.findViewById(R.id.gameview);
		gameThread = gameSurfaceView.getThread();
		gameSurfaceView.setTextView((TextView) findViewById(R.id.textview));

		if (savedInstanceState == null) {
			// 游戏第一次启动时,初始化游戏状态
			gameThread.doStart();
			Log.w(this.getClass().getName(), "SIS is null");
		}
		else {
			// 从其他应用界面切回游戏时,如果Activity重新创建,则恢复上次切出游戏时的各项数据
			gameThread.restoreState(savedInstanceState);
			Log.w(this.getClass().getName(), "SIS is nonnull");
		}
	}

	/**
	 * 当Activity被切换到后台时调用,存储Activity重新创建时需要恢复的游戏数据
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		gameThread.saveState(outState);
		Log.w(this.getClass().getName(), "SIS called");
	}

	/**
	 * 当Activity被切换到后台时调用,在这里对游戏逻辑做"暂停"的处理
	 */
	@Override
	protected void onPause() {
		super.onPause();

		// 暂停游戏
		gameSurfaceView.getThread().pause();
	}

	/**
	 * 当Activity切换到前台时调用
	 */
	@Override
	protected void onResume() {
		super.onResume();

		// 游戏结束暂停状态,游戏逻辑正常进行
		gameSurfaceView.getThread().unpause();
	}

	/**
	 * 创建游戏菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 定义游戏菜单的点击事件处理
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(this.getClass().getName(), "onDestroy");
		
		//停止游戏
		gameThread.setRunning(false);

		boolean retry = true;
		while (retry) {
			try {
				//阻塞Activity的主线程直到游戏线程执行完
				gameThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}
}