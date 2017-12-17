
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class LSH {
	int bands;
	String[] docNames;
	int[][] minHashMatrix;
	int r;
	int k;
	int N;
	HashMap<Integer, HashSet<String>>[] map;
	Map<String, Integer> docToIdxMap = null;

	@SuppressWarnings("unchecked")
	public LSH(int[][] minHashMatrix, String[] docNames, int bands) {
		this.minHashMatrix = minHashMatrix;
		this.docNames = docNames;
		this.bands = bands;
		k = minHashMatrix.length;
		N = minHashMatrix[0].length;
		r = k / bands;
		map = new HashMap[bands];
		for (int i = 0; i < bands; i++) map[i] = new HashMap<>();
		storeBands();
	}

	public void storeBands() {
		for (int i = 0; i < N; i++) {
			for (int b = 0; b < bands; b++) {
				StringBuilder sb = new StringBuilder();
				for (int k = r * b; k < r * (b + 1); k++) {
					sb.append(Integer.toString(minHashMatrix[k][i]));
					sb.append(",");
				}
				int hC = sb.toString().hashCode();
				HashSet<String> nameSet;
				if (map[b].containsKey(hC)) {
					nameSet = map[b].get(hC);
				} else {
					nameSet = new HashSet<String>();
				}
				nameSet.add(docNames[i]);
				map[b].put(hC, nameSet);
			}
		}
	}

	public HashSet<String> nearDuplicatesOf(String docName) {
		HashSet<String> listOfNearDuplciates = new HashSet<String>();
		int docIndex = docToIdxMap.get(docName);

		for (int b = 0; b < bands; b++) {
			StringBuilder sb = new StringBuilder();
			for (int k = r * b; k < r*(b+1); k++) {
				sb.append(Integer.toString(minHashMatrix[k][docIndex]));
				sb.append(",");
			}
			int hC = sb.toString().hashCode();
			listOfNearDuplciates.addAll(map[b].get(hC));
		}
		return listOfNearDuplciates;
	}

	public Map<String, Integer> getDocToIdxMap() {
		return docToIdxMap;
	}

	public void setDocToIdxMap(Map<String, Integer> docToIdxMap) {
		this.docToIdxMap = docToIdxMap;
	}

}
