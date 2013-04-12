/**
 * 
 */
package lexicon;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author Darshan
 *
 */
public class Lexicon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5995529641400938710L;
	private Map<Integer, LexiconValue> map;
	
	
	/**
	 * @param lexiconFileName
	 * @param lexiconObjectName
	 * @param map
	 * @param path
	 * @param objectFilePath
	 * @param fileWriter
	 * @param objectOutputStream
	 * @throws IOException 
	 */
	public Lexicon()  {
		super();
		this.map = new HashMap<Integer, LexiconValue>();
		
		
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public List<BlockInfo> get(Object arg0) {
		return map.get(arg0).getBlockInfos();
	}

	public BlockInfo get(Object arg0, int fileNumber){
		for(BlockInfo blockInfo: map.get(arg0).getBlockInfos()){
			if(blockInfo.getFileNumber()==fileNumber)
				return blockInfo;
		}
		return null;
	}
	
	public synchronized void put(Integer termId,int freq ,BlockInfo blockInfo){
		if(!map.containsKey(termId))
			//TODO Put in a parameter for the arraylist. Rebuild it to ArrayList after Indexing
			map.put(termId, new LexiconValue( new LinkedList<BlockInfo>(),0));
		map.get(termId).getBlockInfos().add(blockInfo);
		map.get(termId).incrGlobalFreq(freq);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Lexicon [map=" + map + "]";
	}

	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	public int size() {
		return map.size();
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object arg0) {
		return map.containsKey(arg0);
	}

	/**
	 * @return
	 * @see java.util.Map#keySet()
	 */
	public Set<Integer> keySet() {
		return map.keySet();
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public LexiconValue remove(Object arg0) {
		return map.remove(arg0);
	}
	
	
	
	
}
