import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class FMPuzzleRecursive {

	static BigInteger globalCounter = new BigInteger("0");
	static int sysoC = 0;
	// String startkey = "";
	String[][] keypad = { { "1", "A", "B", "C", "3" },
			{ "D", "2", "E", "4", "F" }, { "G", "H", "I", "J", "K" },
			{ "", "L", "M", "N", "" } };

	String[] keylist = { "1", "A", "B", "C", "3", "D", "2", "E", "4", "F", "G",
			"H", "I", "J", "K", "", "L", "M", "N", "" };

	String[] moves = { "NNW", "NNE", "EEN", "EES", "SSE", "SSW", "WWN", "WWS" };

	int[][] A = new int[20][20];

	public void generateAdj() {
		for (int i = 0; i < 20; i++) {
			if (keylist[i].equals("")) {
				continue;
			}
			for (int k = 0; k < moves.length; k++) {
				calAdjRow(moves[k], i);

			}
		}
		for (int m = 0; m < A.length; m++) {
			System.out.print(keylist[m]+ "   ");
			for (int n = 0; n < A[0].length; n++) {
				System.out.print(A[m][n] + " ");
			}
			System.out.println();
		}
	}

	private void calAdjRow(String string, int mi) {

		int ki = mi / 5;
		int kj = mi % 5;

		for (int k = 0; k < string.length(); k++) {
			if (string.charAt(k) == 'N') {
				ki--;
				if (ki < 0) {
					return;
				}
				if(keypad[ki][kj].equals("")){
					return;
				}
			}
			if (string.charAt(k) == 'S') {
				ki++;
				if (ki > 3) {
					return;
				}
				if(keypad[ki][kj].equals("")){
					return;
				}
			}
			if (string.charAt(k) == 'E') {
				kj++;
				if (kj > 4) {
					return;
				}
				if(keypad[ki][kj].equals("")){
					return;
				}
			}
			if (string.charAt(k) == 'W') {
				kj--;
				if (kj < 0) {
					return;
				}
				if(keypad[ki][kj].equals("")){
					return;
				}
			}
		}
		int mj = (ki * 5) + kj;
		if (keylist[mj].equals("")) {
			A[mi][mj] = 0;
			return;
		}
		A[mi][mj] = 1;

	}

	/**
	 * 
	 * @param n
	 *            : total no. of keys in a sequence
	 */
	public void calPaths(int n) {
		n = n - 1; // total number of jumps = (no. of keys) -1
		for (int i = 0; i < 20; i++) {
			if (keylist[i].equals("")) {
				continue;
			}
			if (Character.isDigit(keylist[i].charAt(0))) {
				//System.out.print(  keylist[i] + "=>");
				//sysoC++;
				recur(i, n, 1);
			} else {
				//System.out.print( keylist[i] + "=>");
				//sysoC++;
				recur(i, n, 2);
			}
		}
	}

	/**
	 * 
	 * @param i
	 * @param n
	 *            : total no. of jumps
	 * @param lmt
	 *            : total no. of digits (limit is 2)
	 */
	public void recur(int i, int n, int lmt) {
		if (n < 1) {
			globalCounter = globalCounter.add(BigInteger.valueOf(1));
			//System.out.println("End");
			return;
		}
		List<Integer> allIndex = getAllIndexOfOne(i);
		for (int ni : allIndex) {
			if (Character.isDigit(keylist[ni].charAt(0))) {
				//System.out.print(  keylist[ni] + "=>");
				if (lmt == 0) {
					 continue;
				 }
				recur(ni, n - 1, lmt - 1);
				
			} else {
				//System.out.print(  keylist[ni] + "=>");
				recur(ni, n - 1, lmt);
			}
		}
	}

	public List<Integer> getAllIndexOfOne(int i) {
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
		FMPuzzle f = new FMPuzzle();
		f.generateAdj();
		f.calPaths(32);
		System.out.println(globalCounter);

	}

}
