package com.shine.core.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

public class CMIDIPlayer {
	
	private static final String TAG = CMIDIPlayer.class.getName();
	
	public MediaPlayer mPlayerMusic;

	public Context mContext = null;

	public Map<Integer, Uri> musicResMap = new HashMap<Integer, Uri>();

	public CMIDIPlayer(Context context) {
		this.mContext = context;
	}

	// 播放音乐
	public void playMusic(int ID,boolean isLoop) {
		Log.v(TAG, "playMusic id:" + ID);
		freeMusic();
		Uri uri = musicResMap.get(ID);
		if (uri != null) {
			playMusic(uri,isLoop);
		}
	}

	// 退出释放资源
	public void freeMusic() {
		if (mPlayerMusic != null) {
			mPlayerMusic.stop();
			mPlayerMusic.release();
		}
	}

	// 停止播放
	public void stopMusic() {
		if (mPlayerMusic != null) {
			mPlayerMusic.stop();
		}
	}

	public void playMusic(Uri uri,boolean isLoop) {

		if (uri == null)
			return;
		Log.v(TAG, "playMusic uri:" + uri);

		// 装载音乐
		mPlayerMusic = MediaPlayer.create(mContext, uri);
		// 设置循环
		mPlayerMusic.setLooping(isLoop);
		try {
			// 准备
			mPlayerMusic.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 开始
		mPlayerMusic.start();
	}
}
