package com.gmail.txyjssr.reader.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.gmail.txyjssr.reader.dao.db.DBManager;
import com.gmail.txyjssr.reader.data.Book;

public class BookDao {

	public String name;
	public String path;
	public int progress;
	public long lastReadTime;
	public String encodeType;

	public static final String TAB_BOOK_NAME = "book";
	public static final String COL_BOOK_ID = "_id";
	public static final String COL_BOOK_NAME = "name";
	public static final String COL_BOOK_PATH = "path";
	public static final String COL_BOOK_PROGRESS = "progress";
	public static final String COL_BOOK_LAST_READ_TIME = "last_read_time";
	public static final String COL_BOOK_ENCODE_TYPE = "encode_type";

	private static BookDao sBookDao;

	public static BookDao getInstance() {
		if (sBookDao == null) {
			sBookDao = new BookDao();
		}
		return sBookDao;
	}

	public List<Book> getBooks() {
		List<Book> books = new ArrayList<Book>();
		DBManager dbm = DBManager.getInstance();
		Cursor cursor = dbm.qury(TAB_BOOK_NAME, null, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				books.add(changeToBook(cursor));
			} while (cursor.moveToNext());
		}
		return books;
	}

	public long addBook(Book book) {
		DBManager dbm = DBManager.getInstance();
		long id = dbm.insert(TAB_BOOK_NAME, changeToContentValues(book));
		return id;
	}

	public int deleteBook(Book book) {
		DBManager dbm = DBManager.getInstance();
		String whereClause = COL_BOOK_ID + " = ?";
		String[] whereArgs = { "" + book.id };
		int count = dbm.delete(TAB_BOOK_NAME, whereClause, whereArgs);
		return count;
	}

	private ContentValues changeToContentValues(Book book) {
		ContentValues values = new ContentValues();
		values.put(COL_BOOK_NAME, book.name);
		values.put(COL_BOOK_PATH, book.path);
		values.put(COL_BOOK_PROGRESS, book.progress);
		values.put(COL_BOOK_LAST_READ_TIME, book.lastReadTime);
		values.put(COL_BOOK_ENCODE_TYPE, book.encodeType);
		return values;
	}

	private Book changeToBook(Cursor cursor) {
		Book book = new Book();
		book.id = cursor.getLong(cursor.getColumnIndex(COL_BOOK_ID));
		book.name = cursor.getString(cursor.getColumnIndex(COL_BOOK_NAME));
		book.path = cursor.getString(cursor.getColumnIndex(COL_BOOK_PATH));
		book.progress = cursor.getInt(cursor.getColumnIndex(COL_BOOK_PROGRESS));
		book.lastReadTime = cursor.getLong(cursor.getColumnIndex(COL_BOOK_LAST_READ_TIME));
		book.encodeType = cursor.getString(cursor.getColumnIndex(COL_BOOK_ENCODE_TYPE));
		return book;
	}

	public String getCreateTableSQL() {
		String sql = "CREATE  TABLE book (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , name VARCHAR, path VARCHAR, progress INTEGER, last_read_time INTEGER, encode_type VARCHAR)";
		return sql;
	}

	public boolean isExist(Book book) {
		DBManager dbm = DBManager.getInstance();
		String whereClause = COL_BOOK_ID + " = ?";
		String[] whereArgs = { "" + book.id };
		Cursor cursor = dbm.qury(TAB_BOOK_NAME, null, whereClause, whereArgs, null, null);

		return cursor != null && cursor.getCount() > 0;
	}
}
