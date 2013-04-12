/**
 * 
 */
package writers;

/**
 * @author Darshan
 *
 */
public class OffsetBlock {
	private int startOffset, endOffset;

	/**
	 * @param startOffset
	 * @param endOffset
	 */
	public OffsetBlock(int startOffset, int endOffset) {
		super();
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}

	/**
	 * @return the startOffset
	 */
	public int getStartOffset() {
		return startOffset;
	}

	/**
	 * @param startOffset the startOffset to set
	 */
	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}

	/**
	 * @return the endOffset
	 */
	public int getEndOffset() {
		return endOffset;
	}

	/**
	 * @param endOffset the endOffset to set
	 */
	public void setEndOffset(int endOffset) {
		this.endOffset = endOffset;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OffsetBlock [startOffset=" + startOffset + ", endOffset="
				+ endOffset + "]";
	}
	
}
