package indexing;

import java.io.Serializable;

public class CorpusStatistics implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 114248531690435365L;
	private int totalDocuments, totalPostings;

	/**
	 * @param totalDocuments
	 * @param totalPostings
	 */
	public CorpusStatistics(int totalDocuments, int totalPostings) {
		super();
		this.totalDocuments = totalDocuments;
		this.totalPostings = totalPostings;
	}

	/**
	 * @return the totalDocuments
	 */
	public int getTotalDocuments() {
		return totalDocuments;
	}

	/**
	 * @param totalDocuments the totalDocuments to set
	 */
	public void setTotalDocuments(int totalDocuments) {
		this.totalDocuments = totalDocuments;
	}

	/**
	 * @return the totalPostings
	 */
	public int getTotalPostings() {
		return totalPostings;
	}

	/**
	 * @param totalPostings the totalPostings to set
	 */
	public void setTotalPostings(int totalPostings) {
		this.totalPostings = totalPostings;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CorpusStatistics [totalDocuments=" + totalDocuments
				+ ", totalPostings=" + totalPostings + "]";
	}
	
	

}
