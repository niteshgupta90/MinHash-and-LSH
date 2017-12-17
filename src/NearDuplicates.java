import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class NearDuplicates {
	public static void main(String[] args) throws IOException {
		String dirName = args[0];
		int numPermuations = Integer.parseInt(args[1]);
		double error = Double.parseDouble(args[2]);
		String docName = args[3];
		Set<String> simDocList = new HashSet<String>();
		simDocList = nearDuplciateDetector(dirName, numPermuations, error, docName);
		System.out.println("Similar documents list : ");
		for (String s : simDocList) {
			System.out.println(s);
		}
	}

	private static Set<String> nearDuplciateDetector(String dir, int numPerm, double err, String docName)
			throws IOException {
		Set<String> simDocList = new HashSet<String>();
		int bands = calculateBands(numPerm, err);
		System.out.println("Num of Bands = "+bands);
		MinHash minHash = new MinHash(dir, numPerm);
		File fileDir = new File(dir);
		String[] files = fileDir.list();
		LSH lsh = new LSH(minHash.minHashMatrix(), files, bands);
		lsh.setDocToIdxMap(minHash.getFileNameFileIdxMap());
		simDocList = lsh.nearDuplicatesOf(docName);

		Set<String> simDoc = new HashSet<String>();
		for (String s : simDoc) {
			double approxJ = minHash.approxJaccard(docName, s);
			if (approxJ > err)
				simDocList.add(s);
		}

		return simDocList;
	}

	private static int calculateBands(int numPerm, double threshold) {
		double reverseOfPow = 1 / Math.pow(threshold, numPerm);
		for (int band = 1;; band++) {
			if (Math.pow(band, band) < reverseOfPow && Math.pow(band + 1, band + 1) > reverseOfPow) {
				if ((reverseOfPow - Math.pow(band, band)) < (Math.pow((band + 1), (band + 1)) - reverseOfPow))
					return band;
				else
					return band + 1;
			}
		}
	}
}
