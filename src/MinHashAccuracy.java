
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MinHashAccuracy {
	private static String dirName = "space";
	private static double[] erros = { 0.04, 0.07, 0.08 };
	private static int[] perms = { 400, 600, 800 };

	public static void main(String[] args) throws IOException {
//		for (int k : perms) {
//			for (double err : erros) {
//				System.out.println("(K, err) : (" + k + ", " + err + ") : " + accuracy(dirName, k, err));
//			}
//		}
		String dirName = args[0];
		int numPermuations = Integer.parseInt(args[1]);
		double err = Double.parseDouble(args[2]);
		System.out.println(
				"(K, err) : (" + numPermuations + ", " + err + ") : " + accuracy(dirName, numPermuations, err));
	}

	private static int accuracy(String dir, int numPerm, double err) throws IOException {
		int failed = 0;
		MinHash minHash = new MinHash(dir, numPerm);
		File fileDir = new File(dir);
		String[] files = fileDir.list();
		for (int i = 0; i < files.length - 1; i++) {
			for (int j = i + 1; j < files.length; j++) {
				double exactJ = minHash.exactJaccard(files[i], files[j]);
				double approxJ = minHash.approxJaccard(files[i], files[j]);
				if (Math.abs(exactJ - approxJ) > err)
					failed++;
			}
		}
		return failed;
	}
}
