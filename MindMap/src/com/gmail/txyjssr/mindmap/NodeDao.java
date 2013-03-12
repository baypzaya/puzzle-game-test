package com.gmail.txyjssr.mindmap;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.util.Log;

import com.gmail.txyjssr.mindmap.db.BaseDao;

public class NodeDao extends BaseDao {
	private static final String TABLE_NAME = "Node";
	private static final String COLUMN_TITLE = "title";
	private static final String COLUMN_X = "x";
	private static final String COLUMN_Y = "y";
	private static final String COLUMN_MINDMAPID = "mindMapId";
	private static final String COLUMN_ISROOTNODE = "isRootNode";
	private static final String COLUMN_PARENTNODEID = "parentNodeId";

	public List<Node> getAllNodesBy(long mindMapId) {
		String selection = COLUMN_MINDMAPID + " = ?";
		String[] selectionArgs = new String[] { "" + mindMapId };
		Cursor c = mDBManager.qury(TABLE_NAME, null, selection, selectionArgs, null, COLUMN_ID);

		List<Node> nodeList = new ArrayList<Node>();
		if (c != null && c.moveToFirst()) {
			do {
				Node node = transformNodeBy(c);
				nodeList.add(node);
			} while (c.moveToNext());

			c.close();
		}
		return nodeList;
	}

	public Node getRootNodeBy(long mindMapId) {
		String selection = COLUMN_MINDMAPID + " = ? and " + COLUMN_ISROOTNODE + " = ?";
		String[] selectionArgs = new String[] { "" + mindMapId, "" + 1 };
		Cursor c = mDBManager.qury(TABLE_NAME, null, selection, selectionArgs, null, null);
		Node node = null;
		if (c != null && c.moveToFirst()) {
			do {
				node = transformNodeBy(c);
			} while (c.moveToNext());

			c.close();
		}
		return node;
	}

	private Node transformNodeBy(Cursor c) {
		Node node = new Node();
		node._id = c.getLong(c.getColumnIndex(COLUMN_ID));
		Log.i("yujsh log","ci:"+c.getColumnIndex(COLUMN_TITLE));
		Log.i("yujsh log","title:"+c.getString(c.getColumnIndex(COLUMN_TITLE)));
		node.title = c.getString(c.getColumnIndex(COLUMN_TITLE));
		node.x = c.getLong(c.getColumnIndex(COLUMN_X));
		node.y = c.getLong(c.getColumnIndex(COLUMN_Y));
		node.mindMapId = c.getLong(c.getColumnIndex(COLUMN_MINDMAPID));
		node.parentNodeId = c.getLong(c.getColumnIndex(COLUMN_PARENTNODEID));
		node.isRootNode = c.getInt(c.getColumnIndex(COLUMN_ISROOTNODE)) == 1;
		return node;
	}

}
