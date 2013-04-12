/**
 * 
 */
package structures;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Darshan
 *
 */
public class ContextRegistrar implements Serializable {
	public int contextRegistrarBaseSize = 20;
    private Map<String, Integer> invertedContextMap;
    private int wordIDCount;
    private String path;
    private FileWriter fileWriter;
    
    
    /**
	 * @param ContextRegistrarBaseSize
	 * @param path
     * @throws IOException 
	 */
	public ContextRegistrar(int contextRegistrarBaseSize, String path) throws IOException {
		super();
		this.contextRegistrarBaseSize = contextRegistrarBaseSize;
		this.path = path;
		wordIDCount = 0;
		fileWriter = new FileWriter(path);
		invertedContextMap = new HashMap<String, Integer>(contextRegistrarBaseSize);
	}

	public void close() throws IOException{
		for(String context: invertedContextMap.keySet() ){
			fileWriter.write((context+"|@|"+invertedContextMap.get(context)));
			fileWriter.write("\n");
		}
		fileWriter.close();
	}



	/**
     * registers a word and return wordID. The method is synchronized.
     * @param url
     * @return
     */
    public synchronized int register(String word){
	if(!invertedContextMap.containsKey(word)){
	    wordIDCount++;
	//    wordMap.put(wordIDCount, word);
	    invertedContextMap.put(word, wordIDCount);
	    return wordIDCount;
	}
	return invertedContextMap.get(word);
    }
    

}
