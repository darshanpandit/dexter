/**
 * 
 */
package queryProcessing;

/**
 * @author Darshan
 *
 */
public class DocumentScore {
	private int docID;
	private double score;

	/**
	 * @param docID
	 * @param score
	 */
	public DocumentScore(int docID, double  score) {
		super();
		this.docID = docID;
		this.score = score;
	}

	/**
	 * @return the docID
	 */
	public int getDocID() {
		return docID;
	}

	/**
	 * @param docID the docID to set
	 */
	public void setDocID(int docID) {
		this.docID = docID;
	}

	/**
	 * @return the score
	 */
	public double  getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(double  score) {
		this.score = score;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DocumentScore [docID=" + docID + ", score=" + score + "]";
	}
	

}
