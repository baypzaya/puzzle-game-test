import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Main {

	public Hashtable<Integer, Integer> circleRNMap = new Hashtable<Integer, Integer>();
	public Hashtable<Integer, Integer> circleBNMap = new Hashtable<Integer, Integer>();

	public Hashtable<Integer, ArrayList<Integer>> circleBNListMap = new Hashtable<Integer, ArrayList<Integer>>();
	public Hashtable<Integer, ArrayList<Integer>> circleRNListMap = new Hashtable<Integer, ArrayList<Integer>>();

	public int count;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Main main = new Main();
		main.initData();

		String xlsPath = "aa1.xls";
		File file = new File(xlsPath);
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(file);
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (wb == null)
			return;

		Sheet[] sheet = wb.getSheets();

		int[] rns = new int[6];
		int bn = 0;
		int rowNum = sheet[0].getRows();
		main.count = rowNum - 1;
		for (int j = 1; j < rowNum; j++) {
			Cell[] cells = sheet[0].getRow(j);
			for (int m = 2; m < 8; m++) {
				rns[m - 2] = Integer.parseInt(cells[m].getContents());
			}
			main.recordRN(rns);

			bn = Integer.parseInt(cells[8].getContents());
			main.recordBN(bn);
		}
		main.processRN();
		main.processBN();
	}

	private void processBN() {
		System.out.println("-----------process BN------------");
		ArrayList<NumberBall> result = process(circleBNListMap);
		Collections.sort(result, scaleComparator);
		for (NumberBall ball : result) {
			System.out.println(ball.getNumber() + " : " + ball.cycleScale);
		}
	}
	
	private void processRN() {
		System.out.println("-----------process RN------------");
		ArrayList<NumberBall> result = process(circleRNListMap);
		Collections.sort(result, scaleComparator);
		for (NumberBall ball : result) {
			System.out.println(ball.getNumber() + " : " + ball.cycleScale);
		}
	}
	
	
	private ArrayList<NumberBall> process( Hashtable<Integer, ArrayList<Integer>> data){
		ArrayList<NumberBall> result = new ArrayList<NumberBall>();
		count = data.keySet().size();
		for (int i = 1; i < count; i++) {
			NumberBall ball = new NumberBall(i);
			ball.cycleList = data.get(i);
			ball.cycleCount = data.get(i).size();
			ball.cycleScale = data.get(i).size() / (float) count;

			result.add(ball);
		}
		return result;
	}
	
	

	private void recordBN(int bn) {

		int circle = circleBNMap.get(bn);
		circleBNListMap.get(bn).add(circle);
		circleBNMap.put(bn, 0);
		for (int i = 1; i < 17; i++) {
			if (i != bn) {
				int c = circleBNMap.get(i) + 1;
				circleBNMap.put(i, c);
			}
		}
	}

	private void recordRN(int[] rns) {

		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int rn : rns) {
			int circle = circleRNMap.get(rn);
			circleRNListMap.get(rn).add(circle);
			circleRNMap.put(rn, 0);
			list.add(rn);
		}

		for (int i = 1; i < 34; i++) {
			if (!list.contains(i)) {
				int c = circleRNMap.get(i) + 1;
				circleRNMap.put(i, c);
			}
		}
	}

	private void initData() {
		for (int i = 1; i < 34; i++) {
			circleRNMap.put(i, 0);
			circleRNListMap.put(i, new ArrayList<Integer>());
		}

		for (int i = 1; i < 17; i++) {
			circleBNMap.put(i, 0);
			circleBNListMap.put(i, new ArrayList<Integer>());
		}
	}
	
	Comparator<NumberBall> scaleComparator = new Comparator<NumberBall>() {

		@Override
		public int compare(NumberBall arg0, NumberBall arg1) {
			if (arg0.cycleScale > arg1.cycleScale) {
				return 1;
			} else {
				return -1;
			}
		}
	};
}
