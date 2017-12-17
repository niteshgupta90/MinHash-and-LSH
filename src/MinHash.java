
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MinHash {
	private int numPerm;
	private int M, N;
	private Map<String, List<Integer>> fileToTermIds = new HashMap<>();
	private int[][] minHash = null;
	private int[][] hashs = null;
	private Map<Integer, String> fileIndexFileNameMap = new HashMap<>();
	private Map<String, Integer> fileNameFileIdxMap = new HashMap<>();
	private long minHashBuildTime;

	private String getFileName(String dir, String file) {
		return dir + "/" + file;
	}

	public String[] allDocs() {
		return fileNameFileIdxMap.keySet().toArray(new String[fileIndexFileNameMap.size()]);
	}

	public int[] minHashSig(String filename) {
		int[] sig = new int[numPerm];
		int idx = fileNameFileIdxMap.get(filename);
		for (int i = 0; i < numPerm; i++) {
			sig[i] = minHash[i][idx];
		}
		
		return sig;
	}

	public MinHash(String dir, int numPermuatations) throws IOException {
		numPerm = numPermuatations;
		buildModel(dir);
		long startTime = System.currentTimeMillis();
		RandomPermutationGenerator generator = new RandomPermutationGenerator(numPermuatations, M);
		hashs = generator.generateRandomPerumations();
		minHash = new int[numPerm][N];
		buildMinHash();
		long endTime = System.currentTimeMillis();
		minHashBuildTime = endTime - startTime;
	}

	private void buildMinHash() {
		for (int i = 0; i < N; i++) {
			List<Integer> terms = fileToTermIds.get(fileIndexFileNameMap.get(i));
			for (int j = 0; j < numPerm; j++) {
				minHash[j][i] = getOneSignature(hashs[j], terms);
			}
		}
	}

	public double approxJaccard(String f1, String f2) {
		int idx1 = fileNameFileIdxMap.get(f1), idx2 = fileNameFileIdxMap.get(f2);
		int match = 0;
		for (int i = 0; i < numPerm; i++) {
			if (minHash[i][idx1] == minHash[i][idx2])
				match++;
		}

		return ((double) match / numPerm);
	}

	private int getOneSignature(int[] h, List<Integer> terms) {
		int min = Integer.MAX_VALUE;
		for (Integer term : terms) {
			min = Math.min(min, h[term]);
		}

		return min;
	}

	private void buildModel(String dir) throws IOException {
		File fileDir = new File(dir);
		Map<String, Set<String>> fileToTerms = new HashMap<>();
		Set<String> allTerms = new HashSet<>();
		for (String f : fileDir.list()) {
			String fileName = getFileName(dir, f);
			Set<String> terms = Preprocessor.preprocess(fileName);
			allTerms.addAll(terms);
			fileToTerms.put(f, terms);
			fileIndexFileNameMap.put(N, f);
			fileNameFileIdxMap.put(f, N);
			N++;
		}
		this.M = allTerms.size();
		Map<String, Integer> m = new HashMap<>();
		int count = 1;
		for (String s : allTerms) {
			if (!m.containsKey(s))
				m.put(s, count++);
		}

		for (String f : fileToTerms.keySet()) {
			Set<String> terms = fileToTerms.get(f);
			List<Integer> Ids = new ArrayList<>();
			for (String t : terms) {
				Ids.add(m.get(t));
			}
			fileToTermIds.put(f, Ids);
		}
	}

	public double exactJaccard(String f1, String f2) {
		List<Integer> t1 = fileToTermIds.get(f1);
		List<Integer> t2 = fileToTermIds.get(f2);

		Set<Integer> s = new HashSet<>(t1);
		int common = 0, union = t1.size();
		for (Integer elm : t2) {
			if (s.contains(elm))
				common++;
			else
				union++;
		}
		return ((double) common / union);
	}

	public int[][] minHashMatrix() {
		return this.minHash;
	}

	public int numTerms() {
		return M;
	}

	public int numPermuatation() {
		return numPerm;
	}

	public long getMinHashBuildTime() {
		return minHashBuildTime;
	}

	public Map<String, Integer> getFileNameFileIdxMap() {
		return fileNameFileIdxMap;
	}

}
