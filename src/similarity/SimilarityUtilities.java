package similarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sets.SetUtilities;

public class SimilarityUtilities {
	/**
	 * Returns the set of non-empty lines contained in a text, trimmed of
	 * leading and trailing whitespace.
	 * 
	 * @param text
	 * @return the trimmed set of lines
	 */
	public static Set<String> trimmedLines(String text) {
		Set<String> trimmed = new HashSet<String>();
		String[] splitlines = text.split("\\n");
		for (String x : splitlines) {
			if (!(x.trim().isEmpty())) {
				trimmed.add(x.trim());
			}
		}
		return trimmed;
	}

	/**
	 * Returns a list of words in the text, in the order they appeared in the text,
	 * converted to lowercase.
	 * 
	 * Words are defined as a contiguous, non-empty sequence of letters and numbers.
	 *
	 * @param text
	 * @return a list of lowercase words
	 */
	public static List<String> asLowercaseWords(String text) {
		List<String> lowered = new ArrayList<String>();
		String[] splitlines = text.split("\\n");
		for (String x : splitlines) {
			if (!(x.trim().isEmpty())) {
				String[] words = x.trim().toLowerCase().split("\\W+");
				lowered.addAll(Arrays.asList(words));
			}
		}
		return lowered;
	}

	/**
	 * Returns the line-based similarity of two texts.
	 * 
	 * The line-based similarity is the Jaccard index between each text's line
	 * set.
	 */
	public static double lineSimilarity(String text1, String text2) {
		return SetUtilities.jaccardIndex(trimmedLines(text1), trimmedLines(text2));
	}

	/**
	 * Returns the line-based similarity of two texts.
	 * 
	 * The line-based similarity is the Jaccard index between each text's line
	 * set.
	 * 
	 * A text's line set is the set of trimmed lines in that text, as defined by
	 * trimmedLines, less the set of trimmed lines from the templateText. Removes
	 * the template text from consideration after trimming lines, not before.
	 */
	public static double lineSimilarity(String text1, String text2, String templateText) {
		Set<String> first = trimmedLines(text1);
		Set<String> second = trimmedLines(text2);
		Set<String> template = trimmedLines(templateText);
		first = SetUtilities.setDifference(first, template);
		second = SetUtilities.setDifference(second, template);
		return SetUtilities.jaccardIndex(first, second);
	}

	/**
	 * Returns a set of strings representing the shingling of the given length
	 * of a list of words.
	 * 
	 * A shingling of length k of a list of words is the set of all k-shingles
	 * of that list.
	 * 
	 * A k-shingle is the concatenation of k adjacent words.
	 * 
	 * @param words
	 * @param shingleLength
	 * @return
	 */
	public static Set<String> shingle(List<String> words, int shingleLength) {
		Set<String> toReturn = new HashSet<String>();
		for (int x = 0; x < words.size() - shingleLength + 1; x++) {
			String combined = "";
			for (int i = 0; i < shingleLength; i++) {
				combined = combined + words.get(i + x);
			}
			toReturn.add(combined);
		}
		return toReturn;
	}

	/**
	 * Returns the shingled word similarity of two texts.
	 * 
	 * The shingled word similarity is the Jaccard index between each text's
	 * shingle set.
	 * 
	 * A text's shingle set is the set of shingles (of the given length) for the
	 * entire text, as defined by shingle and asLowercaseWords,
	 * less the shingle set of the templateText. Removes the templateText
	 * from consideration after shingling, not before.
	 * 
	 * @param text1
	 * @param text2
	 * @param templateText
	 * @param shingleLength
	 * @return
	 */
	public static double shingleSimilarity(String text1, String text2, String templateText, int shingleLength) {
		Set<String> text1fixed = new HashSet<String>();
		Set<String> text2fixed = new HashSet<String>();
		Set<String> template = new HashSet<String>();
		text1fixed.addAll(shingle(asLowercaseWords(text1), shingleLength));
		text2fixed.addAll(shingle(asLowercaseWords(text2), shingleLength));
		template.addAll(shingle(asLowercaseWords(templateText), shingleLength));
		text1fixed = SetUtilities.setDifference(text1fixed, template);
		text2fixed = SetUtilities.setDifference(text2fixed, template);
		return SetUtilities.jaccardIndex(text1fixed, text2fixed);
	}
}
