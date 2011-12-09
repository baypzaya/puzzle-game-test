package com.idreamsky.ktouchread.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.idreamsky.ktouchread.bookshelf.BookShelf;
import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.data.net.NetChapter;
import com.idreamsky.ktouchread.db.BookDataBase;
import com.idreamsky.ktouchread.http.sync.RequestChaseChapter;
import com.idreamsky.ktouchread.pay.KPayAccount;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.util.NetUtil;

public class ServiceChase extends Service {

	public static int NEW_MSG_NOTIF_ID = 1010;
	private Timer mTimer = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	};

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
	//	LogEx.Log_V("Service", intent.toString());

		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {

				if (NetUtil.checkNetwork(ServiceChase.this)) {
					new Thread(){

						@Override
						public void run() {
							Chase();
							super.run();
						}
						
					}.start();
					
				}
			}
		}, 0,  72000000); // 姣忛殧鍗佸垎閽熸墽琛屼竴娆°�
	}

	private void Chase() {
		if(KPayAccount.getLongtermToken() == null)
			return;

		List<Book> mBookList = new ArrayList<Book>();
		
		mBookList.addAll(Book.GetChaseBooks());
		


		int nCount = 0;
		for (int i = 0; i < mBookList.size(); i++) {
			final Book book = mBookList.get(i);
			if (book.bUpdate == 1 && book.BookType ==0)// book.bUpdate == 1
			{
				NetChapter NetChapterInfo = new RequestChaseChapter(book.rpid,
						book.cpcode, book.bookidNet).GetChapter();
				if (NetChapterInfo != null) {
					if (NetChapterInfo.bookStatus == 0)// 瀹岀粨
					{
						book.UpdateStatus(2);
					}
					if(NetChapterInfo.mChapterInfoList != null && NetChapterInfo.mChapterInfoList.size() > 0)
					{
						 nCount += BookDataBase.getInstance().InsertChapterEx(NetChapterInfo, book.bookidNet);
						NetChapterInfo.mChapterInfoList.clear();
						NetChapterInfo.mChapterInfoList = null;
					}
					Intent intentBroadcast = new Intent();
					intentBroadcast
							.setAction(BookShelf.ACTION_BOOKSHELF_UPDATE_CHASE);
					this.sendBroadcast(intentBroadcast);
//					 nCount +=
//					 BookDataBase.getInstance().GetUnreadChapterCount(book.bookidNet);
					 if(NetChapterInfo.mChapterInfoList != null)
					 {
						 NetChapterInfo.mChapterInfoList.clear();
						 NetChapterInfo = null;
					 }
					    
					
				}
			}

		}

		LogEx.Log_V("Token", "UpdateCount:" +Integer.toString(nCount));
		if (nCount > 0) {

			myNotify(nCount);
			
		} 
		mBookList.clear();
		mBookList = null;

	}

	@Override
	public boolean stopService(Intent name) {
		// TODO Auto-generated method stub

		return super.stopService(name);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	// 璁剧疆鍥炬爣鐘舵�
	public void myNotify(int nCount) {
		NotificationManager nM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification note = new Notification(0, "", System.currentTimeMillis());
		note.number = nCount; // 鍋囪闇�鍦ㄥ浘鏍囦笂鏄剧ず鏁板瓧鈥�鈥�
//		note.classNameInLauncherApp = "com.idreamsky.ktouchread.bookshelf.Book_SplashAct";
		// 搴旂敤鐨剆tart activity
		Context context = getApplicationContext();
		note.setLatestEventInfo(context, "", "", null);

		nM.notify(NEW_MSG_NOTIF_ID, note);
	}

	// 鎭㈠鍥炬爣鐘舵�
	public void unNotify() {
		NotificationManager nM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nM.cancel(NEW_MSG_NOTIF_ID);
	}

}
