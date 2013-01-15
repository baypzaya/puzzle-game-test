package com.gmail.txyjssr.mindmap;

import java.util.List;

public class Node {
	
	public int id;
	public int parentId;
	public String title="";
	public float x;
	public float y;
	public List<Node> nodeChildren;
	public boolean isRootNode;

}
