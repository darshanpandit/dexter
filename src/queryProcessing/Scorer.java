/**
 * 
 */
package queryProcessing;

import java.util.List;

/**
 * @author Darshan
 *
 */
public interface Scorer {
	public double score(int totalDocuments, List<Integer> globalDocumentFrequencyVector, List<Integer> localDocumentFrequencyVector, int documentLength, double avgDocumentLength);
}
