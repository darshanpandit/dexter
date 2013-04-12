/**
 * 
 */
package writers;

import java.util.Arrays;

/**
 * @author Darshan
 *
 */
public class OffsetInfo {

	private int[] offsetArray, contextArray;

	/**
	 * @param offsetArray
	 * @param contextArray
	 */
	public OffsetInfo(int[] offsetArray, int[] contextArray) {
		super();
		this.offsetArray = offsetArray;
		this.contextArray = contextArray;
	}

	/**
	 * @return the offsetArray
	 */
	public int[] getOffsetArray() {
		return offsetArray;
	}

	/**
	 * @param offsetArray the offsetArray to set
	 */
	public void setOffsetArray(int[] offsetArray) {
		this.offsetArray = offsetArray;
	}

	/**
	 * @return the contextArray
	 */
	public int[] getContextArray() {
		return contextArray;
	}

	/**
	 * @param contextArray the contextArray to set
	 */
	public void setContextArray(int[] contextArray) {
		this.contextArray = contextArray;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OffsetInfo [offsetArray=" + Arrays.toString(offsetArray)
				+ ", contextArray=" + Arrays.toString(contextArray) + "]";
	}
	
}
