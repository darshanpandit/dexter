/**
 * 
 */
package indexing;

import java.util.ArrayList;
import java.util.List;

import writers.OffsetBlock;

import lexicon.BlockInfo;

import compression.MyCompression;

/**
 * @author Darshan
 *
 */
public class BasicIndexEntry {

	private int termID;
	private List<Integer> docVector;
	private List<Integer> freqVector;
	private List<Integer> offsetBlockPosition;
	private List<Integer> offsetBlockLength;
	private int readerFrequency;
	
	public BasicIndexEntry(int termID, BlockInfo blockInfo, byte[] bytes) {
		this.termID = termID;
		// lastPositionChecked = 0;
		
		List<Integer> list = MyCompression.decode(bytes);
		
		//docVector = new ArrayList<Integer>(list.size()/4);
		//We decompress the delta compressed docVector and we hence read it in an array first.
		
		freqVector = new ArrayList<Integer>(list.size()/2);
		//offsetBlockPosition = new ArrayList<Integer>(list.size()/4);
		//offsetBlockLength = new ArrayList<Integer>(list.size()/4);
		
		int[] tempDocArray = new int[list.size()/2];
		
		int i=0;
		int j = list.size()/2;
		int k = j*2;
		int l = j*3;
		readerFrequency = 0;
		
		for(;i<list.size()/2;i++,j++,k++,l++){
			tempDocArray[i] = list.get(i);
			freqVector.add(list.get(j));
			readerFrequency+= list.get(j);
			//offsetBlockPosition.add(list.get(k));
			//offsetBlockLength.add(list.get(l));
		}
		
		
		docVector = MyCompression.deltaDeCompress(tempDocArray);
		
		/*
		System.out.println("Size of Byte Array "+ bytes.length);
		System.out.println("Size of Doc Vector "+ docVector.size());
		System.out.println("Size of Freq Vector "+ freqVector.size());
		System.out.println("Size of Offset Vector "+ offsetVector.size());
		System.out.println("Size of Context Vector "+contextVector.size());
		*/
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxDocID(){
		return docVector.get(docVector.size()-1);
	}
	
	/**
	 * 
	 * @param position
	 * @return
	 */
	public int getDocID(int position) {
		try {
			return this.docVector.get(position);
		} catch (IndexOutOfBoundsException e) {
			return -1;
		}
	}
	
	/**
	 * 
	 * @param position
	 * @return
	 */
	public int getFrequency(int position) {
		try {
			return this.freqVector.get(position);
		} catch (IndexOutOfBoundsException e) {
			return -1;
		}
	}
	
	/**
	 * 
	 * @param position
	 * @return
	 */
	public OffsetBlock getOffsetBlock(int position){
		try {
			return new OffsetBlock(this.offsetBlockPosition.get(position), (this.offsetBlockPosition.get(position)+this.offsetBlockLength.get(position) ) );
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * @return the readerFrequency
	 */
	public int getReaderFrequency() {
		return readerFrequency;
	}
	
	public int getTermFrequency(){
		return this.docVector.size();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BasicIndexEntry [termID=" + termID + ", docVector=" + docVector
				+ ", freqVector=" + freqVector + ", offsetBlockPosition="
				+ offsetBlockPosition + ", offsetBlockLength="
				+ offsetBlockLength + ", readerFrequency=" + readerFrequency
				+ "]";
	}
	
	
	

}
