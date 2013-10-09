package com.gmail.txyjssr.mindmap.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;


public class MindMapDao extends BaseDao {
	private static final String TABLE_MINDMAP = "TabMindMap";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_ISCURRENT = "isCurrent";

	public TabMindMap getCurrentMindMap() {
		TabMindMap mm = null;
		String selection = COLUMN_ISCURRENT + " = ?";
		String[] selectionArgs = new String[] { "" + 1 };
		Cursor c = mDBManager.qury(TABLE_MINDMAP, null, selection, selectionArgs, null, null);
		if (c != null && c.moveToFirst()) {
			mm = new TabMindMap();
			mm._id = c.getLong(c.getColumnIndex(COLUMN_ID));
			mm.name = c.getString(c.getColumnIndex(COLUMN_NAME));
			mm.isCurrent = c.getInt(c.getColumnIndex(COLUMN_ISCURRENT)) == 1;

			c.close();
		}

		return mm;
	}

	public void updateRecentMindMap(long mindMapId) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_ISCURRENT, false);

		String whereClause = COLUMN_ID + " <> ?";
		String[] whereArgs = new String[] { "" + mindMapId };
		mDBManager.update(TABLE_MINDMAP, values, whereClause, whereArgs);

		values.put(COLUMN_ISCURRENT, true);
		whereClause = COLUMN_ID + " = ?";
		mDBManager.update(TABLE_MINDMAP, values, whereClause, whereArgs);
	}

	public List<TabMindMap> getAllMindMap() {
		Cursor c = mDBManager.qury(TABLE_MINDMAP, null, null, null, null, COLUMN_ID+" desc");
		List<TabMindMap> tmmList = new ArrayList<TabMindMap>();
		if (c != null && c.moveToFirst()) {
			do {
				TabMindMap mm = new TabMindMap();
				mm._id = c.getLong(c.getColumnIndex(COLUMN_ID));
				mm.name = c.getString(c.getColumnIndex(COLUMN_NAME));
				mm.isCurrent = c.getInt(c.getColumnIndex(COLUMN_ISCURRENT)) == 1;
				tmmList.add(mm);
			} while (c.moveToNext());
			c.close();
		}

		return tmmList;
	}

	public void updateMindMapnnName(long mindMapId, String name) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);

		String whereClause = COLUMN_ID + " = ?";
		String[] whereArgs = new String[] { "" + mindMapId };
		mDBManager.update(TABLE_MINDMAP, values, whereClause, whereArgs);
		
	}

	public TabMindMap getMindMapBy(long mindMapId) {
		TabMindMap mm = null;
		String selection = COLUMN_ID + " = ?";
		String[] selectionArgs = new String[] { "" + mindMapId };
		Cursor c = mDBManager.qury(TABLE_MINDMAP, null, selection, selectionArgs, null, null);
		if (c != null && c.moveToFirst()) {
			mm = new TabMindMap();
			mm._id = c.getLong(c.getColumnIndex(COLUMN_ID));
			mm.name = c.getString(c.getColumnIndex(COLUMN_NAME));
			mm.isCurrent = c.getInt(c.getColumnIndex(COLUMN_ISCURRENT)) == 1;

			c.close();
		}

		return mm;
	}

	public void deleteTabMindMap(TabMindMap tmm) {
		String whereClause = COLUMN_ID + " = ?";
		String[] whereArgs = new String[] { "" + tmm._id };
		mDBManager.delete(TABLE_MINDMAP, whereClause, whereArgs);
	}
}
