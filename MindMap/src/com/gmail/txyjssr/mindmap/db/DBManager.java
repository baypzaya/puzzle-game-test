package com.gmail.txyjssr.mindmap.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.txyjssr.engine.core.EngineApplication;

public class DBManager {
	private static DBManager sDBManager;
	private DBHelper helper;
	private SQLiteDatabase db;

	private DBManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	public static DBManager getInstance() {
		if (sDBManager == null) {
			Context context = EngineApplication.getEngineCoreContextInstance();
			sDBManager = new DBManager(context);
		}
		return sDBManager;
	}

	public long insert(String table, ContentValues values) {

		long id = db.insert(table, null, values);
		return id;
	}

	public Cursor qury(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
			String orderBy) {
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy,null,orderBy);
		return c;
	}
	
	public int delete(String table, String whereClause, String[] whereArgs) {
		int count = db.delete(table, whereClause, whereArgs);
		return count;
	}
	
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		int count = db.update(table, values, whereClause, whereArgs);
		return count;
	}

	public void closeDataBase() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}
}
