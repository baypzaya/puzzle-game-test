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
		String selection = COLUMN_ISCURRENT + " = ?";
		String[] selectionArgs = new String[]{""+1};
		Cursor c = mDBManager.qury(TABLE_MINDMAP, null, selection, selectionArgs, null, null);
		if (c != null && c.moveToFirst()) {
			mm = new TabMindMap();
			mm._id = c.getLong(c.getColumnIndex(COLUMN_ID));
			mm.name = c.getString(c.getColumnIndex(COLUMN_NAME));
			mm.isCurrent = c.getInt(c.getColumnIndex(COLUMN_ISCURRENT)) == 1;
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
