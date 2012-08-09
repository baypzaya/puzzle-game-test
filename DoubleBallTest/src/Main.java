import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Main {

	public Hashtable<Integer, Integer> circleRNMap = new Hashtable<Integer, Integer>();
	public Hashtable<Integer, Integer> circleBNMap = new Hashtable<Integer, Integer>();

	public Hashtable<Integer, ArrayList<Integer>> circleBNListMap = new Hashtable<Integer, ArrayList<Integer>>();

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
		for (int j = 1; j < rowNum; j++) {
			Cell[] cells = sheet[0].getRow(j);
			for (int m = 2; m < 8; m++) {
				rns[m - 2] = Integer.parseInt(cells[m].getContents());
			}
			main.processRN(rns);

			bn = Integer.parseInt(cells[8].getContents());
			main.recordBN(bn);

			// System.out.println(rns[0] + "," + rns[1] + "," + rns[2] + "," +
			// rns[3] + "," + rns[4] + "," + rns[5] + "   " + bn);
		}

		main.processBN();
	}

	private void processBN() {
		for (int i = 1; i < 17; i++) {
			int count = 0;
			for (Integer c : circleBNListMap.get(i)) {
				count += c;
			}
			System.out.println(i+"_size:"+circleBNListMap.get(i).size());
			System.out.println(i+"_count:"+count);
			System.out.println(i+"_avg:"+(count/ circleBNListMap.get(i).size()));
		}
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

	private void processRN(int[] rns) {

	}

	private void initData() {
		for (int i = 1; i < 34; i++) {
			circleRNMap.put(i, 0);
		}

		for (int i = 1; i < 17; i++) {
			circleBNMap.put(i, 0);
			circleBNListMap.put(i, new ArrayList<Integer>());
		}
	}
}
