package com.idreamsky.ktouchread.bookshelf;

import android.os.Handler;
import android.os.Message;
import android.widget.ViewFlipper;

public class PosterRunnable implements Runnable {
	private ViewFlipper mViewFlipper;
	private boolean flag = true;
	boolean wait = false;
	private Thread thread;
	private Handler mHandler;
	public PosterRunnable(ViewFlipper mViewFlipper,Handler mHandler) {
		this.mViewFlipper = mViewFlipper;
		this.mHandler = mHandler;
		thread = new Thread(this);
	}

	public void run() {
		while (flag) {
			if(wait){
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			try {
				Thread.sleep(6000);
//				View posterCurrentView = mViewFlipper.getCurrentView();
//				Integer currentPosterIndex = (Integer) posterCurrentView.getTag();
				Message msg = new Message();
//				Bundle bundle = new Bundle();
//				bundle.putInt("currentPosterIndex",currentPosterIndex);
//				msg.setData(bundle); 
				mHandler.sendMessage(msg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// 这里是启动线程的方法，也就是启动线程
	public void start() {
		thread.start();
	}

	// 这里是继续的方法,唤醒线程
	public void Resume() {
		wait = false;
		synchronized (this) {
			this.notify();
		}
	}

	// 停止线程
	public void stop() {
		flag = false;
//		thread.stop();
		
	}
	//停止
	public void Pause() {
		wait = true;
//		try {
//			thread.wait();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

}
