package com.idreamsky.ktouchread.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
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

public class Book_System_SettingService extends Service {

//	private int NEW_MSG_NOTIF_ID = 1010;
//	private HashMap<String,String> map=new HashMap<String,String>();
	
	private Timer mTimer = null;
    
	
	@Override
	public void onCreate() {
		super.onCreate();
		LogEx.Log_V("Service", "Book_System_SettingService Create");
		if(mTimer == null)
		{
			LogEx.Log_V("Service", "Create mTimer == null");
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		super.onDestroy();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		
		
		if(mTimer != null)
		{
			mTimer.cancel();
			mTimer = null;
		}
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				if(NetUtil.checkNetwork(getApplicationContext())){
					new Thread(){

						@Override
						public void run() {
							Chase();	
							super.run();
						}
						
					}.start();
					
				}
			}
		},0,72000000);   //1000*600 每12小时执行一次。
		
		super.onStart(intent, startId);
	}
	
	
	private void Chase()
	{
		if(KPayAccount.getLongtermToken() == null)
			return;

		List<Book> mBookList = new ArrayList<Book>();
		
		mBookList.addAll(Book.GetChaseBooks());
		for (int i = 0; i < mBookList.size(); i++) {
			final Book book = mBookList.get(i);
			if (book.bUpdate == 1 && book.BookType == 0)// book.bUpdate == 1
			{
				NetChapter NetChapterInfo = new RequestChaseChapter(book.rpid,
						book.cpcode, book.bookidNet).GetChapter();
				if (NetChapterInfo != null) {
					if (NetChapterInfo.bookStatus == 0)// 完结
					{
						book.UpdateStatus(2);
					}
					int nCount = 0;
					if(NetChapterInfo.mChapterInfoList != null && NetChapterInfo.mChapterInfoList.size() > 0)
					{
						nCount = BookDataBase.getInstance().InsertChapterEx(NetChapterInfo, book.bookidNet);
						NetChapterInfo.mChapterInfoList.clear();
						NetChapterInfo.mChapterInfoList = null;
					}
					Intent intentBroadcast = new Intent();
					intentBroadcast
							.setAction(BookShelf.ACTION_BOOKSHELF_UPDATE_CHASE);
					this.sendBroadcast(intentBroadcast);
//					int nCount = BookDataBase.getInstance().GetUnreadChapterCount(book.bookidNet);
					if(nCount > 0)
					{
						myNotify(nCount,book.Book_Name,book.bookidNet,book.Book_ID);
					}

				}
			}

		}
		mBookList.clear();
		mBookList = null;

	}
	


	protected void myNotify(int nCount,String BookName, String BookIDNet ,int bookid) {
		 NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		 CharSequence from ="《" + BookName + "》更新了" + Integer.toString(nCount) + "章";
		 Intent openintent = new Intent(this,
					BookShelf.class);
		 
//		 Bundle bundle = new Bundle();
//	     bundle.putString("BookIDNet", BookIDNet);
//	     openintent.putExtras(bundle);
	     openintent.putExtra("BookIDNet", BookIDNet);
	     
	     LogEx.Log_V("BookIDNet", BookIDNet);
	     
		 PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	                openintent, 0);
		 Notification notif = new Notification(com.idreamsky.ktouchread.bookshelf.R.drawable.icon,null,
	                System.currentTimeMillis());
		 notif.defaults |=Notification.DEFAULT_SOUND; 
		 notif.flags |= Notification.FLAG_AUTO_CANCEL;
		 
		 notif.setLatestEventInfo(this, from, null, contentIntent);
		 nm.notify(bookid, notif);
		 
//	     String bookflag=map.get(BookIDNet);
//		 if(bookflag!=null){
//			 Log.i("hello","false");
//			 
//		 }else{
//			 Log.i("hello","true");
//			 nm.notify(NEW_MSG_NOTIF_ID, notif);
//			 map.put(BookIDNet,String.valueOf(NEW_MSG_NOTIF_ID));
//			 NEW_MSG_NOTIF_ID++;
//		 }
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	





}
