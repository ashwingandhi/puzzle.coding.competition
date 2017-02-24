/**
 *  Author: Ashwin Gandhi
 *  
 */

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FMPuzzleDynamic {
	String[][] keypad = { { "1", "A", "B", "C", "3" },
			{ "D", "2", "E", "4", "F" }, { "G", "H", "I", "J", "K" },
			{ "", "L", "M", "N", "" } };

	String[] keylist = { "1", "A", "B", "C", "3", "D", "2", "E", "4", "F", "G",
			"H", "I", "J", "K", "", "L", "M", "N", "" };

	String[] moves = { "NNW", "NNE", "EEN", "EES", "SSE", "SSW", "WWN", "WWS" };

	// Adjacency matrix
	int[][] A = new int[20][20];

	// path data structure
	List<Map<String, BigInteger>> totalpaths = new ArrayList<Map<String, BigInteger>>();

	/**
	 * Generates Adjacency Matrix
	 */
	protected void generateAdjMatrix() {
		for (int i = 0; i < 20; i++) {
			if (keylist[i].equals("")) {
				continue;
			}
			for (int k = 0; k < moves.length; k++) {
				calculateAdjRow(moves[k], i);
			}
		}

		// To print the Adjacency matrix
		/*
		 * for (int m = 0; m < A.length; m++) { System.out.print("\'" +
		 * keylist[m] + "\'" + "\t"); for (int n = 0; n < A[0].length; n++) {
		 * System.out.print(A[m][n] + " "); } System.out.println(); }
		 */
	}
	
	/**
	 * Calculates a row of Adjacency Matrix
	 */
	protected void calculateAdjRow(String fidMove, int mi) {
		// unflatten
		int ki = mi / 5;
		int kj = mi % 5;

		for (int k = 0; k < fidMove.length(); k++) {
			if (fidMove.charAt(k) == 'N') {
				ki--;
				if (ki < 0 || keypad[ki][kj].equals("")) {
					return;
				}
			}
			if (fidMove.charAt(k) == 'S') {
				ki++;
				if (ki > 3 || keypad[ki][kj].equals("")) {
					return;
				}
			}
			if (fidMove.charAt(k) == 'E') {
				kj++;
				if (kj > 4 || keypad[ki][kj].equals("")) {
					return;
				}
			}
			if (fidMove.charAt(k) == 'W') {
				kj--;
				if (kj < 0 || keypad[ki][kj].equals("")) {
					return;
				}
			}
		}
		// flatten
		int mj = (ki * 5) + kj;
		if (keylist[mj].equals("")) {
			A[mi][mj] = 0;
			return;
		}
		A[mi][mj] = 1;
	}

	/*
	 * Calculates count for single move
	 */
	protected void calculateInitialPath(int n) {
		for (int i = 0; i < A.length; i++) {
			// System.out.println(keylist[i] + " goes to - ");
			int counter0 = 0;
			int counter1 = 0;
			int counter2 = 0;
			for (int j = 0; j < A[0].length; j++) {
				if (A[i][j] == 1) {
					// System.out.print(keylist[j] + ", ");
					if (Character.isDigit(keylist[i].charAt(0))
							&& Character.isDigit(keylist[j].charAt(0))) {
						counter2++;
					} else if (Character.isDigit(keylist[i].charAt(0))
							|| Character.isDigit(keylist[j].charAt(0))) {
						counter1++;
					} else {
						counter0++;
					}
				}
			}
			// System.out.println();
			HashMap<String, BigInteger> newMap = new HashMap<String, BigInteger>();
			newMap.put("zeroDigits", BigInteger.valueOf(counter0));
			newMap.put("oneDigit", BigInteger.valueOf(counter1));
			newMap.put("twoDigits", BigInteger.valueOf(counter2));
			//System.out.println(newMap.toString());
			totalpaths.add(i, newMap);
			counter0 = 0;
			counter1 = 0;
			counter2 = 0;
		}
		/*
		 * for (int i = 0; i < totalpaths.size(); i++) {
		 * System.out.println(keylist[i] + "-->");
		 * System.out.println(totalpaths.get(i)); }
		 */
		BigInteger totalCount = new BigInteger("0");
		for (int i = 0; i < keylist.length; i++) {
			// System.out.println(keylist[i] + "-->");
			// System.out.println(totalpaths.get(i));
			totalCount = totalCount.add(totalpaths.get(i).get("zeroDigits"));
			totalCount = totalCount.add(totalpaths.get(i).get("oneDigit"));
			totalCount = totalCount.add(totalpaths.get(i).get("twoDigits"));
		}
		System.out.println("********* " + totalCount + " for n = " + 2);
		
		calculateTotalpaths(n);
	}

	/**
	 * 
	 * Calculates count for remaining moves using the result from calculateInitialPath()
	 */
	protected void calculateTotalpaths(int n) {
		int c = 3;
		while (c <= n) {
			System.out
					.println(" =================================================");
			List<Map<String, BigInteger>> newTotalpaths = new ArrayList<Map<String, BigInteger>>();
			for (int i = 0; i < keylist.length; i++) {
				BigInteger counter0 = new BigInteger("0");
				BigInteger counter1 = new BigInteger("0");
				BigInteger counter2 = new BigInteger("0");
				List<Integer> myList = getAllIndexOfOne(i);
				for (int j = 0; j < myList.size(); j++) {
					if (Character.isDigit(keylist[i].charAt(0))) {
						/*counter1 += totalpaths.get(myList.get(j)).get(
								"zeroDigits");*/
						counter1 = totalpaths.get(myList.get(j)).get("zeroDigits").add(counter1);
						/*counter2 += totalpaths.get(myList.get(j)).get(
								"oneDigit");*/
						counter2 = totalpaths.get(myList.get(j)).get("oneDigit").add(counter2);
					} else {
						/*counter0 += totalpaths.get(myList.get(j)).get(
								"zeroDigits");
						counter1 += totalpaths.get(myList.get(j)).get(
								"oneDigit");
						counter2 += totalpaths.get(myList.get(j)).get(
								"twoDigits");*/
						counter0 = totalpaths.get(myList.get(j)).get("zeroDigits").add(counter0);
						counter1 = totalpaths.get(myList.get(j)).get("oneDigit").add(counter1);
						counter2 = totalpaths.get(myList.get(j)).get("twoDigits").add(counter2);
					}
				}
				HashMap<String, BigInteger> newMap = new HashMap<String, BigInteger>();
				newMap.put("zeroDigits", counter0);
				newMap.put("oneDigit", counter1);
				newMap.put("twoDigits", counter2);
				newTotalpaths.add(i, newMap);

			}

			totalpaths = newTotalpaths;
			BigInteger totalCount = new BigInteger("0");
			for (int i = 0; i < keylist.length; i++) {
				// System.out.println(keylist[i] + "-->");
				// System.out.println(totalpaths.get(i));
				totalCount = totalCount.add(totalpaths.get(i).get("zeroDigits"));
				totalCount = totalCount.add(totalpaths.get(i).get("oneDigit"));
				totalCount = totalCount.add(totalpaths.get(i).get("twoDigits"));
			}
			System.out.println("********* " + totalCount + " for n = " + c);
			c++;
		}
	}

	// returns a list of indices whose value is 1
	protected List<Integer> getAllIndexOfOne(int i) {
		List<Integer> indexList = new ArrayList<Integer>();
		for (int j = 0; j < 20; j++)
			if (A[i][j] == 1) {
				indexList.add(j);
			}
		return indexList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FMPuzzle1 f = new FMPuzzle1();
		f.generateAdjMatrix();
		f.calculateInitialPath(32);
	}
}
