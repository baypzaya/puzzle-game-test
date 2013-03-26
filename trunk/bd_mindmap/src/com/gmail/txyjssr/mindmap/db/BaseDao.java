package com.gmail.txyjssr.mindmap.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public class BaseDao{
	public static final String COLUMN_ID = "_id";
	protected DBManager mDBManager = DBManager.getInstance();

	public static String getCreateTableSQL(Class tClass) {
		String tableName = tClass.getSimpleName();
		StringBuffer sb = new StringBuffer("CREATE  TABLE ");
		sb.append(tableName).append(" (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,");

		Field[] allFields = tClass.getDeclaredFields();
		for (Field field : allFields) {
			String columnName = field.getName();
			String columnType = DatabaseUtils.getColumnType(field);
			if (columnType != null) {
				sb.append(columnName).append(" ").append(columnType).append(" ,");
			}
		}

		sb.deleteCharAt(sb.length() - 1).append(")");
		return sb.toString();
	}

	public long insert(Object o) {
		Class tClass = o.getClass();
		String tableName = tClass.getSimpleName();
		ContentValues values = DatabaseUtils.transformObject(o);
		long id = mDBManager.insert(tableName, values);
		return id;
	}

	public int update(Object o) {
		Class tClass = o.getClass();
		String tableName = tClass.getSimpleName();
		ContentValues values = DatabaseUtils.transformObject(o);
		String whereClause = "_id = ?";
		String[] whereArgs = new String[] { "" + ((BaseData) o)._id };
		int count = mDBManager.update(tableName, values, whereClause, whereArgs);
		return count;
	}

//	public List queryAll(Class tClass){
//		List list = new ArrayList();
//		String tableName = tClass.getName();
//		Cursor c = mDBManager.qury(tableName, null, null, null, null, "_id");
//		if(c!=null&&c.moveToFirst()){
//			do{
//				Object
//			}
//		}
//	}
}
