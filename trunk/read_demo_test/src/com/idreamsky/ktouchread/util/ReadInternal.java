package com.idreamsky.ktouchread.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;




import android.util.Log;



public class ReadInternal {
	private RequestPoolExecutor mExecutor;
	private static final int CORE_POOL_SIZE = 3;

	private static final int MAX_POOL_SIZE = 4;

	private static final int ALIVE_TIME = 30;
	
	private static ReadInternal sInstance;
	private static byte[] SYNC = new byte[0];
	
	public static ReadInternal getInstance() {
		if (null == sInstance) {
			synchronized (SYNC) {
				if (null == sInstance) {
					sInstance = new ReadInternal();
				}
			}
		}
		return sInstance;
	}
	
	public ReadInternal()
	{
		mExecutor = new RequestPoolExecutor();
	}
	public ThreadPoolExecutor getPoolExecutor() {
		return mExecutor;
	}
	
	private static class RequestPoolExecutor extends ThreadPoolExecutor {

		public RequestPoolExecutor() {
			super(CORE_POOL_SIZE, MAX_POOL_SIZE, ALIVE_TIME, TimeUnit.SECONDS,
					new LinkedBlockingQueue<Runnable>(),
					new RejectedExecutionHandler() {

						@Override
						public void rejectedExecution(Runnable r,
								ThreadPoolExecutor executor) {
							Log.e(Configuration.TAG,
									"Reject executing runnable " + r.toString());
						}
					});
		}
	}
}
