package com.idreamsky.ktouchread.db;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/* Max
 * 
 * ��ݿ�Ĵ���������رյ�
 * 
 * 
 */
public class DatabaseOpenHelper extends SDCardSQLiteOpenHelper{

	/* ------------------------- ���� --------------------------------------*/
	/** ����Ϣ��ݿ����*/
	private static final String DATABASE_NAME = "ktouchread.db";

	/** ��ݿ�汾��*/
	private static final int DATABASE_VARSION = 1;

	private Context mContext = null;
	/** SELECT */
	public static final String SELECT		= " SELECT ";
	/** FROM */
	public static final String FROM 		= " FROM ";
	/** WHERE */
	public static final String WHERE		= " WHERE ";
	/** AND */
	public static final String AND 			= " AND ";
	/** OR */
	public static final String OR 			= " OR ";
	
	private static final String COLUMN_COUNT = "count";

	private static final String IS_EXISTS_TABLE_SQL =
		SELECT +
			" COUNT(*) AS " + COLUMN_COUNT
		+ FROM
			+ " sqlite_master "
		+ WHERE
			+ " ( "
				+ " type = 'table' "
				+ AND
				+ " name = ?"
			+ " ) ";
	

	/**
	 * ��ʼ��
	 * @param mContext Context
	 */
	public DatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VARSION);
		this.mContext = context;
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public DatabaseOpenHelper reCreateDB() {
		deleteDB();
		return new DatabaseOpenHelper(mContext);
	}

	private void deleteDB() {
		SQLiteDatabase db = super.getWritableDatabase();
		if (db != null) {
			mContext.deleteDatabase(DATABASE_NAME);
		}
	}

	public final boolean isExistsTable(SQLiteDatabase db, String tableName) {

		boolean _result = false;

		String[] whrerArray = new String[]{
				tableName
		};
		Cursor cursor = db.rawQuery(IS_EXISTS_TABLE_SQL , whrerArray);
		cursor.moveToFirst();

		if (cursor.getCount() != 0) {
			if (cursor.getInt(cursor.getColumnIndex(COLUMN_COUNT)) != 0) {
				_result = true;
			}
		}

		cursor.close();
		return _result;
	}
}

