package indexing;

public class Posting implements Comparable<Posting> {
	private int termID, docID;
	//private int offset, contextID;

	@Override
	public int compareTo(Posting o) {
		if(this.getTermID()!=o.getTermID())
			return (this.getTermID()>o.getTermID()? 1:-1);
		if(this.getDocID()!=o.getDocID())
			return (this.getDocID()>o.getDocID()? 1:-1);
		//if(this.getOffset()!=o.getOffset())
		//	return (this.getOffset()>o.getOffset()? 1:-1);
		//if(this.getContextID()!=o.getContextID())
		//	return (this.getContextID()>o.getContextID()? 1:-1);
		return 0;
		
	}

	
	
	
	
	
	/**
	 * @param termID
	 * @param docID
	 * @param offset
	 * @param contextID
	 */
	public Posting(int termID, int docID) {
		super();
		this.termID = termID;
		this.docID = docID;
	//	this.offset = offset;
	//	this.contextID = contextID;
	}

	/**
	 * 
	 */
	public Posting() {
		super();
		this.termID = 		0;
		this.docID = 		0;
	//	this.offset = 		0;
	//	this.contextID =	0;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Posting [termID=" + termID + ", docID=" + docID + "]";
	}
	
	

}
