/**
 * 
 */
package lexicon;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Darshan
 * 
 */
public class LexiconValue implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8826253705045189110L;
	private List<BlockInfo> blockInfos;
	private int globalFrequency;
	
	public LexiconValue(LinkedList<BlockInfo> linkedList, int i) {
		this.blockInfos = linkedList;
		this.globalFrequency = i;
	}
	
	

	/**
	 * @return the blockInfos
	 */
	public List<BlockInfo> getBlockInfos() {
		return blockInfos;
		
	}

	/**
	 * @param blockInfos
	 *            the blockInfos to set
	 */
	public void setBlockInfos(List<BlockInfo> blockInfos) {
		this.blockInfos = blockInfos;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LexiconValue [blockInfos=" + blockInfos + "]";
	}

	public void incrGlobalFreq(int freq) {
		globalFrequency+= freq;
		
	}



	/**
	 * @return the globalFrequency
	 */
	public int getGlobalFrequency() {
		return globalFrequency;
	}



	/**
	 * @param globalFrequency the globalFrequency to set
	 */
	public void setGlobalFrequency(int globalFrequency) {
		this.globalFrequency = globalFrequency;
	}

	
}
