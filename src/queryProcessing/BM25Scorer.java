/**
 * 
 */
package queryProcessing;

import java.util.List;

/**
 * @author Darshan
 * 
 */
public class BM25Scorer implements Scorer {
	private double k1, b;

	/**
	 * @param k1
	 * @param b
	 */
	public BM25Scorer(double k1, double b) {
		super();
		this.k1 = k1;
		this.b = b;
	}

	@Override
	public double score(int totalDocuments,
			List<Integer> globalDocumentFrequencyVector,
			List<Integer> localDocumentFrequencyVector, int documentLength,
			double avgDocumentLength) {
		double score = 0;

		double constFactorK = k1
				* ((1 - b) + (b * documentLength / avgDocumentLength));

		for (int i = 0; i < globalDocumentFrequencyVector.size(); i++) {
			double factor1 = ((totalDocuments - globalDocumentFrequencyVector.get(i) + 0.5) / (globalDocumentFrequencyVector
					.get(i) + 0.5));
			
			double factor2 = (((k1 + 1) * (double)localDocumentFrequencyVector.get(i)) / (constFactorK +(double) localDocumentFrequencyVector
					.get(i)));
			
			
			score += Math.log(factor1)*factor2; 
		}

		return score;
	}

}
