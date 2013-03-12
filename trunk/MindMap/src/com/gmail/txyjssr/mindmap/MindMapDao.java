package com.gmail.txyjssr.mindmap;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.gmail.txyjssr.mindmap.db.BaseDao;

public class MindMapDao extends BaseDao {
	private static final String TABLE_MINDMAP = "TabMindMap";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_ISCURRENT = "isCurrent";
	

	public TabMindMap getCurrentMindMap() {
		TabMindMap mm = null;
		Cursor c = mDBManager.qury(TABLE_MINDMAP, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			int columnIndex = c.getColumnIndex("isCurrent");
			int isCurrent = c.getInt(columnIndex);
			Log.i("yujsh log", "isCurrent:" + isCurrent);
		}

		return mm;
	}

	public void updateRecentMindMap(long mindMapId) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_ISCURRENT, false);
		
		String whereClause = COLUMN_ID + " <> ?";
		String[] whereArgs = new String[]{""+mindMapId};
		mDBManager.update(TABLE_MINDMAP, values, whereClause, whereArgs);
		
		
		values.put(COLUMN_ISCURRENT, true);
		whereClause = COLUMN_ID + " = ?";
		mDBManager.update(TABLE_MINDMAP, values, whereClause, whereArgs);
	}
}
