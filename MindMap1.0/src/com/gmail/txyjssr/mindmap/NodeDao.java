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

	public List<Node> getNodesBy(long id, boolean holdRootNode) {
		String selection = "";
		String[] selectionArgs = null;
		if (!holdRootNode) {
			selection = COLUMN_ID + " = ? or " + COLUMN_PARENTNODEID + " = ?";
			selectionArgs = new String[] { "" + id, "" + id };
		} else {
			selection = COLUMN_PARENTNODEID + " = ?";
			selectionArgs = new String[] { "" + id };
		}
		Cursor c = mDBManager.qury(TABLE_NAME, null, selection, selectionArgs, null, null);

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

	public void deleteNodesBy(long id, boolean holdRootNode) {
		String whereClause = "";
		String[] whereArgs = null;
		if (!holdRootNode) {
			whereClause = COLUMN_ID + " = ? or " + COLUMN_PARENTNODEID + " = ?";
			whereArgs = new String[] { "" + id, "" + id };
		} else {
			whereClause = COLUMN_PARENTNODEID + " = ?";
			whereArgs = new String[] { "" + id };
		}
		mDBManager.delete(TABLE_NAME, whereClause, whereArgs);
	}

	private Node transformNodeBy(Cursor c) {
		Node node = new Node();
		node._id = c.getLong(c.getColumnIndex(COLUMN_ID));
		node.title = c.getString(c.getColumnIndex(COLUMN_TITLE));
		node.x = c.getLong(c.getColumnIndex(COLUMN_X));
		node.y = c.getLong(c.getColumnIndex(COLUMN_Y));
		node.mindMapId = c.getLong(c.getColumnIndex(COLUMN_MINDMAPID));
		node.parentNodeId = c.getLong(c.getColumnIndex(COLUMN_PARENTNODEID));
		node.isRootNode = c.getInt(c.getColumnIndex(COLUMN_ISROOTNODE)) == 1;
		return node;
	}

	public List<Node> getChildNodesBy(long id) {
		return getNodesBy(id, true);
	}

	public void deleteNodesBy(List<Node> listNode) {
		if (listNode != null && listNode.size() > 0) {
			
			StringBuffer sb = new StringBuffer("(");
			for (Node n : listNode) {
				sb.append(n._id).append(",");
			}
			sb.deleteCharAt(sb.length()-1).append(")");
			String whereClause = COLUMN_ID + " in "+sb.toString();
//			String[] whereArgs = new String[] {};
			mDBManager.delete(TABLE_NAME, whereClause, null);
		}
	}

	public void deleteNodesBy(long mindMapId) {
		String whereClause = COLUMN_MINDMAPID + " = ?";
		String[] whereArgs = new String[] {""+mindMapId};
		mDBManager.delete(TABLE_NAME, whereClause, whereArgs);
	}
}
