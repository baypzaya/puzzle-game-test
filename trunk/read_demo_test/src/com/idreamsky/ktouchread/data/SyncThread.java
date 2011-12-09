package com.idreamsky.ktouchread.data;

import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.idreamsky.ktouchread.data.net.NetBookMark;
import com.idreamsky.ktouchread.data.net.NetBookShelf.BookEx;
import com.idreamsky.ktouchread.data.net.NetChapter;
import com.idreamsky.ktouchread.db.BookDataBase;
import com.idreamsky.ktouchread.http.sync.RequestGetBookMark;
import com.idreamsky.ktouchread.http.sync.RequestGetBookShelf;
import com.idreamsky.ktouchread.http.sync.RequestGetChapter;
import com.idreamsky.ktouchread.util.SettingUtils;

public class SyncThread extends Thread{
	private Context mContext = null;
	public SyncThread(SyncCallback syncCallback,Context context)
	{
		mContext = context;
		mSyncCallback = syncCallback;
	}
	public static interface SyncCallback {
		public void onSuccess();
		public void onFail();
	}
	private boolean flag = false;
	SyncCallback mSyncCallback;
	public SyncThread(SyncCallback syncCallback)
	{
		mSyncCallback = syncCallback;
	}
	
	public boolean IsStop()
	{
		return !flag;
	}
	public void StartThread()
	{
		flag = true;
		this.start();
	}
	public void StopThread()
	{
		if(flag = true)
		{
			if(isAlive())
			{
				this.stop();
			}
			flag = false;			
		}
	}
	public void run()
	{
		Book.Init();
		if(mContext != null)
		{
			SettingUtils utils = new SettingUtils(mContext, "SaveParameter",
					 Context.MODE_PRIVATE);
			
			if(!utils.getBoolean("InitBook", false));
			{
				String bookID = "1209977";
				if (!Book.IsExit(bookID)) {
					Book book = new Book();
					book.Book_Name = "斗破苍穹";
					book.Author = "天蚕土豆";
					book.pic_Path = "";
					book.Pic_Url = "http://img1.sddcp.com/10/1209/977/cover.jpg";
					book.Recent_Chapter_ID = "";
					book.Recent_Chapter = "";
					book.Recent_Chapter_Name = "";
					book.bUpdate = 2; // 0 不追更 ，1追更
					book.bFree = 2; // 0 免费 ，1付费

					book.cpcode = "yzsc"; // CP编号
					book.rpid = "10"; // CP的CP编号
					book.bookidNet = "1209977"; // 书籍编号
					book.Price = "0";
					book.Sync = 0;
					book.BookType = 0;

					book.RecentReadTime = System.currentTimeMillis();
					Book.GetBookListEx().add(book);
					
					NetChapter chapters = new RequestGetChapter(book.cpcode,
							book.rpid, book.bookidNet).GetChapter();
					if(chapters != null)
					{
						int nCount = BookDataBase.getInstance().InsertChapterEx(chapters, book.bookidNet);
						chapters.mChapterInfoList.clear();
						chapters.mChapterInfoList = null;
						chapters = null;
					}	
					book = null;
					utils.putBolean("InitBook", true);
				}
			}
		}

        HashMap<String,String>map =	 Book.GetBookIDMap();
		List<BookEx> bookList =  new RequestGetBookShelf().GetBookShelf();
		if(bookList != null)
		{
			for(int i = 0 ; i < bookList.size();i++ )
			{
				if(!flag)
				{
					break;
				}
				BookEx bookEx = bookList.get(i);

				if(bookEx.isDel == 1) // delete
				{
					if(map.containsKey(bookEx.bookid))
					{
						Book.DeleteBook(bookEx.bookid);
					}
					
				}
				else                  //add    
				{
					if(!map.containsKey(bookEx.bookid))
					{
						Book book = new Book();
						
						book.Book_Name = bookEx.bookname;
						book.Author = bookEx.authorName;							
						book.pic_Path = "";
						book.Pic_Url = bookEx.coverimageurl;
						book.Recent_Chapter_ID = "";
						book.Recent_Chapter = "";
						book.Recent_Chapter_Name = "";
						book.BookType = 0;
						book.Price = "0";
						if(bookEx.bookstatus == 0) //完结
						{
							book.bUpdate = 2;
						}
						else {
							book.bUpdate = bookEx.ischase;
						}
						               
						book.bFree = 0;                   //0 免费     ，1付费
						
						book.cpcode = bookEx.cpcode;        //CP编号
						book.rpid = bookEx.rpid;          //CP的CP编号
						book.bookidNet = bookEx.bookid;        //书籍编号
						book.Sync = 0;
						book.RecentReadTime = System.currentTimeMillis();
						Book.GetBookListEx().add(book);
						
						

						NetChapter NetChapterInfo = new RequestGetChapter(book.cpcode, book.rpid, book.bookidNet).GetChapter();
						if(NetChapterInfo != null)
						{
							BookDataBase.getInstance().InsertChapterEx(NetChapterInfo, book.bookidNet);
							NetChapterInfo.mChapterInfoList.clear();
							NetChapterInfo.mChapterInfoList = null;
							NetChapterInfo = null;
						}				
					}

				}

				List<NetBookMark> bookmarklist = new RequestGetBookMark(bookEx.cpcode, bookEx.rpid, bookEx.bookid).GetBookMark();
				if(bookmarklist != null)
				{
					for(int j = 0 ; j < bookmarklist.size(); j++)
					{
						if(!flag)
						{
							break;
						}
						NetBookMark netBookMark = bookmarklist.get(j);

						if(netBookMark.isDel == 1)
						{
							if(BookDataBase.getInstance().IsExistsBookMark(netBookMark.bookmarkid))
							{
								BookDataBase.getInstance().DeleteBookMarkByBookId(netBookMark.bookmarkid);
							}
							
						}
						else
						{
							if(!BookDataBase.getInstance().IsExistsBookMark(netBookMark.bookmarkid))
							{
								BookMark bookMark = new BookMark();
								
								bookMark.Book_ID_Net = netBookMark.bookmarkid;	//书签编号		
								bookMark.Mark_Text = netBookMark.textdesc;	    //说明		
								bookMark.Chapter_ID_Net = netBookMark.chapteridNet;	//章节编号		
								bookMark.Pos = netBookMark.pos;	        //位置		客户端自己定义吧，服务器只需要记录一个位置信息即可，比如1,3,4000 代表第一章，第三页，每页400字的这么一个位置信息。
								bookMark.Date = netBookMark.updatets;	    //最后更新时间		
								bookMark.Sync = 0;
								BookDataBase.getInstance().InsertBookMark(bookMark);
							}

						}		
					}
					bookmarklist.clear();
					bookmarklist = null;
				}
			}
			bookList.clear();
			bookList = null;
		}
		Book.InitChase();
		Book.Save();
		if(flag)
		{
			mSyncCallback.onSuccess();
		}
		
	}
}
