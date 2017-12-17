

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Preprocessor {
	private static final String SPECIAL_WORD = "the";
	private static String[] punctuations = { ",", ":", "'", ";" };

	public static Set<String> preprocess(String fileName) throws IOException {
		Set<String> words = new HashSet<>();
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line = "";
		while ((line = reader.readLine()) != null) {
			String processed = removePunctuation(line);
			String[] ws = processed.split("\\s+");
			for (String word : ws) {
				if (word.length() >= 3 && !word.equals(SPECIAL_WORD)) {
					words.add(word.toLowerCase());
				}
			}
		}

		reader.close();
		return words;
	}

	private static String removePunctuation(String line) {
		for (String p : punctuations) {
			line = line.replaceAll(p, "");
		}
		
		line = line.replace(".", " ");

		return line;
	}
}
