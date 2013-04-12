/**
 * 
 */
package indexing;

import java.util.List;

import writers.OffsetBlock;

/**
 * @author Darshan
 *
 */
public class WrapperIndexEntry {
	private BasicIndexEntry basicIndexEntry;
	private int currentPosition;
	/**
	 * @param basicIndexEntry
	 */
	public WrapperIndexEntry(BasicIndexEntry basicIndexEntry) {
		super();
		this.basicIndexEntry = basicIndexEntry;
		this.currentPosition =0;
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return basicIndexEntry.equals(obj);
	}

	/**
	 * @param position
	 * @return
	 * @see indexing.BasicIndexEntry#getDocID(int)
	 */
	public int getDocID(int position) {
		return basicIndexEntry.getDocID(position);
	}

	/**
	 * @param position
	 * @return
	 * @see indexing.BasicIndexEntry#getFrequency(int)
	 */
	public int getFrequency(int position) {
		return basicIndexEntry.getFrequency(position);
	}

	/**
	 * 
	 * @return
	 */
	public int getCurrentPosition() {
		return currentPosition;
	}
	
	/**
	 * @param lastPositionChecked
	 *            the lastPositionChecked to set
	 */

	public void setCurrentPosition(int lastPositionChecked) {
		this.currentPosition = lastPositionChecked;
	}

	/**
	 * @param position
	 * @return
	 * @see indexing.BasicIndexEntry#getOffsetBlock(int)
	 */
	public OffsetBlock getOffsetBlock(int position) {
		return basicIndexEntry.getOffsetBlock(position);
	}

	/**
	 * @return
	 * @see indexing.BasicIndexEntry#getReaderFrequency()
	 */
	public int getReaderFrequency() {
		return basicIndexEntry.getReaderFrequency();
	}

	/**
	 * @return
	 * @see indexing.BasicIndexEntry#getTermFrequency()
	 */
	public int getTermFrequency() {
		return basicIndexEntry.getTermFrequency();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return basicIndexEntry.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WrapperIndexEntry [basicIndexEntry=" + basicIndexEntry
				+ ", currentPosition=" + currentPosition + "]";
	}

	//**********************************************************************************************************************
	//Static Methods
	
		public static boolean incrIndex(WrapperIndexEntry indexEntry){
			int nextIndex = indexEntry.getCurrentPosition();
			//To avoid reading more docs than present
			if(!((nextIndex+1)<(indexEntry.getTermFrequency()) ) )
				return false;
			nextIndex++;
			indexEntry.setCurrentPosition(nextIndex);
			return true;
			
		}
		
		/**
		 * 
		 * @param indexEntry
		 * @param DocID
		 * @param detailedPosting
		 * @return int
		 */
		public static int nextGEQ(WrapperIndexEntry indexEntry, int DocID,
				boolean detailedPosting) {
			int position = indexEntry.getCurrentPosition();
			while (indexEntry.getDocID(position) < DocID) {
				position++;
				if (position >= indexEntry.getTermFrequency()) {
					position = indexEntry.getTermFrequency();
					return -1;
				}
			}
			indexEntry.setCurrentPosition(position);
			return indexEntry.getDocID(position);
		}

		public static int currentFrequency(WrapperIndexEntry indexEntry){
			return indexEntry.getFrequency(indexEntry.getCurrentPosition());
		}

		public static Integer currentDoc(WrapperIndexEntry indexEntry) {
			return indexEntry.getDocID(indexEntry.currentPosition);
		}

		public int getMaxDocID() {
			return basicIndexEntry.getMaxDocID();
		}


}
