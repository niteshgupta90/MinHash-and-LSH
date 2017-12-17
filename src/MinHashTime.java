
import java.io.File;
import java.io.IOException;

public class MinHashTime {
	public static void main(String[] args) throws IOException {
		String dirName = args[0];
		int numPermuations = Integer.parseInt(args[1]);
		estamateTimes(dirName, numPermuations);
	}

	private static void estamateTimes(String dir, int numPerm) throws IOException {
		MinHash minHash = new MinHash(dir, numPerm);
		File fileDir = new File(dir);
		String[] files = fileDir.list();
		long minHashTime = 0, extactTime = 0;
		for (int i = 0; i < files.length - 1; i++) {
			for (int j = i + 1; j < files.length; j++) {
				long t1 = System.currentTimeMillis();
				minHash.exactJaccard(files[i], files[j]);
				long t2 = System.currentTimeMillis();
				minHash.approxJaccard(files[i], files[j]);
				long t3 = System.currentTimeMillis();
				extactTime += (t2 - t1);
				minHashTime += (t3 - t2);
			}
		}

		System.out.println("Time Taken for Build Min Hash : " + (double) minHash.getMinHashBuildTime() / 1000);
		System.out.println("Time Taken for Approx Jaccard (Including Build Time) : "
				+ ((double) (minHashTime + minHash.getMinHashBuildTime())) / 1000);
		System.out.println("Time Taken for Exact Jaccard : " + (double) extactTime / 1000);
	}
}
