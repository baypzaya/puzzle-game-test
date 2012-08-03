package com.gmail.txyjssr.reader.dao.db;

import com.gmail.txyjssr.reader.dao.BookDao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "YReaderDataBase.db";
	private static final int DATABASE_VERSION = 1;
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//book table
		String sql = BookDao.getInstance().getCreateTableSQL();
		db.execSQL(sql);
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
