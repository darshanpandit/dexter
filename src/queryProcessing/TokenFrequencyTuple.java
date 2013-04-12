/**
 * 
 */
package queryProcessing;

/**
 * @author Darshan
 *
 */
public class TokenFrequencyTuple implements Comparable<TokenFrequencyTuple> {
	private int termID,frequency;

	/**
	 * @param termID
	 * @param frequency
	 */
	public TokenFrequencyTuple(int termID, int frequency) {
		super();
		this.termID = termID;
		this.frequency = frequency;
	}

	/**
	 * @return the termID
	 */
	public int getTermID() {
		return termID;
	}

	/**
	 * @param termID the termID to set
	 */
	public void setTermID(int termID) {
		this.termID = termID;
	}

	/**
	 * @return the frequency
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	@Override
	public int compareTo(TokenFrequencyTuple o) {
		if (this.frequency == o.getFrequency()) return 0;
		else
			return (this.frequency>o.frequency)?1:-1;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TokenFrequencyTuple [termID=" + termID + ", frequency="
				+ frequency + "]";
	};
	
	

}
