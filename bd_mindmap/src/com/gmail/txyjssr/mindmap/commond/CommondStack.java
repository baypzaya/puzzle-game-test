package com.gmail.txyjssr.mindmap.commond;

import java.util.LinkedList;


public class CommondStack {
	private static final int MAX_STACK = 20;
	private LinkedList<ICommond> undoStack = new LinkedList<ICommond>();
	private LinkedList<ICommond> redoStack = new LinkedList<ICommond>();
	
	
	
	public void clear(){
		undoStack.clear();
		redoStack.clear();
	}
	
	private void pushUndo(ICommond commond){
		if(undoStack.size()>MAX_STACK){
			undoStack.removeLast();
		}
		undoStack.addFirst(commond);
	}
	
	private ICommond popUndo(){
		ICommond commond = undoStack.getFirst();
		undoStack.removeFirst();
		return commond;
	}
	
	private void pushRedo(ICommond commond){
		if(redoStack.size()>MAX_STACK){
			redoStack.removeLast();
		}
		redoStack.addFirst(commond);
	}
	
	private ICommond popRedo(){
		ICommond commond = redoStack.getFirst();
		redoStack.removeFirst();
		return commond;
	}
	
	public boolean canUndo(){
		return !undoStack.isEmpty();
	}
	
	public boolean canRedo(){
		return !redoStack.isEmpty();
	}
	
	public void redo(){
		ICommond commond = popRedo();
		commond.redo();
		pushUndo(commond);
	}
	
	public void undo(){
		ICommond commond = popUndo();
		commond.undo();
		pushRedo(commond);
	}
	
	public void pushCommond(ICommond commond){
		pushUndo(commond);
		redoStack.clear();
	}
}
