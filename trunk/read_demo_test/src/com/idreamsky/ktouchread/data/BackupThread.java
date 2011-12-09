package com.idreamsky.ktouchread.data;

import java.util.List;

import com.idreamsky.ktouchread.db.BookDataBase;
import com.idreamsky.ktouchread.http.sync.RequestAddBookMark;
import com.idreamsky.ktouchread.http.sync.RequestAddBookshelf;
import com.idreamsky.ktouchread.http.sync.RequestDeleteBookShelf;
import com.idreamsky.ktouchread.http.sync.RequestDeleteBookmark;
import com.idreamsky.ktouchread.util.LogEx;

public class BackupThread extends Thread {
	
	public static interface BackupCallback {
		public void onSuccess();
		public void onFail();
	}
	private boolean flag = false;
	BackupCallback mBackupCallback;
	public BackupThread(BackupCallback syncCallback)
	{
		mBackupCallback = syncCallback;
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

		List<Book> BookList = Book.GetSyncBooks();
		if(BookList == null)
			return ;
		for (int i = 0; i < BookList.size(); i++) {
			final Book book = BookList.get(i);
			if (flag == false) {
				break;
			}
			if (book.Sync == 0 && book.BookType == 0) { // 增加
				if (new RequestAddBookshelf(book.cpcode, book.rpid,
						book.bookidNet).Add()) {
					Book.UpdateBookSyncStatus(book.bookidNet, 1);
					LogEx.Log_V("AddBookShelfNet", "Success");
				} else {
					flag = false;
					LogEx.Log_V("AddBookShelfNet", "fail");
				}
			} else if (book.Sync == 2 && book.BookType == 0) { // 删除
				if (new RequestDeleteBookShelf(book.cpcode, book.rpid,
						book.bookidNet).Delete()) {

					List<BookMark> bookMarkList = BookDataBase.getInstance()
							.QueryBookMarkByBookID(book.bookidNet);

					for (int j = 0; i < bookMarkList.size(); i++) {
						if (flag == false) {
							break;
						}
						final BookMark bookMark = bookMarkList.get(j);
						if (bookMark.BookMarkIDNet != null
								&& bookMark.BookMarkIDNet.length() > 0) {
							new RequestDeleteBookmark(bookMark.BookMarkIDNet)
									.Delete();
						}
					}
					BookDataBase.getInstance().DeleteBookById(book.bookidNet);
				}
			}

		}

		BookList.clear();
		List<BookMark> bookMarkList = BookDataBase.getInstance()
				.QueryBookMarkUnSync();
		for (int j = 0; j < bookMarkList.size(); j++) {
			if (flag == false) {
				break;
			}
			final BookMark bookMark = bookMarkList.get(j);
			if (bookMark.Sync == 1) {
				String bookmarkidNet = new RequestAddBookMark(bookMark.cpcode, bookMark.rpid,
						bookMark.Book_ID_Net, bookMark.Chapter_ID_Net,
						bookMark.Pos, bookMark.Mark_Text, "0").Add();
				if (bookmarkidNet != null && bookmarkidNet.length() > 0) {
					bookMark.BookMarkIDNet = bookmarkidNet;
					BookDataBase.getInstance().UpdateBookMarkIDNet(
							bookMark.BookMark_ID, bookmarkidNet);
					BookDataBase.getInstance().UpdateBookMarkSync(
							bookMark.BookMark_ID, 0);
					LogEx.Log_V("AddBookMarkNet", "Success");
				} else {
					LogEx.Log_V("AddBookMarkNet", "fail");
					flag = false;
				}
			} else {
				if (bookMark.BookMarkIDNet != null
						&& bookMark.BookMarkIDNet.length() > 0) {
					if (new RequestDeleteBookmark(bookMark.BookMarkIDNet)
							.Delete()) {
						BookDataBase.getInstance().DeleteBookMarkById(
								bookMark.BookMark_ID);
					} else {
						BookDataBase.getInstance().DeleteBookMarkById(
								bookMark.BookMark_ID);
					}
				}

			}
		}
		
		bookMarkList.clear();
		bookMarkList = null;
		mBackupCallback.onSuccess();
	}

}
