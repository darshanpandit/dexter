/**
 * 
 */
package lexicon;

import java.io.Serializable;

/**
 * @author Darshan
 *
 */
public class BlockInfo implements  Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7989954884023179545L;
	private int fileNumber;
	private int startOffset;
	private int endOffset;
	/**
	 * @param fileNumber
	 * @param startOffset
	 * @param endOffset
	 */
	public BlockInfo(int fileNumber, int startOffset, int endOffset) {
		super();
		this.fileNumber = fileNumber;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}
	/**
	 * @return the fileNumber
	 */
	public int getFileNumber() {
		return fileNumber;
	}
	/**
	 * @param fileNumber the fileNumber to set
	 */
	public void setFileNumber(int fileNumber) {
		this.fileNumber = fileNumber;
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
		return "BlockInfo [fileNumber=" + fileNumber + ", startOffset="
				+ startOffset + ", endOffset=" + endOffset + "]";
	}
	
}
