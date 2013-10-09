package com.gmail.txyjssr.mindmap.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "database.db";
	private static final int DATABASE_VERSION = 1;
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String sqlMindMap = BaseDao.getCreateTableSQL(TabMindMap.class);
		db.execSQL(sqlMindMap);
		
		String sqlNode = BaseDao.getCreateTableSQL(Node.class);
		db.execSQL(sqlNode);
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
