package com.yujsh.android.app.adpater;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainTest {

	int arrayCol = 5;
	int arrayRow = 6;
	int[][] mArray = {  { 0, 0, 0, 0, 0 }, 
						{ 0, 0, 0, 0, 0 }, 
						{ 2, 2, 2, 0, 0 },
						{ 2, 2, 2, 1, 0 }, 
						{ 3, 3, 3, 1, 0 }, 
						{ 1, 1, 1, 1, 1 } };

	public static void main(String[] args) {
		MainTest test = new MainTest();
		test.printArray();

		Random random = new Random();
//		for (int i = 0; i < 1000; i++) {
			int col = 3;//random.nextInt(5);
			int type = 1;//random.nextInt(2) + 1;
			int row = test.addElement(col,type);
			if(row != -1){
				test.updateArray(row, col, type);
			}else{
				System.out.println("this test is end");
			}
			
		test.printArray();
//		}
	}

	private int addElement(int col, int type) {
		int row = getReserveEmptyRow(col);
		if (row != -1) {
			changeArrayType(row,col,type);
		}
		return row;
	}

	private void updateArray(int row, int col, int type) {
		List<int[]> aroundList = new ArrayList<int[]>();
		aroundList.add(new int[]{row,col});
		findAroundSameType(row, col, type,aroundList);
		if(aroundList.size()>3){
			clearSameType(aroundList);
			
			List<int[]> movedList = moveElement();
			for(int[] temp:movedList){
				int row1 = temp[0];
				int col1 = temp[1];
				if(mArray[row1][col1] != 0){
					updateArray(row1,col1,mArray[row1][col1]);
				}
			}
		}
	}

	private void clearSameType(List<int[]> aroundList) {
		for(int[] temp:aroundList){
			int row = temp[0];
			int col = temp[1];
			changeArrayType(row,col,0);
		}		
	}
	
	private List<int[]> moveElement(){
		List<int[]> needMoveList = new ArrayList<int[]>();
		
		for (int j = 0; j < arrayCol; j++) {
			boolean isZero = false;
			for (int i = arrayRow-1 ; i >= 0; i--) {
				if( mArray[i][j]==0){
					isZero = true;
				}else if(isZero){
					needMoveList.add(new int[]{i,j});
					addElement(j, mArray[i][j]);
					changeArrayType(i,j,0);
				}
			}
		}
		
		return needMoveList;
	}

	private void findAroundSameType(int row, int col, int type,
			List<int[]> aroundList) {

		int[] temp = new int[] { row-1, col };
		if (row - 1 >=0 && mArray[row-1][col] == type && !isContain(aroundList,temp)) {			
				aroundList.add(temp);
				findAroundSameType(temp[0], temp[1], type, aroundList);
		}
		
		temp = new int[] { row + 1, col };
		if (row + 1 < arrayRow && mArray[row + 1][col] == type && !isContain(aroundList,temp)) {
				aroundList.add(temp);
				findAroundSameType(temp[0], temp[1], type, aroundList);
		}

		temp = new int[] { row, col - 1 };
		if (col - 1 >= 0 && mArray[row][col - 1] == type && !isContain(aroundList,temp)) {
				aroundList.add(temp);
				findAroundSameType(temp[0], temp[1], type, aroundList);
		}
		
		temp = new int[] { row, col+1 };
		if (col + 1 < arrayCol && mArray[row][col + 1] == type && !isContain(aroundList,temp)) {
				aroundList.add(temp);
				findAroundSameType(temp[0], temp[1], type, aroundList);
		}
	}
	
	private boolean isContain(List<int[]> list,int[] array){
		for(int[] temp:list){
			if(temp[0]==array[0]&&temp[1]==array[1]){
				return true;
			}
		}
		return false;
	}

	private int getEmptyRow(int col) {
		int row = -1;
		for (int i = 0; i < arrayRow; i++) {
			if (mArray[i][col] != 0) {
				break;
			}
			row = i;
		}
		return row;
	}
	
	private int getReserveEmptyRow(int col) {
		int row = -1;
		for (int i = arrayRow-1; i >= 0; i--) {
			if (mArray[i][col] == 0) {
				row = i;
				break;
			}
		}
		return row;
	}

	private void printArray() {
		for (int i = 0; i < arrayRow; i++) {
			System.out.print("{");
			for (int j = 0; j < arrayCol; j++) {
				System.out.print(mArray[i][j] + ",");
			}
			System.out.print("}\n");
		}
		System.out.print("\n");
	}
	
	private void changeArrayType(int row,int col,int type){
		System.out.println("updateArray "+row+","+col+","+type);
		mArray[row][col] = type;
		printArray();
	}

}
